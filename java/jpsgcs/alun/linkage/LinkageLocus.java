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
 This is the base class from which the linkage locus types are
 derived.
 Only the numbered alleles type of locus is currently properly 
 implemented.
*/

abstract public class LinkageLocus 
{
	public LinkageLocus(LinkageLocus l)
	{
		freq = new double[l.freq.length];
		for (int i=0; i<freq.length; i++)
			freq[i] = l.freq[i];
		line1comment = l.line1comment;
	}

	public LinkageLocus()
	{
	}

/**
 The type of the locus specified. This will be one of
QUANTITATIVE_VARIABLE, AFFECTION_STATUS BINARY_FACTORS or NUMBERED_ALLELES.
*/
	public int type = 0;

/**
 Represents the linkage quantiative variable locus type.
*/
	public final static int QUANTITATIVE_VARIABLE = 0;
/**
 Represents the linakge affectation status locus type.
*/
	public final static int AFFECTION_STATUS = 1;
/**
 Represents the linkage binary factors locus type.
*/
	public final static int BINARY_FACTORS = 2;
/**
 Represents the linkage numbered alleles locus type.
*/
	public final static int NUMBERED_ALLELES = 3;

/**
 A method to read in the data for this locus from the given
 input formatter. This is defined by the subclasses.
*/
	abstract public LinkagePhenotype readPhenotype(LinkageFormatter f);

/**
 Returns the number of alleles that this locus has.
*/
	public int nAlleles()
	{
		return freq.length;
	}

/**
 Returns the allele frequencies for the locus.
*/
	public double[] alleleFrequencies()
	{
		return freq;
	}

	public void setAlleleFrequencies(double[] f)
	{
		freq = f;
	}

	public void checkAndSetAlleleFrequencies(double[] f, LinkageFormatter b)
	{
		double tot = 0;
		for (int i=0; i<f.length; i++)
			tot += f[i];

		if (tot <= 0)
		{
			b.warn("Allele frequencies sum to zero. Setting to uniform");
			for (int i=0; i<f.length; i++)
				f[i] = 1.0/f.length;
		}
			
		freq = f;
	}

/**
 Returns the comment string from the first line of input
 associated with this locus.
*/
	public String firstComment()
	{
		return line1comment;
	}

	public String locName()
	{
		StringBuffer b = new StringBuffer(line1comment);
		if (b.length() > 0)
		{
			while (b.charAt(0) == ' ' || b.charAt(0) == '\t')
				b.deleteCharAt(0);
			while (b.charAt(b.length()-1) == ' ')
				b.deleteCharAt(b.length()-1);
		}
		return b.toString();
	}

// Protected data.

	public double[] freq = null;
	public String line1comment = null;
}
