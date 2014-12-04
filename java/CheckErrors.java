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


import jpsgcs.alun.genio.GeneticDataSource;
import jpsgcs.alun.pedmcmc.Error;
import jpsgcs.alun.pedmcmc.ErrorLocusProduct;
import jpsgcs.alun.linkage.Linkage;
import jpsgcs.alun.markov.GraphicalModel;
import jpsgcs.alun.markov.Function;

/**
	This program finds the posterior probabilities of errors for 
	genotypes on individuals in pedigrees given a set of observed genotypes
	and the pedigree structure.
<ul>
Usage : <b> java CheckErrors input.par input.ped [thresh1] [thresh2] [prior]</b> 
</ul>
	where
<ul>
<li> <b> input.par </b> is a LINKAGE parameter file </li>
<li> <b> input.ped </b> is a LINKAGE pedigree file </li>
<li> <b> thresh1 </b> specifies the minimum value for the overall probability that there
	is at least one genotyping error at a particular locus above which errors are reported. The default is 50%. </li>
<li> <b> thresh2 </b> specifies the minimum value for reporting individual error probabilities, given that the 
	overall error probability exceeds <b> thresh1 </b>. The default is 25%. </li> 
<li> <b> prior </b> is the prior probability that any genotyping call is in error. The default is 1%.</li>
</ul>

<p>
	This analyzes the data in the input files, locus by locus and kindred by kindred.
	It computes the posterior probability that each genotype call is in error using 
	a standard graphical modeling forward-backward algorithm.
	It replaces the program <a href="GMCheck.html"> GMCheck </a>. 
<p>
	For large or complex pedigrees, or large (> 15) numbers of alleles per locus
	use the <b> -Xms </b> and <b> -Xmx </b> options to increase the space available to the java program.
<p>
	The method is described in 
	<a href="http://bioinformatics.oxfordjournals.org/cgi/reprint/bti485?ijkey=KcqzKdbCHLJPeV2&keytype=ref">
	Thomas (2005), Bioinformatics.
	</a>
*/

public class CheckErrors
{
	public static void main(String[] args)
	{
		try
		{
			GeneticDataSource[] x = null;
			double overallthresh = 0.5;
			double margthresh = 0.25;
			double prior = 0.01;
			int[] zero = {0};
			int[] zeroone = {0, 1};
			int[] one = {1};

			switch(args.length)
			{
			case 5: prior = new Double(args[4]).doubleValue();

			case 4: margthresh = new Double(args[3]).doubleValue();

			case 3: overallthresh = new Double(args[2]).doubleValue();

			case 2: x = Linkage.readAndSplit(args[0],args[1]);
				break;

			default:
				System.err.println("Usage: java CheckErrors parfile pedfile [main_threshold] [marginal_threshold] [error_prior]");
				System.exit(1);
			}

			for (GeneticDataSource d : x)
			{
				d.downcodeAlleles();

				for (int i=0; i<d.nLoci(); i++)
				{
					try
					{
						ErrorLocusProduct p = new ErrorLocusProduct(d,i,prior);
						Error[] erro = p.getErrors();
						for (Error e : erro)
							if (e != null)
								e.setStates(zero);
						GraphicalModel g = new GraphicalModel(p,false);
						double pnoerror = g.peel();
						boolean oblig = !(pnoerror > 0);

						for (Error e : erro)
							if (e != null)
								e.setStates(zeroone);
						g = new GraphicalModel(p,false);
						pnoerror /= g.peel();
						g.marginalize();

						if (pnoerror < overallthresh)
						{
							System.out.printf("Pedigree %s locus #%d %s:\n",d.pedigreeName(0),i,d.locusName(i));
							if (oblig)
								System.out.println("\t Obligatory error.");
							else
								System.out.printf("\t Probability of at least one error is %6.4f\n",(1-pnoerror));

							System.out.println("\t Individuals with high posterior error probability:");
	
							for (int k=0; k<erro.length; k++)
							{
								if (erro[k] != null)
								{
									Function f = g.margin(erro[k]);
									erro[k].setState(1);
									if (f.getValue() > margthresh)
										System.out.printf("\t\t %s with probability %6.4f\n",d.individualName(k),f.getValue());
								}
							}

							System.out.println();
						}
						else
						{
							System.out.printf("Pedigree %s locus #%d %s:",d.pedigreeName(0),i,d.locusName(i));
							System.out.printf(" probability of no error is %6.4f\n",pnoerror);
						}

					}
					catch (OutOfMemoryError e)
					{
						System.out.println("Pedigree (#"+(1+i)+") "+d.pedigreeName(0)+"\tlocus (#"+(1+i)+") "+d.locusName(i));
						System.out.println("Cannot allocate memory for computation");
					}
				}
			}
		}
		catch (Exception e)
		{
			System.err.println("Caught in CheckErrors:main().");
			e.printStackTrace();
		}
	}
}
