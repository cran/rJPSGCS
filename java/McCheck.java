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
import jpsgcs.alun.pedmcmc.ExtendedLMSampler;
import jpsgcs.alun.pedmcmc.LocusMeiosisSampler;
import jpsgcs.alun.pedapps.ErrorCount;

/**
	This program uses Markov chain Monte Carlo methods for fully
	informative multi locus error detection for linked loci
	in a pedigree.

<ul>
        Usage : <b> java McCheck input.par input.ped [n_samples] [error_prior] </b> </li>
</ul>
        where
<ul>
<li> <b> input.par </b> is the input LINKAGE parameter file </li>
<li> <b> input.ped </b> is the input LINKAGE pedigree file </li>
<li> <b> n_samples </b> specifies the number of samples to use. The default is 1000. </li>
<li> <b> eroror_prior </b> specifies the prior probability of a genotyping error. 
	The default is 1%. </li>
</ul>

<p>
	The output is a matrix of probailties, one row per individual, one
	column per locus, specifying the posterior probability that each genotype
	observation is in error.
	
*/

public class McCheck
{
	public static void main(String[] args)
	{
		try
		{
			int n_samples = 1000;
			double error_prior = 0.01;
			int maxerral = 4;
			int sampler = 0;

			GeneticDataSource[] x = null;

			switch(args.length)
			{
			case 4: sampler = new Integer(args[3]).intValue();

			case 3: n_samples = new Integer(args[2]).intValue();

			case 2: x = Linkage.readAndSplit(args[0],args[1]);
				break;

			default: System.err.println("Usage: java McCheck input.par input.ped [n_samples] [sampler]");
				System.exit(1);
			}


			for (GeneticDataSource data : x)
			{
				data.downcodeAlleles();
		
				LocusSampler mc = null;
				switch(sampler)
				{
				case 1: mc = new LocusMeiosisSampler(data,false,error_prior,maxerral);
					break;
				case 0:
				default:
					mc = new ExtendedLMSampler(data,false,error_prior,maxerral);
				}

				mc.initialize();

				ErrorCount err = new ErrorCount(mc.getErrors());

				for (int s=1; s<=n_samples; s++)
				{
					mc.sample();
					err.update();
					System.err.print(".");
				}

				System.err.println();

				double[][] res = err.results();
				for (int i=0; i<res.length; i++)
				{
					for (int j=0; j<res[i].length; j++)
						System.out.printf("%6.4f ",res[i][j]);
					System.out.println();
				}
				System.out.println();
			}
		}
		catch (Exception e)
		{
			System.err.println("Caught in McCheck:main()");
			e.printStackTrace();
		}
	}
}
