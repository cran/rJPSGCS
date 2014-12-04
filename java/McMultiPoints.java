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
import jpsgcs.alun.pedmcmc.LocusSampler;
import jpsgcs.alun.pedmcmc.LocusMeiosisSampler;
import jpsgcs.alun.pedmcmc.ExtendedLMSampler;
import jpsgcs.alun.pedapps.MultiPoints;
import jpsgcs.alun.pedapps.GeneticMap;

/**
        This program samples the posterior distribution of inheritance vectors
        given the observed genotypes and, hence, calculated multi point linkage
	lod scores between a trait phenotype and genetic markers in pedigrees.
<ul>
        Usage : <b> java McMultiPoints input.par input.ped [n] </b>
</ul>
<p>
        where
<ul>
        <li> <b> input.par </b> is the input LINKAGE parameter file. </li>
        <li> <b> input.ped </b> is the input LINAKGE pedigree file. </li>
        <li> <b> n </b> is an optional paramter specifiying the number of iterations
        to do. The default is 1000. </li>
</ul>
<p>
        Sampling is done using the Markov chain Monte Carlo method of
        blocked Gibbs updating.
<p>
	The output gives a table for each pedigree. The first column of each table
	is a genomic location in cMorgans with the first marker used as the origin.
	The second column gives the multi point lod score at that location for that pedigree.
*/

public class McMultiPoints
{
	public static void main(String[] args)
	{
		try
		{
			boolean verbose = true;
			int n_samples = 1000;
			int sampler = 0;

			double[] pos = null;

			GeneticDataSource[] alldata = null;

			switch(args.length)
			{
			case 4: sampler = new Integer(args[3]).intValue();

			case 3: n_samples = new Integer(args[2]).intValue();

			case 2: alldata = Linkage.readAndSplit(args[0],args[1]);
				break;

			default: 
				System.err.println("Usage: java McMultiPoints input.par input.ped [n_samples] [sampler]");
				System.exit(1);
			}

			pos = GeneticMap.locusCentiMorgans(alldata[0],false,5);

			for (GeneticDataSource data : alldata)
			{
				if (verbose)
					System.err.println("Setting up sampler");

				data.downcodeAlleles();

				LocusSampler mc = null;
		
				switch(sampler)
				{
				case 1: mc = new LocusMeiosisSampler(data,false);
					break;
				case 0:
				default:
					mc = new ExtendedLMSampler(data,false);
				}
		
				mc.initialize();

				MultiPoints mp = new MultiPoints(data,mc.getInheritances(),pos);

				if (verbose)
					System.err.println("Sampling");

				for (int s=0; s<n_samples; s++)
				{
					mc.sample();
					mp.update();
					if (verbose) 
						System.err.print(".");
				}

				if (verbose) 
					System.err.println();

				double[] lods = mp.results();

				for (int i=0; i<lods.length; i++)
					System.out.printf(" %8.4f\t%5.4f\n",pos[i],lods[i]);
			}
		}
		catch (Exception e)
		{
			System.err.println("Caught in McMultiPoints:main()");
			e.printStackTrace();
		}
	}
}
