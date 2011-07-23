#include <stdio.h>

void write_as_transposed_pedfile(char **file, int *y, char *x, int *N, int *M, int *P,
		     char **sep, char **eol, char **na, int *iferror) {
  int nrow = *N;
  int ncol = *M;
  int pcol = *P;
  FILE *  outfile;
  int i=0, j=0, k=0, ik=0;
  outfile = fopen(*file, "w");
  if (!outfile) {
    *iferror = 1;
    return;
  }
  fprintf(outfile, "%d", nrow);
  fputs(*eol, outfile);

  for (i=0; i<nrow; i++) { 
    for (k=0, ik=i; k<pcol; k++, ik+=nrow) {
      fprintf(outfile, "%d", y[ik]);
      fputs(*sep, outfile);
    }
    fputs(*eol, outfile);
  }

  for (j=0; j<ncol; j++) { 
    for (i=0; i<nrow; i++) { 
	int  g = (int) x[j*nrow+i];
	if (!g) {
	  fputs(*na, outfile);
	  fputs(*sep, outfile);
	  fputs(*na, outfile);
	  fputs(*sep, outfile);
	}
	else {
	  fputc(g<3? '1': '2', outfile);
	  fputs(*sep, outfile);
	  fputc(g<2? '1': '2', outfile);
	  fputs(*sep, outfile);
	}
    }
    fputs(*eol, outfile);
  }
  fclose(outfile);
  *iferror = 0;
  return;
}
