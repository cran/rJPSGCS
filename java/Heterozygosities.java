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


import jpsgcs.alun.linkage.LinkageParameterData;
import jpsgcs.alun.linkage.LinkageFormatter;

/**
        This program reads in a LINKAGE parameter, and outputs the heterozygosity 
	measure for each locus. 

<ul>
        Usage : <b> java Heterozygosities < input.par  </b> </li>
</ul>
        where
<ul>
<li> <b> input.par </b> is the input LINKAGE parameter file. </li>
</ul>
<p>	The heterozygosity score is the probability that 
	a randomly chosen individual is heterozygous and is calculated at 
	1 minus the sum of the squares of the 
	allele frequencies.

*/

public class Heterozygosities
{
	public static void main(String[] args)
	{
		try
		{
			LinkageParameterData p = new LinkageParameterData(new LinkageFormatter());

			for (int i=0; i<p.nLoci(); i++)
			{
				double[] a = p.getLocus(i).alleleFrequencies();
				double h = 0;
				for (int j=0; j<a.length; j++)
					h += a[j];
				for (int j=0; j<a.length; j++)
					a[j] /= h;
				h = 1;
				for (int j=0; j<a.length; j++)
					h -= a[j]*a[j];

				System.out.println(h);
			}
		}
		catch (Exception e)
		{
			System.err.println("Caught in Heterozygocities:main().");
			e.printStackTrace();
		}
	}
}
