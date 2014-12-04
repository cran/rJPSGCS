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
        This program calculates two point lod scores on a grid of points.
<ul>
        Usage : <b> java TwoPointLods input.par input.ped </b> </li>
</ul>
        where
<ul>
<li> <b> input.par </b> is a LINKAGE parameter file </li>
<li> <b> input.ped </b> is a LINKAGE pedigree file </li>
</ul>
        The results are written to the standard output file as
        a bare table. There is a line of output for each marker
        containing the lod score for recombination fractions of
        <ul><b> 0.0, 0.01, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 0.99, 1.0 </b> </ul>
        respectively.
*/

public class TwoPointLods 
{
	public static void main(String[] args)
	{
		try
		{
			double[] thetas = {0.0, 0.01, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 0.99, 1.0};
			GeneticDataSource[] x = null;
			double log10 = Math.log(10);

			switch(args.length)
			{
			case 2: x = Linkage.readAndSplit(args[0],args[1]);
				break;
			default: 
				System.err.println("Usage: java TwoPointLods input.par input.ped");
				System.exit(1);
			}

			for (GeneticDataSource data : x)
			{
				data.downcodeAlleles();

				LocusProduct trait = new LocusProduct(data,0);

				for (int i=1; i<data.nLoci(); i++)
				{
					LocusProduct mark = new LocusProduct(data,i);
					TwoLocusProduct link = new TwoLocusProduct(trait,mark,0.5);
					GraphicalModel g = new GraphicalModel(link,false);
					
					double llhalf = g.logPeel();

					for (int k=0; k<thetas.length; k++)
					{
						link.fix(thetas[k]);
						double lod = ( g.logPeel() - llhalf ) / log10;
						System.out.printf("%8.4f ",lod);
					}
					System.out.println();
				}
				System.out.println();
			}
		}
		catch (Exception e)
		{
			System.err.println("Caught in TwoPointLods:main()");
			e.printStackTrace();
		}
	}
}
