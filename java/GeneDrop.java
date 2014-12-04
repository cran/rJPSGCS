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


import jpsgcs.alun.pedapps.GeneDropper;
import jpsgcs.alun.genio.GeneticDataSource;
import jpsgcs.alun.linkage.Linkage;
import jpsgcs.alun.linkage.LinkageFormatter;
import jpsgcs.alun.pedmcmc.LDModel;
import jpsgcs.alun.util.Main;

import java.io.PrintStream;

/**
	This program uses the multi locus gene drop method to simulate genotypes 
	on a pedigree to match those in the input. 

<ul>
	Usage : <b> java GeneDrop input.par(ld) input.ped geneDrop.out [ldmodelfile] [-a] > output.ped </b> </li>
</ul>
	where 
<ul>
<li> <b> input.par(ld) </b> is the input LINKAGE parameter file which may or may not have and LD model 
	appended to it. If no LD model is explicitly specified, linkage equilibrium is assumed.  </li>
<li> <b> input.ped </b> is the input LINKAGE pedigree file. </li>
<li> <b> -a </b> if this option is specified, simulated genotypes are output for all the
	individuals in the pedigree, not just the ones observed in the input.
<li> <b> output.ped </b> is the simulated output LINKAGE pedigree file. 
</ul>

<p>
	If an individual's 
	genotype at a certain marker is observed -- that is not zero -- in the specified input file,
	an observation will be simulated and specified in the output file.
	The recombination fractions specified in the input parameter file are used so
	that the simulations on the ith locus are simulated properly conditional on the i-1th.
<p>
	If a linkage disequilibrium model for the founder haplotypes is specfied this 
	is used and the alleles frequencies in the input parameter file are
	ignored. An LD  model file can be estimated from data using
	<a href="FitGMLD.html"> FitGMLD </a>.
*/

public class GeneDrop 
{
	public static void main(String[] args)
	{
		try
		{
			PrintStream out_stream = System.out;
			LinkageFormatter parin = null;
			LinkageFormatter pedin = null;

			boolean allout = false;

			String[] bargs = Main.strip(args,"-a");
			if (bargs != args)
			{
				allout = true;
				args = bargs;
			}

			switch(args.length)
			{
			case 3: out_stream = new PrintStream(args[2]);
			case 2: parin = new LinkageFormatter(args[0]);
				pedin = new LinkageFormatter(args[1]);
				break;

			default: System.err.println("Usage: java GeneDrop input.par(ld) input.ped geneDrop.out [-a]");
				System.exit(1);
			}
			
			GeneticDataSource data = Linkage.read(parin,pedin);
			LDModel ldmod = new LDModel(parin);
			if (ldmod.getLocusVariables() == null)
			{
				System.err.println("Warning: parameter file has no LD model appended.");
				System.err.println("Assuming linkage equilirbiurm and given allele frequencies.");
				ldmod = new LDModel(data);
			}

			GeneDropper g = new GeneDropper(data,ldmod,true);
			g.geneDrop(allout);
			data.writePedigree(out_stream);
			out_stream.close();
		}
		catch (Exception e)
		{
			System.err.println("Caught in GeneDrop:main()");
			e.printStackTrace();
		}
	}
}
