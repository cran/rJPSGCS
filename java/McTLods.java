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
import jpsgcs.alun.pedapps.TLods;

/**
        This program samples the posterior distribution of inheritance vectors
        given the observed genotypes and, hence, calculates Tlod linkage
	statistics between a trait phenotype and genetic markers in pedigrees.
<ul>
        Usage : <b> java McTLods input.par input.ped [n] </b>
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
	The output gives a table for each pedigree. Each row of the table corresponds
	to a marker, each column to a value of the recombination fraction between the
	trait and the marker. Each entry is the Tlod, or fully informative two point
	lod score.
	The values of the recombination fraction used are (0.0, 0.01, 0.1, 0.2, 0.3, 0.4, 
	0.5, 0.6, 0.7, 0.8, 0.9, 0.99, 1).
*/

public class McTLods
{
	public static void main(String[] args)
	{
		try
		{
			boolean verbose = true;
			int n_samples = 1000;
			int sampler = 0;
			double[] thetas = {0.0, 0.01, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 0.99, 1};

			GeneticDataSource[] alldata = null;

			switch(args.length)
			{
			case 4: sampler = new Integer(args[3]).intValue();

			case 3: n_samples = new Integer(args[2]).intValue();

			case 2: alldata = Linkage.readAndSplit(args[0],args[1]);
				break;

			default: 
				System.err.println("Usage: java McTLods input.par input.ped [n_samples] [sampler]");
				System.exit(1);
			}

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

				TLods tlod = new TLods(data,mc.getInheritances(),thetas);

				if (verbose)
					System.err.println("Sampling");

				for (int s=0; s<n_samples; s++)
				{
					mc.sample();
					if (verbose) 
						System.err.print("'");
					tlod.update();
					if (verbose) 
						System.err.print(".");
				}

				if (verbose) 
					System.err.println();

				double[][] lods = tlod.results();


				for (int i=1; i<lods.length; i++)
				{
					for (int k=0; k<lods[i].length; k++)
						System.out.printf(" %5.4f",lods[i][k]);
					System.out.println();
				}

				System.out.println();
			}
		}
		catch (Exception e)
		{
			System.err.println("Caught in McTLods:main()");
			e.printStackTrace();
		}
	}
}
