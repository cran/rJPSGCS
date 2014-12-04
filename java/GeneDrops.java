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


import jpsgcs.alun.util.Main;
import jpsgcs.alun.linkage.Linkage;
import jpsgcs.alun.genio.GeneticDataSource;
import jpsgcs.alun.linkage.LinkageFormatter;
import jpsgcs.alun.pedmcmc.LDModel;
import jpsgcs.alun.pedapps.GeneDropper;
import java.io.PrintStream;
import java.util.zip.*;
import java.io.*;

/**
	This program uses the multi locus gene drop method to simulate genotypes 
	on a pedigree to match those in the input. 
<ul>
	Usage : <b> java GeneDrops input.par(ld) input.ped n filename [-a] </b> </li>
</ul>
	where 
<ul>
<li> <b> input.par(ld) </b> is the input LINKAGE parameter file which may or may not have and LD model
        appended to it. If no LD model is explicitly specified, linkage equilibrium is assumed. </li>
<li> <b> input.ped </b> is the input LINKAGE pedigree file. </li>
<li> <b> n </b> is the number of simulations required. </li>
<li> <b> filename </b> is the name of the output file. </li>
<li> <b> -a </b> if this option is specified, simulated genotypes are output for all the
	individuals in the pedigree, not just the ones observed in the input. </li>
<li> <b> -z </b> if this option is specified, write pedfile in a compressed form. </li>
</ul>

<p>
	That is, if an individual's 
	genotype at a certain marker is observed -- that is not zero -- in the specified input file,
	an observation will be simulated and specified in the output file.
	The recombination fractions specified in the input parameter file are used so
	that the simulations on the ith locus are simulated properly conditional on the i-1th.
<p>
	If a linkage disequilibrium model for the founder haplotypes is specfied this 
	is used and the alleles frequencies in the input parameter file are
	ignored. An LD  model file can be estimated from data using other programs
	such as <a href="HapGraph.html"> HapGraph </a> or <a href="IntevalLD.html"> IntervalLD </a>.

*/

public class GeneDrops
{
	public static void main(String[] args)
	{
		try
		{
			LinkageFormatter parin = null;
			LinkageFormatter pedin = null;
			
			boolean allout = false;
			boolean compress = false;
			String prefix = null;
			int n = 0;

			String[] bargs = Main.strip(args,"-a");
			if (bargs != args)
			{
				allout = true;
				args = bargs;
			}

			String[] cargs = Main.strip(args, "-z");
			if (cargs != args)
			{
				compress = true;
				args = cargs;
			}			

			switch(args.length)
			{
			case 4: parin = new LinkageFormatter(args[0]);
				pedin = new LinkageFormatter(args[1]);
				n = new Integer(args[2]).intValue();
				prefix = args[3];
				break;

			default: System.err.println("Usage: java GeneDrops input.par(ld) input.ped n prefix [-a]");
				System.exit(1);
			}

			GeneticDataSource x = Linkage.read(parin,pedin);

			LDModel ldmod = new LDModel(parin);
			if (ldmod.getLocusVariables() == null)
			{
                                System.err.println("Warning: parameter file has no LD model appended.");
                                System.err.println("Assuming linkage equilirbiurm and given allele frequencies.");
                                ldmod = new LDModel(x);
			}

			GeneDropper g = new GeneDropper(x,ldmod,true);
			int places = (int) (1.00000000001 + (Math.log10(n)));
if (!compress) {
			PrintStream ps = new PrintStream(prefix);
			for (int i=0; i<n; i++)
			{
				g.geneDrop(allout);
				x.writePedigree(ps);
				//x.writePedigree(new PrintStream(prefix+"."+format(i+1,places)));
			}
			ps.close();
}
else {
FileOutputStream fout = new FileOutputStream(prefix + ".zip");
GZIPOutputStream zout = new GZIPOutputStream(fout);
//ZipEntry ze = new ZipEntry(prefix);
//zout.putNextEntry(ze);
			for (int i=0; i<n; i++)
			{
				g.geneDrop(allout);
				x.writePedigree(zout);
			}
//zout.closeEntry();
			zout.close();
}

		}
		catch (Exception e)
		{
			System.err.println("Caught in GeneDrops:main()");
			e.printStackTrace();
		}
	}

	public static String format(int i, int p)
	{
		String r = ""+i;
		for (int j = (int)(1.000000000001 + Math.log10(i)); j<p; j++)
			r = "0"+r;
		return r;
	}
}
