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
import jpsgcs.alun.pedmcmc.LocusProduct;
import jpsgcs.alun.markov.GraphicalModel;

/**
	This program checks genotype data on pedigrees and indicates whether
	the data at any particular locus is inconsistent with Mendelian 
	inheritance.

<ul>
        Usage : <b> java ObligateErrros input.par input.ped </b> </li>
</ul>
        where
<ul>
<li> <b> input.par </b> is a LINKAGE parameter file </li>
<li> <b> input.ped </b> is a LINKAGE pedigree file </li>
</ul>

<p>
	The program works through each pedigree and locus in turn and writes
	a list of errors to the standard output file.
<p>
	A more complete analysis which finds the posterior error probabilities for
	each genotype call, and hence makes finding the source of error easier, is done by 
	<a href="CheckErrors.html"> CheckErrors </a>.
*/

public class ObligateErrors
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
				System.err.println("Usage: java ObligateErrors input.par input.ped");
				System.exit(1);
			}


			for (int k=0; k<x.length; k++)
			{
				x[k].downcodeAlleles();

				for (int i=0; i<x[k].nLoci(); i++)
				{
					LocusProduct p = new LocusProduct(x[k],i);
					GraphicalModel g = new GraphicalModel(p,false);
					double prob = g.peel();
					if (!(g.peel() > 0))
						System.out.println("Pedigree "+(1+k)+" " +"\tlocus "+(1+i)+" "+x[k].locusName(i)+"\tobligatory error");
				}

				System.err.println();
			}
		}
		catch (Exception e)
		{
			System.err.println("Caught in ObligateErrors:main().");
			e.printStackTrace();
		}
	}
}
