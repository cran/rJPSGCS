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
 This represents the data for an individual at a locus
 specified in the linkage affected status format.
*/
public class AffectionStatusPhenotype extends LinkagePhenotype
{
/**
 Creates a new phenotype from the given status and liability class numbers.
 The data is checked for consistency with the data for the specified locus.
*/
	public AffectionStatusPhenotype(AffectionStatusLocus l, int stat, int liab)
	{
		setLocus(l);

		switch(status)
		{
		case UNKNOWN:
		case UNAFFECTED:
		case AFFECTED:
			status = stat;
			break;
		default:
			throw new LinkageException("Inappropriate affectation status "+status);
		}

		nliab = l.getLiabilities().length;

		//if (liab < 1 || liab > l.getLiabilities().length)
		if (liab < 0 || liab > nliab)
			throw new LinkageException("Liability class out of range 0 to "+nliab+" : "+liab);
		else
			liability = liab;
		
	}

	public LinkagePhenotype nullCopy()
	{
		return new AffectionStatusPhenotype((AffectionStatusLocus)getLocus(),0,1);
	}

/**
 Returns a string representation of the data.
*/
	public String toString()
	{
		if (nliab > 1)
			return f.format(status,2)+" "+f.format(liability,2);
		else
			return f.format(status,2);
	}

// Private data.

	public static final int UNKNOWN = 0;
	public static final int AFFECTED = 2;
	public static final int UNAFFECTED = 1;

	public int status = 0;
	public int liability = 0;
	public int nliab = 0;
}
