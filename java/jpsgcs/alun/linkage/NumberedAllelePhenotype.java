/*
 Copyright 2010 Alun Thomas.

This file is part of JPSGCS.

JPSGCS is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

JPSGCS is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with JPSGCS.  If not, see <http://www.gnu.org/licenses/>.
*/


package jpsgcs.alun.linkage;

/**
 This class represents the data for an individual for a locus
 in the linkage numbered allele input format.
*/
public class NumberedAllelePhenotype extends LinkagePhenotype
{
/** 
 Creates a new phenotype given the specified allele numbers and
 checks that the data is consistent with the parameters for 
 the given locus.
*/
	public NumberedAllelePhenotype(NumberedAlleleLocus l, int allele1, int allele2)
	{
		setLocus(l);

		if (allele1 < 0 || allele1 > l.alleleFrequencies().length)
			throw new LinkageException("Allele number out of range "+allele1);
		else
			a1 = allele1;
		
		if (allele2 < 0 || allele2 > l.alleleFrequencies().length)
			throw new LinkageException("Allele number out of range "+allele2);
		else
			a2 = allele2;
	}

	public LinkagePhenotype nullCopy()
	{
		return new NumberedAllelePhenotype((NumberedAlleleLocus) getLocus(),0,0);
	}

/**
 Returns a string giving the numbers of the alleles at this
 locus for the individual whose data this is.
*/
	public String toString()
	{
		return f.format(a1,2)+" "+f.format(a2,2);
	}

	public void reCode(int[] c)
	{
		a1 = c[a1];
		a2 = c[a2];
	}

/**
 The first allele at this locus for the individual whose data this is.
*/
	public int a1 = 0;
/**
 The second allele at this locus for the individual whose data this is.
*/
	public int a2 = 0;
}
