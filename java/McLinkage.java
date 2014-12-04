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
import jpsgcs.alun.pedapps.TLods;
import jpsgcs.alun.pedapps.GeneticMap;

/**
	The programs combines 
	<a href="McMultiPoints.html"> McMultiPoints </a>
	and
	<a href="McTLods.html"> McTLods </a>
	to give both true multi point lod scores and Tlod statistics.

<ul>
        Usage : <b> java McLinkage input.par input.ped [n] </b>
</ul>
<p>
        where
<ul>
        <li> <b> input.par </b> is the input LINKAGE parameter file. </li>
        <li> <b> input.ped </b> is the input LINAKGE pedigree file. </li>
        <li> <b> n </b> is an optional paramter specifiying the number of iterations
        to do. The default is 1000. </li>
</ul>
*/

public class McLinkage
{
	public static void main(String[] args)
	{
		try
		{
			boolean verbose = true;
			int n_samples = 1000;
			int sampler = 0;
			
			boolean dotlods = true;
			boolean domulti = true;
			double[] thetas = {0, 0.01, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 0.99, 1};
			double[] pos = null;

			MultiPoints mp = null;
			TLods tl = null;

			GeneticDataSource[] alldata = null;

			switch(args.length)
			{
			case 4: sampler = new Integer(args[3]).intValue();

			case 3: n_samples = new Integer(args[2]).intValue();

			case 2: alldata = Linkage.readAndSplit(args[0],args[1]);
				break;

			default: 
				System.err.println("Usage: java McLinkage input.par input.ped [n_samples] [sampler]");
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
				
				if (domulti)
					mp = new MultiPoints(data,mc.getInheritances(),pos);

				if (dotlods)
					tl = new TLods(data,mc.getInheritances(),thetas);

				if (verbose)
					System.err.println("Sampling");

				for (int s=0; s<n_samples; s++)
				{
					mc.sample();
					
					if (domulti)
						mp.update();
					if (dotlods)
						tl.update();
					if (verbose) 
						System.err.print(".");
				}

				if (verbose) 
					System.err.println();

				if (domulti)
				{
					double[] lods = mp.results();

					for (int i=0; i<lods.length; i++)
						System.out.printf(" %8.4f\t%5.4f\n",pos[i],lods[i]);
					System.out.println();
				}

				if (dotlods)
				{
					double[][] tlods = tl.results();
				
					for (int i=1; i<tlods.length; i++)
					{
						for (int j=0; j<tlods[i].length; j++)
							System.out.printf(" %8.4f",tlods[i][j]);
						System.out.println();
					}
				}

				System.out.println();
			}
		}
		catch (Exception e)
		{
			System.err.println("Caught in McLinkage:main()");
			e.printStackTrace();
		}
	}
}
