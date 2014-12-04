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

import jpsgcs.alun.util.StringFormatter;
import java.io.IOException;

/**
 This class represents the a locus given
 in the linkage numbered locus format.
*/
public class NumberedAlleleLocus extends LinkageLocus
{
	public NumberedAlleleLocus()
	{
	}

/**
 Creates a new locus from data read from the given input formatter.
*/
	public NumberedAlleleLocus(LinkageFormatter b) throws IOException
	{
		type = NUMBERED_ALLELES;

		int na = b.readInt("number of alleles",0,true,true);
		line1comment = b.restOfLine();
		
		freq = new double[na];
		b.readLine();
		for (int i=0; i<freq.length; i++)
		{
			freq[i] = b.readDouble("allele frequency "+i,0,true,true);
			if (freq[i] < 0)
				b.crash("Negative allele frequency "+freq[i]);
		}

		checkAndSetAlleleFrequencies(freq,b);

		line2comment = b.restOfLine();
	}

	public NumberedAlleleLocus(NumberedAlleleLocus n)
	{
		super(n);
		type = NUMBERED_ALLELES;
		line2comment = n.line2comment;
	}

/**
 Reads the phenotypic data in the format associated with this
 type of locus from the given input formatter.
*/
	public LinkagePhenotype readPhenotype(LinkageFormatter f)
	{
		int a = f.readInt("allele code",0,true,false);
		int b = f.readInt("allele code",0,true,false);

		if (a < 0 || a > freq.length || b < 0 || b > freq.length)
		{
			if (a < 0 || a > freq.length)
				f.warn(locName()+": allele code "+a+" is out of range "+0+" "+(freq.length)+"\n\tSetting genotype to 0 0");
			if (b < 0 || b > freq.length)
				f.warn(locName()+": allele code "+b+" is out of range "+0+" "+(freq.length)+"\n\tSetting genotype to 0 0");
			a = 0;
			b = 0;
		}
/*
		if (a < 0 || a > freq.length)
		{
			f.warn(locName()+": allele code "+a+" is out of range "+0+" "+(freq.length)+"\n\tSetting to 0");
			a = 0;
		}
		if (b < 0 || b > freq.length)
		{
			f.warn(locName()+": allele code "+b+" is out of range "+0+" "+(freq.length)+"\n\tSetting to 0");
			b = 0;
		}
*/
		return new NumberedAllelePhenotype(this,a,b);
	}

/**
 Returns a String representing the data for this locus in the
 same format as given in the linkage .par file.
*/
	public String toString()
	{
		StringBuffer s = new StringBuffer();
		
		s.append(type+" "+freq.length+" "+line1comment+"\n");
		
		for (int i=0; i<freq.length; i++)
		{
			s.append(StringFormatter.format(freq[i],2,6));
			s.append(" ");
		}
		s.append(line2comment+"\n");

		return s.toString();
	}

	public void reCode(int[] c)
	{
		int n = 0;
		for (int i=0; i<c.length; i++)
			if (n < c[i]) 
				n = c[i];

		double[] newf = new double[n];
		double tot = 0;
		for (int i=1; i<c.length; i++)
			if (c[i] > 0)
			{
				newf[c[i]-1] += freq[i-1];
				tot += freq[i-1];
			}

		for (int i=0; i<newf.length; i++)
			newf[i] /= tot;
	
		freq = newf;
	}

// Private data.

	public String line2comment;
}
