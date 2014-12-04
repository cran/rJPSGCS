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



import jpsgcs.alun.linkage.Linkage;
import jpsgcs.alun.genio.GeneticDataSource;
import jpsgcs.alun.markov.GraphicalModel;
import jpsgcs.alun.pedmcmc.LocusProduct;

/**
	This program calculates the log likelihood for the data for each pedigree at 
	each locus specified in the input.
<ul>
	Usage : <b> java LocusLogLikelihoods input.par input.ped </b> </li>
</ul>
	where 
<ul>
<li> <b> input.par </b> is the input LINKAGE parameter file. </li>
<li> <b> input.ped </b> is the input LINKAGE pedigree file. </li>
</ul>

<p>
	The output is a matrix with row for each kindred in the pedigree file. On each
	row is the log likelihood for the data at each locus.

*/

public class LocusLogLikelihoods
{
	public static void main(String[] args)
	{
		try
		{
			GeneticDataSource[] x = null;

			switch(args.length)
			{
			case 2: x = Linkage.readAndSplit(args[0],args[1]);
				break;

			default: 
				System.err.println("Usage: java LocusLogLikelihoods input.par input.ped");
				System.exit(1);
			}

			for (GeneticDataSource data : x)
			{
				data.downcodeAlleles();

				for (int i=0; i<data.nLoci(); i++)
				{
					LocusProduct p = new LocusProduct(data,i);
					GraphicalModel g = new GraphicalModel(p,false);
					System.out.printf("%6.4f ",g.logPeel());
				}
				System.out.println();
			}
		}
		catch (Exception e)
		{
			System.err.println("Caught in LocusLogLikelihoods:main()");
			e.printStackTrace();
		}
	}
}
