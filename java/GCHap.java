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
import jpsgcs.alun.gchap.MultiLocusObservation;
import jpsgcs.alun.gchap.MarkerObservation;
import jpsgcs.alun.gchap.HapFormatter;
import jpsgcs.alun.genio.GeneticDataSource;
import jpsgcs.alun.linkage.Linkage;

/**
	This program finds maximum likelihood estimates of
	haplotype frequencies from a sample of genotyped individuals.
<ul>
	Usage : <b> java GCHap input.par input.ped </b>
</ul>
	where
<ul>
	<li> <b> input.par </b> is a LINKAGE parameter file </li>
	<li> <b> input.ped </b> is a LINKAGE pedigree file. Only the 
	genotype data from this file are used, the individuals are
	assumed to be unrelated. </li>
</ul>
	
	<p> 
	The program uses the gene counting method, or EM algorithm,
	to iterate between reconstructing the haplotypes and estimating
	haplotype frequencies. 

	<p>
	The results are written to the standard output file.
	There is a line for each haplotype with a positive frequency MLE,
	the frequency is given first in the line followed by the alleles
	of the haplotype.
	These are followed by two lines for each individual giving the 
	reconstructed haplotypes, one haplotype on each line.
*/
public class GCHap
{
	public static void main(String[] args)
	{
		try
		{
			GeneticDataSource gds = null;

			switch(args.length)
			{
			case 2: gds = Linkage.read(args[0],args[1]);
				break;
			default:
				System.err.println("Usage: java GCHap parfile pedfile");
				System.exit(1);
			}

			MarkerObservation[] x = new MarkerObservation[gds.nLoci()];
			for (int i=0; i<x.length; i++)
			{
				x[i] = new MarkerObservation(gds,i);
				System.err.println("Marker "+i+"  number of alleles = "+x[i].getTrait().getLocus().nAlleles());
			}

			Observation y = x[0];
			for (int i=1; i<x.length; i++)
			{
				System.err.print("Adding marker "+i);
				y = new MultiLocusObservation(y,x[i]);
				System.err.println(" number of haplotypes = "+y.getTrait().getLocus().nAlleles());
			}

			int its = y.geneCountToConvergence();
			y.getTrait().downCode();
			System.err.println("Log likelihood = "+y.logLikelihood()+" after "+its+" iterations");

			its = y.geneCountToConvergence();
			y.getTrait().downCode();
			System.err.println("Log likelihood = "+y.logLikelihood()+" after "+its+" iterations");

			System.out.println(HapFormatter.formatHaplotypes(y));
			System.out.println(HapFormatter.formatGuesses(y,gds));
		}
		catch (Exception e)
		{
			System.err.println("Caught in GCHap:main()");
			e.printStackTrace();
		}
	}
}
