/*
 * Copyright (C) 2007  Hin-Tak Leung
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA.
 */

#include <zlib.h>
#include "R.h"
#include "Rinternals.h"

#include "read_pedfile_priv.h"

#define MAX_FAMILY_NAME 64
#define MAX_MEMBER_ID 64

typedef struct linecontent {
  struct linecontent *next;
  char family[MAX_FAMILY_NAME];
  char member[MAX_MEMBER_ID];
  int father;
  int mother;
  int first_offspring;
  int next_paternal_sib;
  int next_maternal_sib;
  int sex;
  int affected;
  char *snps;
} *linecontent_ptr;

static void contentlist_destroy(linecontent_ptr start) {
  linecontent_ptr cur_content_ptr = NULL;
  for(cur_content_ptr = start; cur_content_ptr ; /* advancing pointer inside */ ) {
    linecontent_ptr copy = cur_content_ptr; /* make a copy before moving on */
    cur_content_ptr = cur_content_ptr->next;
    free(copy->snps);
    free(copy);
  }
}

#define NA_IF_ZERO(x)  ((x)? (x) : NA_INTEGER)  

SEXP read_pedfile(SEXP in_file, SEXP snp_names, SEXP missing, SEXP X, SEXP sep) {
  const char *filename = NULL;
  // int snp_length = 0;
  int n_samples = 0;
  int n_snps = 0;
  int is_X = 0; /* default FALSE */
  char *translation = NULL;
  int i_snps = 0;

  struct linecontent listhead;
  linecontent_ptr cur_content_ptr = &listhead;


  /* check inputs */
  if(TYPEOF(in_file) != STRSXP)
    error("input filename wrong type\n");
  if(TYPEOF(sep) != STRSXP)
    error("input sep wrong type\n");
  if((missing != R_NilValue) && (TYPEOF(missing) != STRSXP))
    error("input missing wrong type\n");
  if(TYPEOF(X) != LGLSXP)
    error("input X wrong type\n");

  if ((snp_names != R_NilValue) && (TYPEOF(snp_names) != STRSXP)) {
    error("input snp_names wrong type\n");
  }

  filename = CHAR(STRING_ELT(in_file, 0));
  gzFile filehandle = gzopen(filename, "rb");

  if (!filehandle) {
    Rprintf("Cannot read %s\n", filename);
    return R_NilValue;
  }
  // Rprintf("Reading %s ...\nCan take a while...\n", filename);

  is_X = LOGICAL(X)[0];
  // if (snp_names != R_NilValue) {
  //  snp_length = LENGTH(snp_names);
  // }

  /* building the translation table */
  translation = malloc(128);
  memset(translation, INVALID_MASK, 128); /* set all unknowns to INVALID first */
  int i;
  for(i = 0; i < BUILDIN_TABLE_LENGTH; i++) {
    translation[(int) buildin[i][0] ] = buildin[i][1]; 
  }

  /* work out line length and number of fields */
  /* read 4k blocks until the first line ending, then rewind,
   to count number of fields, and line length */
#define BUF_SIZE 4192
  char buffer[BUF_SIZE+1]; /* add one so we'll can null terminate later */
  int read_done = 0; /* not done */
  int n_buf = 0; /* used for line buffer size below */
  int n_read =0;
  int n_fields = 0;
  int is_blank = 1; /* start with white space, so the first non-white counts as one */
  while((n_read = gzread(filehandle, buffer, BUF_SIZE)) != 0) {
    char *b_ptr = buffer;
    n_buf++; /* increment the size of the line buffer needed later */
    /* terminate explicitly as the buffer may contain garbage from last
       read after the end of current read */
    buffer[n_read] = '\0';
    while(*b_ptr) {
      char a = *b_ptr;
      if (a == '\n') {
	read_done = 1;
	break;
      }
      if ((a == ' ') || (a == '\t') || (a == '\f') || (a == '\r') || (a == '\v')) {
	is_blank = 1;
      } else {
	if (is_blank) {
	  /* counting blank to non-blank transitions */
	  n_fields++;
	}
	is_blank = 0; /* is non-blank */
      }
      b_ptr++;
    }
    if (read_done)
      break;
  }
  if (!(read_done)) {
    Rprintf("Had read error after scanning for %i fields in header\n", n_fields);
    return R_NilValue;
  } else {
//    if (n_fields % 2) {
//      Rprintf("Found odd number of %i fields in the header. Please check input\n", n_fields);
//      return R_NilValue;
//    }
    n_snps = (n_fields - 9)/2 ;
    // Rprintf("Found %i fields in the header, i.e. %i snps, hopefully...\n", n_fields, n_snps);
  }
  gzrewind(filehandle);

  int line_buffer_size = n_buf * BUF_SIZE;
  char *current_line = malloc(line_buffer_size);

  char *snp_summary = calloc(n_snps + 0,1);

  int read_failed = 1;
  while (1) {
    int get1 = 0;
    /* read until breaking out because of eof or error */
    if (gzeof(filehandle)) {
      gzclose(filehandle);
      // Rprintf("current line [%d] : %.20s...\n", n_samples - 1, current_line);
      // Rprintf("EOF reached after %d samples\n", n_samples);
      read_failed = 0; /* read_failed = "no" */
      break;
    }
    
    /* gzeof() is not reliable for non-compressed files,
       trying alternative method for testing end of file*/
    if ((get1 = gzgetc(filehandle)) != -1) {
      /* get1 was successful, put it back */
      if (gzungetc(get1, filehandle) != get1) {
        Rprintf("read error (gzungetc) after %d samples\n", n_samples);
        break;
      }
    } else {
      if (gzeof(filehandle)) {
        gzclose(filehandle);
        // Rprintf("last line [%d] : %.30s...\n", n_samples - 1, current_line);
        // Rprintf("EOF reached after %d samples\n", n_samples);
        read_failed = 0; /* read_failed = "no" */
        break;
      }
    }
    /* normal eof detection ends - below are either processing or errors */
    
    if (gzgets(filehandle, current_line, line_buffer_size) == Z_NULL) {
      Rprintf("read error (gzgets) after %d samples\n", n_samples);
      break;
    }

    // Forgiving empty lines in data file
    char a='a', *line_pt = current_line;
    while(*line_pt) {
       a = *line_pt;
       if ((a == ' ') || (a == '\t') || (a == '\f') || (a == '\r') || (a == '\v'))
          line_pt++;
       else break;
    } if (a == '\n') continue;

    n_samples++;

    /* now we have got a line, start stripping it apart */
    cur_content_ptr->next = (linecontent_ptr)calloc(sizeof(struct linecontent), 1);
    cur_content_ptr = cur_content_ptr->next;
    /* doing the first 3 pairs blindly,
       for performance reasons */       
    cur_content_ptr->snps = (char *)calloc(sizeof(char), n_snps + 0);
    char *snps = cur_content_ptr->snps; /* short hand */
    linecontent_ptr l = cur_content_ptr ; /* just a lazy short hand */
    int i = sscanf(current_line, "%s %s %d %d %d %d %d %d %d",
		   l->family,
		   l->member,
		   &(l->father),
		   &(l->mother),
		   &(l->first_offspring),
		   &(l->next_paternal_sib),
		   &(l->next_maternal_sib),
		   &(l->sex),
		   &(l->affected));
    if ((i != 9) || (i== EOF)) {
      Rprintf("malformed input line: [%20s...]\n", current_line);
      break;
    }
    
    /* now we'll going to play with the snp data...*/
    is_blank = 1;
    char *line_ptr = current_line;

    n_fields = 0;
    int count_support_fields = 0;
    while(*line_ptr) {
      char a = *line_ptr;
      if (a == '\n') {
  	  break;
      }
      if ((a == ' ') || (a == '\t') || (a == '\f') || (a == '\r') || (a == '\v')) {
	is_blank = 1;
      } else {

	if (is_blank) {
        /* Discard first 9 columns: */
        if (count_support_fields < 9) {line_ptr++; count_support_fields++; is_blank = 0; continue;}
	  int idx = n_fields>>1;
	  if(a & 0x80) {
	    a = 0x7f;
	  }
	  snps[idx] |= translation[(int)a];
	  snp_summary[idx] |= translation[(int)a];
	  n_fields++;
	}
	is_blank = 0; /* is non-blank */
      }
      line_ptr++;
    } /* while line_ptr */
  } /* while (1) */

  if(read_failed) {
    /* no message, as any relevant message should be above
       where the error occurs */
    return R_NilValue;
  } else {
    // Rprintf("Read %i samples from input, now converting...\n", n_samples);
  }

  /* we'll set the multiallelic mask now that we have seen all 
     the genotypes, and also do a bit of summary output now */
  int n_invalid = 0;
  int n_multiallelic = 0;
  int n_invalid_or_multiallelic = 0;
  for(i_snps = 0 ; i_snps < n_snps; i_snps ++) {
    char *snp_sum = snp_summary + 0;
    if ((*snp_sum) & INVALID_MASK) {
      n_invalid ++;
    }
    if (resultmap[(int) ((*snp_sum) & 0x0f)].allele1 & MULTIALLELIC_MASK) {
      (*snp_sum) |= MULTIALLELIC_MASK;
      n_multiallelic ++;
    }
    if ( (*snp_sum) & (INVALID_MASK|MULTIALLELIC_MASK)) {
      n_invalid_or_multiallelic++;
    }    
  }
  if (n_invalid) {
    Rprintf("%i snps contains invalid entries.\n", n_invalid);
  }
  if (n_multiallelic) {
    Rprintf("%i snps are multiallelic.\n", n_multiallelic);
  }
  if (n_invalid) {
    Rprintf("%i invalid/multi-allelic entries will be marked all missing\n", n_invalid_or_multiallelic);
  }

  SEXP family = R_NilValue, member= R_NilValue, father= R_NilValue;
  SEXP mother= R_NilValue, sex = R_NilValue, affected = R_NilValue;
  SEXP first_offspring = R_NilValue, next_paternal_sib = R_NilValue, next_maternal_sib = R_NilValue;
  SEXP snp_data = R_NilValue;

  /* sample support data */
  PROTECT(family   = allocVector(STRSXP, n_samples));

  PROTECT(member   = allocVector(STRSXP, n_samples));
  PROTECT(father   = allocVector(INTSXP, n_samples));
  PROTECT(mother   = allocVector(INTSXP, n_samples));
  PROTECT(first_offspring   = allocVector(INTSXP, n_samples));
  PROTECT(next_paternal_sib   = allocVector(INTSXP, n_samples));
  PROTECT(next_maternal_sib   = allocVector(INTSXP, n_samples));

  PROTECT(sex      = allocVector(INTSXP, n_samples));
  PROTECT(affected = allocVector(INTSXP, n_samples));
  int protected = 9;

  SEXP sample_names;
  PROTECT(sample_names = allocVector(STRSXP, n_samples));
  /* filling the sample support data in */
  int i_sample = 0;
  for(cur_content_ptr = listhead.next; cur_content_ptr ; cur_content_ptr = cur_content_ptr->next) {
    char samplename_buffer[4*MAX_FAMILY_NAME]; /* Was 2 times for family + individual, which looks right, but a recent compiler is complaining, so upped buffer size */
    snprintf(samplename_buffer, 4*MAX_FAMILY_NAME, "Family.%s.Individual.%s", cur_content_ptr->family, 
	     cur_content_ptr->member);
    SET_STRING_ELT(sample_names, i_sample, mkChar(samplename_buffer));
    SET_STRING_ELT(family, i_sample, mkChar(cur_content_ptr->family));
    SET_STRING_ELT(member, i_sample, mkChar(cur_content_ptr->member));
    INTEGER(father  )[i_sample] = NA_IF_ZERO(cur_content_ptr->father  );
    INTEGER(mother  )[i_sample] = NA_IF_ZERO(cur_content_ptr->mother  );
    INTEGER(first_offspring)[i_sample] = NA_IF_ZERO(cur_content_ptr->first_offspring  );
    INTEGER(next_paternal_sib)[i_sample] = NA_IF_ZERO(cur_content_ptr->next_paternal_sib  );
    INTEGER(next_maternal_sib)[i_sample] = NA_IF_ZERO(cur_content_ptr->next_maternal_sib  );

    INTEGER(sex     )[i_sample] = NA_IF_ZERO(cur_content_ptr->sex     );
    INTEGER(affected)[i_sample] = NA_IF_ZERO(cur_content_ptr->affected);
    i_sample++;
  }


  /* the rest of the sample support data frame */
  SEXP sample_support_df, sample_support_class, sample_support_df_names;
  PROTECT(sample_support_df_names = allocVector(STRSXP, 9));
  PROTECT(sample_support_df = allocVector(VECSXP, 9));
  SET_VECTOR_ELT(sample_support_df, 0, family);
  SET_VECTOR_ELT(sample_support_df, 1, member);
  SET_VECTOR_ELT(sample_support_df, 2, father);
  SET_VECTOR_ELT(sample_support_df, 3, mother); /* doing item 4, 5 later */
  SET_VECTOR_ELT(sample_support_df, 4, first_offspring);
  SET_VECTOR_ELT(sample_support_df, 5, next_paternal_sib);
  SET_VECTOR_ELT(sample_support_df, 6, next_maternal_sib);

  SET_STRING_ELT(sample_support_df_names, 0, mkChar("Family"));
  SET_STRING_ELT(sample_support_df_names, 1, mkChar("Member"));
  SET_STRING_ELT(sample_support_df_names, 2, mkChar("Father"));
  SET_STRING_ELT(sample_support_df_names, 3, mkChar("Mother"));
  SET_STRING_ELT(sample_support_df_names, 4, mkChar("First Offspring"));
  SET_STRING_ELT(sample_support_df_names, 5, mkChar("Next Paternal Sibling"));
  SET_STRING_ELT(sample_support_df_names, 6, mkChar("Next Maternal Sibling"));

  SET_STRING_ELT(sample_support_df_names, 7, mkChar("Sex"));
  SET_STRING_ELT(sample_support_df_names, 8, mkChar("Affected"));
  protected +=3;

  SEXP sex_levels, classattr_factors, affected_levels;

  PROTECT(sex_levels = allocVector(STRSXP, 2));
  SET_STRING_ELT(sex_levels, 0, mkChar("Male"));
  SET_STRING_ELT(sex_levels, 1, mkChar("Female"));
  setAttrib(sex, R_LevelsSymbol, sex_levels);

  PROTECT(classattr_factors = allocVector(STRSXP, 1));
  SET_STRING_ELT(classattr_factors, 0, mkChar("factor"));
  classgets(sex, classattr_factors);
  SET_VECTOR_ELT(sample_support_df, 7, sex);   /* item 7 done */

  PROTECT(affected_levels = allocVector(STRSXP, 2));
  SET_STRING_ELT(affected_levels, 0, mkChar("Unaffected"));
  SET_STRING_ELT(affected_levels, 1, mkChar("Affected"));
  setAttrib(affected, R_LevelsSymbol, affected_levels);

  classgets(affected, duplicate(classattr_factors));
  SET_VECTOR_ELT(sample_support_df, 8, affected); /* item 8 done */

  setAttrib(sample_support_df, R_RowNamesSymbol, sample_names);
  setAttrib(sample_support_df, R_NamesSymbol, sample_support_df_names);
  PROTECT(sample_support_class = allocVector(STRSXP, 1));
  SET_STRING_ELT(sample_support_class, 0, mkChar("data.frame"));
  classgets(sample_support_df, sample_support_class);
  protected += 4;

  /* main snp data */
  SEXP female = R_NilValue, slot = R_NilValue;
  PROTECT(snp_data = allocMatrix(RAWSXP, n_samples, n_snps));
  memset(RAW(snp_data), 0x00, n_samples * n_snps); /* filling it with zero */
  protected++;

  int i_samples = 0;
  int male_as_hets =0;
  int partially_missing = 0;
  for(cur_content_ptr = listhead.next; cur_content_ptr ; cur_content_ptr = cur_content_ptr->next) {
    char *snps = (cur_content_ptr->snps) + 0;

    for (i_snps = 0; i_snps < n_snps; i_snps++) {
      char snp_sum = snp_summary[i_snps + 0];
      int idx = i_snps * n_samples + i_samples;




      if(( snp_sum & (INVALID_MASK|MULTIALLELIC_MASK) ) ||
	 ( (snps[i_snps] & MISSING_MASK) && ((!is_X) || (cur_content_ptr->sex != 1)) ) ||
	 ( !(snps[i_snps] & ~MISSING_MASK) )
	 ) {
	/* (snp is invalid/multiallelic) or
	   (sample has some missingness and (chrom is non-X or sample is non-male) or
	   (sample is all missing regardless)
        */
	if ( (!( snp_sum & (INVALID_MASK|MULTIALLELIC_MASK) )) &&  
	     (snps[i_snps] & ~MISSING_MASK)) {
	  /* snp is not invalid, and sample has non-missing parts */
	  partially_missing++;
	}
	    
	RAW(snp_data)[idx] = 0x00;
      } else {
	/* sample is non-missing, or (X and male) */
	snp_sum &= ~ MISSING_MASK; /* squash the missing mask */
	snps[i_snps] &= ~MISSING_MASK; 
	if (snps[i_snps] == resultmap[(int) snp_sum].allele1) {
	  /* only A, code as AA */
	  RAW(snp_data)[idx] = 0x01;
	} else if (snps[i_snps] == resultmap[(int) snp_sum].allele2) {
	  /* only B, code as BB */
	  RAW(snp_data)[idx] = 0x03;
	} else {
	  /* a mixture of A and B, or no-call in male on X  */
	  if ((is_X) && (cur_content_ptr->sex == 1)) {
	    /* hets or no-call in male/X are both no calls */
	    RAW(snp_data)[idx] = 0x00;
	    /* but counting hets in male/X and emit a warning later */
	    if (snps[i_snps]) {
	      male_as_hets ++;
	    }
	  } else {
	    RAW(snp_data)[idx] = 0x02;
	  }
	}
      }
    }
    i_samples++;
  }
  if(male_as_hets) {
    Rprintf("%i heterozygotic record for known male has been detected and corrected as missing\n", male_as_hets);  
  }
  if(partially_missing) {
    Rprintf("%i partially missing record treated as missing\n", partially_missing);  
  }

  if(is_X) {
    PROTECT(female = allocVector(LGLSXP, n_samples));
    int i_samples = 0;
    for(cur_content_ptr = listhead.next; cur_content_ptr ; cur_content_ptr = cur_content_ptr->next) {
      LOGICAL(female)[i_samples] = ( !((cur_content_ptr->sex) ==1) );
      i_samples++;
    }
    PROTECT(slot  = allocVector(STRSXP, 1));
    protected += 2;
    SET_STRING_ELT(slot, 0, mkChar("Female"));
  }

  SEXP snp_data_class = R_NilValue, snp_data_dimnames = R_NilValue, snp_data_colnames = R_NilValue;
  PROTECT(snp_data_class = allocVector(STRSXP, 1));
  if (is_X) {
    SET_STRING_ELT(snp_data_class, 0, mkChar("X.snp.matrix"));
    R_do_slot_assign(snp_data, slot, female);
  } else {
    SET_STRING_ELT(snp_data_class, 0, mkChar("snp.matrix"));
  }
  SEXP Package;
  PROTECT(Package = allocVector(STRSXP, 1));
  SET_STRING_ELT(Package, 0, mkChar("chopsticks"));
  setAttrib(snp_data_class, install("package"), Package);
  classgets(snp_data, snp_data_class);
  SET_S4_OBJECT(snp_data);

  PROTECT(snp_data_dimnames = allocVector(VECSXP, 2));
  SET_VECTOR_ELT(snp_data_dimnames, 0, duplicate(sample_names));
  if (snp_names !=R_NilValue) {
    SET_VECTOR_ELT(snp_data_dimnames, 1, duplicate(snp_names));
  } else {
    int i = 0;
    char buffer[16]; /* that should cover 99999 99999 99999 snps */
    PROTECT(snp_data_colnames = allocVector(STRSXP, n_snps));
    for (i=0 ; i < n_snps ; i++) {
      sprintf(buffer, "%d", i+1);
      SET_STRING_ELT(snp_data_colnames, i, mkChar(buffer));
    }
    protected++;
    SET_VECTOR_ELT(snp_data_dimnames, 1, snp_data_colnames);
  }
  setAttrib(snp_data, R_DimNamesSymbol, snp_data_dimnames);
  protected +=3;

  /* snp support data - really, just assigment information */
  SEXP snp_support_df = R_NilValue, snp_support_names;
  if (snp_names !=R_NilValue) {
    PROTECT(snp_support_names = duplicate(snp_names));
  } else {
    PROTECT(snp_support_names = duplicate(snp_data_colnames));
  }
  PROTECT(snp_support_df = allocVector(STRSXP, n_snps));
  for (i_snps = 0; i_snps < n_snps ; i_snps++) {
    char snp_sum = snp_summary[i_snps+0];
    if( snp_sum & (INVALID_MASK|MULTIALLELIC_MASK) ) {
      SET_STRING_ELT(snp_support_df, i_snps, NA_STRING);
    } else {
      /* squash the missing mask */
      snp_sum &=  ~ MISSING_MASK;
      SET_STRING_ELT(snp_support_df, i_snps, mkChar(resultmap[(int) snp_sum].message));
    }
  }
  setAttrib(snp_support_df, R_NamesSymbol, snp_support_names);
  protected +=2;

  /* clear the temp content line */
  contentlist_destroy(listhead.next);
  free(translation);
  free(current_line);
  free(snp_summary);

  /* final result */
  SEXP ans = R_NilValue, ans_names = R_NilValue;
  PROTECT(ans = allocVector(VECSXP, 3));
  SET_VECTOR_ELT(ans, 0, snp_data);
  SET_VECTOR_ELT(ans, 1, sample_support_df);
  SET_VECTOR_ELT(ans, 2, snp_support_df);
  PROTECT(ans_names = allocVector(STRSXP, 3));
  SET_STRING_ELT(ans_names, 0, mkChar("snp.data"));
  SET_STRING_ELT(ans_names, 1, mkChar("subject.support"));
  SET_STRING_ELT(ans_names, 2, mkChar("snp.support"));
  setAttrib(ans, R_NamesSymbol, ans_names);
  protected += 2;

  UNPROTECT(protected);
  // Rprintf("...done, %i samples with %i snps\n", n_samples, n_snps);
  return ans;
}
