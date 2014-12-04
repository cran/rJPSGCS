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


import jpsgcs.alun.gchap.Observation;
import jpsgcs.alun.gchap.MarkerObservation;
import jpsgcs.alun.gchap.MultiLocusObservation;
import jpsgcs.alun.gchap.HapFormatter;
import jpsgcs.alun.genio.GeneticDataSource;
import jpsgcs.alun.linkage.Linkage;

/**
	This program finds approximations to the maximum likelihood estimates of
	haplotype frequencies from a sample of genotyped individuals.
<ul>
	Usage : <b> java ApproxGCHap input.par input.ped [threshold] [max_iterations] </b>
</ul>
	where
<ul>
<li> <b> input.par </b> is a LINKAGE parameter file </li>
<li> <b> input.ped </b> is a LINKAGE pedigree file. Only the 
	genotype data from this file are used, the individuals are
	assumed to be unrelated.</li>
<li> <b> threshold </b> is a minimum value for a haplotype frequency. At each iteration 
	any haplotype with a lower estimated frequency will be eliminated to save time
	and space. The default value is 0. </li>
<li> <b> max_iterations </b> is an upper bound on the number of iteration performed at
	each stage of the algorithm. The default value is infinite, in which case
	the algorithm continues to convergence. </li>
	
	<p> 
	Like the program GCHap, the program uses the gene counting method, or EM algorithm,
	to iterate between reconstructing the haplotypes and estimating
	haplotype frequencies. However, to save time and space haplotypes with
	low frequency are eliminated at each stage.

	<p>
	The results are written to the standard output file.
	There is a line for each haplotype with a positive frequency MLE,
	the frequency is given first in the line followed by the alleles
	of the haplotype.
	These are followed by two lines for each individual giving the 
	reconstructed haplotypes, one haplotype on each line.
*/

public class ApproxGCHap 
{
	public static void main(String[] args)
	{
		try
		{
			double thresh = 0.0;
			int nits = 0;

			GeneticDataSource gds = null;

			switch(args.length)
			{
			case 4: nits = new Integer(args[3]).intValue();

			case 3: thresh = new Double(args[2]).doubleValue();

			case 2: gds = Linkage.read(args[0],args[1]);
				break;

			default:
				System.err.println("Usage: java ApproxGCHap parfile pedfile [threshold]");
				System.exit(1);
			}

			MarkerObservation[] x = new MarkerObservation[gds.nLoci()];
			for (int i=0; i<x.length; i++)
				x[i] = new MarkerObservation(gds,i);

			Observation y = x[0];
			for (int i=1; i<x.length; i++)
			{
				System.err.print("Adding locus "+i+"  "+x[i].getTrait().getLocus().nAlleles());
				y = new MultiLocusObservation(y,x[i]);
				System.err.print("  Number of haplotypes = "+y.getTrait().getLocus().nAlleles());
				if (nits == 0)
					y.geneCountToConvergence();
				else
					y.getTrait().geneCount(nits);

				y.getTrait().downCode(thresh);
				System.err.print(" downcoded to = "+y.getTrait().getLocus().nAlleles());
				y.getTrait().parsDownCode();
				System.err.println(" then to = "+y.getTrait().getLocus().nAlleles());
			}

			y.geneCountToConvergence();
			y.getTrait().downCode(thresh);

			System.out.println(HapFormatter.formatHaplotypes(y));
			System.out.println(HapFormatter.formatGuesses(y,gds));
		}
		catch (Exception e)
		{
			System.err.println("Caught in ApproxGCHap:main()");
			e.printStackTrace();
		}
	}
}
