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


package jpsgcs.alun.pedapps;

import jpsgcs.alun.genio.BasicGeneticData;
import jpsgcs.alun.markov.GraphicalModel;
import jpsgcs.alun.markov.Table;
import jpsgcs.alun.pedmcmc.Allele;
import jpsgcs.alun.pedmcmc.Inheritance;
import jpsgcs.alun.pedmcmc.LocusProduct;


import jpsgcs.alun.markov.Function;
import jpsgcs.alun.pedmcmc.AllelePrior;

public class GeneCounter
{
/** 
	Finds allele frequencies for the specified locus using Cedric Smith's gene counting
	method taking the whole pedigree structure into account.
	The estimates replace the existing values in the given genetic data
	struture.
	Retuns the standard errors of the estimates.
*/
	public static double[] geneCount(BasicGeneticData data, int i)
	{
		return geneCount(data,i,0.000001,1000);
	}

	public static double[] geneCount(BasicGeneticData d, int i, double max_diff, int max_its)
	{
		// Make the product of terms to peel for this locus.

		LocusProduct p = new LocusProduct(d,i);

		// Find the founder alleles.

		Inheritance[][] h = p.getInheritances();
		int nf = 0;
		for (int j=0; j<h.length; j++)
		{
			if (h[j][0] == null)
				nf++;
			if (h[j][1] == null)
				nf++;
		}

		Allele[][] al = p.getAlleles();
		Allele[] f = new Allele[nf];
		nf = 0;
		for (int j=0; j<h.length; j++)
		{
			if (h[j][0] == null)
				f[nf++] = al[j][0];
			if (h[j][1] == null)
				f[nf++] = al[j][1];
		}
		
		// Make the graphical model to peel the locus.

		GraphicalModel g = new GraphicalModel(p,false);
		g.reduceStates();
		
		// Track the locus alleles frequenceis.

		double[] freqs = new double[d.nAlleles(i)];
		double[] olderfreqs = new double[freqs.length];
		for (int k=0; k<olderfreqs.length; k++)
			olderfreqs[k] = 1.0 / olderfreqs.length;
		double[] oldfreqs = d.alleleFreqs(i);

		// Loop for gene counting.

		int its = 0;
		double  diff = 1;
		double lambda = 0;

		for ( ; its < max_its && diff > max_diff; its++)
		{
			g.marginalize();

			for (int k=0; k<freqs.length; k++)
				freqs[k] = 0;

			for (Allele a : f)
			{
				Table t = g.margin(a);
				for (a.init(); a.next(); )
				{ 
					freqs[a.getState()] += t.getValue();
				}
			}

			double tot = 0;
			for (int k=0; k<freqs.length; k++)
				tot += freqs[k];

			if (tot <= 0)
				throw new RuntimeException("Inconsistency in data. Cannot use gene counting. Locus: "+i);

			diff = 0;
			double ll = 0;
			int cc = 0;

			for (int k=0; k<freqs.length; k++)
			{
				freqs[k] /= tot;
				diff += Math.abs(oldfreqs[k] - freqs[k]);

				double dd = olderfreqs[k] - freqs[k];

				if (dd != 0)
				{
					dd = (oldfreqs[k] - freqs[k]) / dd;
					if (dd > 0 && dd < 1)
					{
						ll += dd;
						cc += 1;
					}
				}
			}

			if (cc > 0)
				lambda = ll/cc;

			for (int k=0; k<freqs.length; k++)
			{
				olderfreqs[k] = oldfreqs[k];
				oldfreqs[k] = freqs[k];
			}
		}

		if (its == max_its)
			System.err.println("Warning: maximum number of iterations exceeded in gene counting on locus "+i);

		for (int k=0; k<freqs.length; k++)
			freqs[k] = Math.sqrt( freqs[k] * (1-freqs[k]) / (1-lambda) / f.length );
			
		return freqs;
	}
}
