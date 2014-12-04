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
import jpsgcs.alun.util.Curve;
import jpsgcs.alun.util.CartesianPoint;
import jpsgcs.alun.util.GoldenSection;

/**
	This program finds the maximum lod score for values of the 
	recombination between 0 and 0.5 and between 0.5 and 1.
<ul>
        Usage : <b> java MaxTwoPointLods input.par input.ped </b> </li>
</ul>
        where
<ul>
<li> <b> input.par </b> is a LINKAGE parameter file </li>
<li> <b> input.ped </b> is a LINKAGE pedigree file </li>
</ul>
<p>
	The method used is a golden section search that takes about 20
	function evaluations to get to within 0.001 of the maximizing value.
	The methods also assumes that the lod score is convex over
	(0,0.5) and over (0.5,1). This is not always true.
<p>
	The program maximizes the lod scores separately over the two intervals
	(0,0.5) and (0.5,1), because there may be a local maximum in each
	of these intervals. The output is a bare table of 4 columns
	giving the maximizing value and maximum in the region (0,0.5)
	followed by the maximizing value and maximum in the region (0.5,1).
	Values of the recombination parameter in the region (0.5,1) have
	no genetic interpretation, but may occur if the phase information
	is low and/or the model is misspecified.
	If you don't think you want values in the (0.5,1) region, just
	ignore the last two columns.
*/

public class MaxTwoPointLods 
{
	public static void main(String[] args)
	{
		try
		{
			GeneticDataSource[] x = null;
			double error = 0.001;

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
					LodScore l = new LodScore(link);

					CartesianPoint xy = GoldenSection.maximum(l,0,0.5,error);
					System.out.printf("%6.4f %6.4f\t\t",xy.x,xy.y);

					xy = GoldenSection.maximum(l,0.5,1.0,error);
					System.out.printf("%6.4f %6.4f\t\t",xy.x,xy.y);

                                        System.out.println();
				}

				System.out.println();
			}
		}
		catch (Exception e)
		{
			System.err.println("Caught in MaxTwoPointLods:main()");
			e.printStackTrace();
		}
	}
}

class LodScore implements Curve
{
	public LodScore(TwoLocusProduct l)
	{
		link = l;
		g = new GraphicalModel(link,false);
		link.fix(0.5);
		llhalf = g.logPeel();
	}

	public double f(double x)
	{
		link.fix(x);
		return (g.logPeel() - llhalf) / log10;
	}

	private GraphicalModel g = null;
	private TwoLocusProduct link = null;
	private double llhalf = 0;
	static private double log10 = Math.log(10);
}
