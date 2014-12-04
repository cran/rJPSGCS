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


package jpsgcs.alun.gchap;

import jpsgcs.alun.util.StringFormatter;
import jpsgcs.alun.genio.GeneticDataSource;

/**
 A class for formatting output.
*/
public class HapFormatter
{
/**
 Returns a string representation of the haplotypes and 
 frequencies for the given observation set.
*/
	public static String formatHaplotypes(Observation y)
	{
		double[] freq = y.getTrait().getLocus().alleleFrequencies();
		String[] names = y.getTrait().getLocus().alleleNames();

		StringBuffer s = new StringBuffer();
		for (int i=0; i<freq.length; i++)
			//s.append(StringFormatter.format(100*freq[i],5)+"\t"+names[i]+"\n");
			s.append(freq[i]+"\t"+names[i]+"\n");
		s.setLength(s.length()-1);

		return s.toString();
	}

/**
 Returns a string representation of the most likely pair of 
 haplotypes for each observed phenotype.
*/
	public static String formatGuesses(Observation y, GeneticDataSource gds)
	{
		StringBuffer s = new StringBuffer();
		
		Phenotype[] o = y.getData();
		Locus l = y.getTrait().getLocus();
		for (int i=0; i<o.length; i++)
		{
			s.append("\n");
			s.append( gds.individualName(i)+"\n");
			s.append("Phenotype: "+o[i]+"\n");

			Genotype g = null;
			Genotype best = null;
			double tot = 0;	
			double bb = 0;
			for (o[i].initGenotypes(); (g=o[i].nextGenotype()) != null; )
			{
				double f = l.alleleFrequencies()[g.a];
				f *= l.alleleFrequencies()[g.b];
				if (g.a != g.b)
					f *= 2;
				
				tot += f;
				if (f > bb)
				{
					best = g;
					bb = f;
				}
			}

			s.append("Best explanation\n");
			s.append("\t"+l.alleleNames()[best.a]);
			s.append("\n");
			s.append("\t"+l.alleleNames()[best.b]);
			s.append("\n");
			s.append("\t"+((int)(100*100*bb/tot)/100.0)+"%\n");
		}

		return s.toString();
	}
}
