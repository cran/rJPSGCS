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
import jpsgcs.alun.pedapps.Kinship;

/**
	This program samples the posterior distribution of inheritance vectors
	given the observed genotypes and, hence, estimates postrior probabilities
	of kinship. 
<ul>
	Usage : <b> java McKinship input.par input.ped [n] </b>
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
	The output gives a matrix of posterior kinship coefficients for each pedigree
	in the input file.
*/

public class McKinship
{
	public static void main(String[] args)
	{
		try
		{
			boolean verbose = true;

			int n_samples = 1000;
			double error = 0.01;
			int maxerral = 4;
			int sampler = 0;

			GeneticDataSource[] alldata = null;

			switch(args.length)
			{
			case 4: sampler = new Integer(args[3]).intValue();

			case 3: n_samples = new Integer(args[2]).intValue();

			case 2: alldata = Linkage.readAndSplit(args[0],args[1]);
				break;

			default: 
				System.err.println("Usage: java McKinship input.par input.ped [n_samples] [sampler]");
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
				case 1: mc = new LocusMeiosisSampler(data,true,error,maxerral);
					break;
				case 0:
				default:
					mc = new ExtendedLMSampler(data,true,error,maxerral);
				}

				mc.initialize();

				Kinship kin = new Kinship(data,mc.getInheritances());

				if (verbose)
					System.err.println("Sampling");

				for (int s=1; s<=n_samples; s++)
				{
					mc.sample();
					kin.update();
					if (verbose) 
					{
						if (s % 100 == 0)
							System.err.print(".");
						if (s % 500 == 0)
							System.err.print(" ");
					}
				}

				if (verbose) 
					System.err.println();

				for (int j=0; j<kin.nPositions(); j++)
				{
					double[][] k = kin.kinships(j);
					for (int a=0; a<k.length; a++)
					{
						System.out.print(data.individualName(a)+" :\t");
						for (int b=0; b<k[a].length; b++)
							System.out.print(k[a][b]+"\t");
						System.out.println();
					}
					System.out.println();
				}
			}
		}
		catch (Exception e)
		{
			System.err.println("Caught in McKinship:main()");
			e.printStackTrace();
		}
	}
}
