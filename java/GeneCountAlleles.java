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
import jpsgcs.alun.pedapps.GeneCounter;
import jpsgcs.alun.util.Main;

/**
	This program calculates maximum likelihood estimates of allele frequencies
	from genotypes observed in individuals related in pedigrees.

<ul>
        Usage : <b> java GeneCountAlleles input.par input.ped [conv_thresh] [-a] </b> </li>
</ul>
        where
<ul>
<li> <b> input.par </b> is the original LINKAGE parameter file. </li>
<li> <b> input.ped </b> is the original LINKAGE pedigree file. </li>
<li> <b> conv_thresh </b> is an optional parameter specifiying the largest difference
	between allele frequencies in successive iterations that is acceptable before
	convergence is assumed. The default value is 0.000001.</li>
<li> if <b> -a </b> is specified the frequencies for all loci are estimated (see below).</li>
</ul>

<p>
	The output is a linkage parameter file that is 
	the same as the old one, except that the original allele
	frequencies will be replaced by the new maximum likelihood estimates.
        This is writen to standard output.

<p>
	This program is an extension of C A B Smith's gene counting method or EM
	algorithm. It works
	by iteratively inferring the genotypes of the founders in a pedigree given
	the current alleles frequency estimates, the pedigree structure and any
	genotypes observed in the pedigree (the E step). From the posterior
	distribution of the founder alleles frequencies new maximum likelihood
	estimates for the allele frequencies are derived (the M step).
<p>
	The program also uses C A B Smith's trick of using the rate of convergence
	of the algorithm to estimate the curvature of the likelihood, and hence
	get an inflation factor for the variance of the estimators. As the program
	works through each locus it outputs to the screen the initial estimates, 
	the final estimates,
	and the standard errors of the estimates incorporating the inflation factor.
<p>
	In a typical use of this program the first marker in the input files is often
	a phenotype, not a marker, hence, because pedigrees are usually
	ascertained in ways that bias the estimation of alleles at trait loci 
	the default is not to estimate these frequencies but only those 
	for subsequent loci. 
	To force estimation at all loci, specify the <b> -a </b> option on the command line.
*/

public class GeneCountAlleles extends GeneCounter
{
	public static void main(String[] args)
	{
		try
		{
			GeneticDataSource x = null;

			boolean report = true;
			double max_diff = 0.000001;
			int max_its = 100;
			boolean all = false;

			String[] bargs = Main.strip(args,"-a");
			if (bargs != args)
			{
				all = true;
				args = bargs;
			}
			
			switch(args.length)
			{
			case 3: max_diff = new Double(args[2]).doubleValue();

			case 2: x = Linkage.read(args[0],args[1]);
				break;

			default:
				System.err.println("Usage: java GeneCountAlleles input.par input.ped [conv_thresh] [-a]");
				System.exit(1);
			}


			for (int i = (all ? 0 : 1); i<x.nLoci(); i++)
			{
				double[] f = x.alleleFreqs(i);
				if (report)
				{
					System.err.println(x.locusName(i));
					System.err.print("Initial\t");
					for (int k=0; k<f.length; k++)
						System.err.printf("%2.5f ",f[k]);
					System.err.println();
				}

				double[] s = geneCount(x,i,max_diff,max_its);

				if (report)
				{
					System.err.print("Final\t");
					for (int k=0; k<f.length; k++)
						System.err.printf("%2.5f ",f[k]);
					System.err.println();

					System.err.print("Stderr\t");
					for (int k=0; k<s.length; k++)
						System.err.printf("%2.5f ",s[k]);
					System.err.println();
				}
			}

			x.writeParameters(System.out);
		}
		catch (Exception e)
		{
			System.err.println("Caught in EstimateAlleleFreqs:main().");
			e.printStackTrace();
		}
	}
}
