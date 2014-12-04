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


import jpsgcs.alun.pedmcmc.LocusProduct;
import jpsgcs.alun.pedmcmc.TwoLocusProduct;
import jpsgcs.alun.markov.GraphicalModel;
import jpsgcs.alun.linkage.Linkage;
import jpsgcs.alun.genio.GeneticDataSource;

/**
        This program calculates two point hetlod scores on a grid of points.

<ul>
        Usage : <b> java HetLods input.par input.ped </b> </li>
</ul>
        where
<ul>
<li> <b> input.par </b> is a LINKAGE parameter file </li>
<li> <b> input.ped </b> is a LINKAGE pedigree file </li>
</ul>
<p>
        The values of theta used are {0.0, 0.01, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 0.99, 1.0}.
<p>
        The values of alpha used are {0.0, 0.01, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 0.99, 1.0}.
<p>
        The output is a matrix of hetlods for each marker. Theta varies across the columns
        and alpha varies down the rows of each matrix. Each matrix is separated by
        a blank line.
*/

public class HetLods 
{
	public static void main(String[] args)
	{
		try
		{
			double[] thetas = {0.0, 0.01, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 0.99, 1.0};
			double[] alphas = {0.0, 0.01, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 0.99, 1.0};

			GeneticDataSource[] x = null;
			double log10 = Math.log(10);

			switch(args.length)
			{
			case 2: x = Linkage.readAndSplit(args[0],args[1]);
				break;
			default: 
				System.err.println("Usage: java HetLods input.par input.ped");
				System.exit(1);
			}

			for (GeneticDataSource data : x)
				data.downcodeAlleles();

			for (int i=1; i<x[0].nLoci(); i++)
			{
				double[][] h = new double[alphas.length][thetas.length];

				for (GeneticDataSource data : x)
				{
					LocusProduct trait = new LocusProduct(data,0);
					LocusProduct mark = new LocusProduct(data,i);
					TwoLocusProduct link = new TwoLocusProduct(trait,mark,0.5);
					GraphicalModel g = new GraphicalModel(link,false);
					
					double llhalf = g.logPeel();

					double[] lod = new double[thetas.length];
					for (int k=0; k<thetas.length; k++)
					{
						link.fix(thetas[k]);
						lod[k] = ( g.logPeel() - llhalf ) / log10;
					}

					for (int j=0; j<h.length; j++)
						for (int k=0; k<h[j].length; k++)
							h[j][k] += Math.log10(alphas[j] * Math.pow(10,lod[k]) + (1-alphas[j]));
				}

				for (int j=0;  j<h.length; j++)
				{
					for (int k=0; k<h[j].length; k++)
						System.out.printf("%8.4f ",h[j][k]);
					System.out.println();
				}
				System.out.println();
			}
		}
		catch (Exception e)
		{
			System.err.println("Caught in HetLods:main()");
			e.printStackTrace();
		}
	}
}
