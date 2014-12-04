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


import jpsgcs.alun.linkage.LinkageDataSet;
import jpsgcs.alun.util.Main;

import java.io.PrintStream;

/**
	This program reduces the number of alleles in the models for
	the given genetic loci to include only alleles that appear in 
	the pedigree file plus, optionally, one catchall allele to represent the
	unseen alleles which has
	frequency equal to the total frequency of the original unseen alleles.
<ul>
	Usage : <b> java DownCodeAlleles input.par input.ped [output.par] [output.ped] [-h/s] </b> </li>
</ul>
	where
<ul>
<li> <b> input.par </b> is the original LINKAGE parameter file </li>
<li> <b> input.ped </b> is the original LINKAGE pedigree file </li>
<li> <b> output.par </b> is the file where the new down coded parameters will be put.
	The default is to write to standard output.</li>
<li> <b> output.ped </b> is the file where the new down coded pedigree data will be put.
	The default is to write to standard output.</li>
<li> <b> -h/s </b> these are options to specify the down coding strategy. 
	<ul>
		<li> <b> -h </b> this is the default action which is a "hard" down coding
			where only the alleles that appear in the pedigree are kept. </li>
		<li> <b> -s </b> this is a "soft" down coding in which all the alleles that
			appear in the pedigree are kept, along with one catchall allele to
			represent the unobserved alleles. </li>
	</ul>
</li>
</ul>

<p>
	A description of the down coding made is also printed: one line for
	each locus with the new code number for each of the old alleles in order.
	A zero indicates that the allele is missing and 
	not included in the downcoded model.

*/

public class DownCodeAlleles
{
	public static void main(String[] args)
	{
		try
		{
			int harddowncode = 2;

			LinkageDataSet ld = null;
			PrintStream parout = System.out;
			PrintStream pedout = System.out;

			String[] bargs = Main.strip(args,"-h");
			if (bargs != args)
			{
				harddowncode = 2;
				args = bargs;
			}
	
			bargs = Main.strip(args,"-s");
			if (bargs != args)
			{
				harddowncode = 0;
				args = bargs;
			}
			
			bargs = Main.strip(args,"-s1");
			if (bargs != args)
			{
				harddowncode = 1;
				args = bargs;
			}
			
			switch(args.length)
			{
			case 4: pedout = new PrintStream(args[3]);
			case 3: parout = new PrintStream(args[2]);
			case 2: ld = new LinkageDataSet(args[0],args[1]);
				break;
			default:
				System.err.println("Usage: java DownCodeAlleles input.par input.ped [output.par] [output.ped] [-h/s]");
				System.exit(0);
			}

			for (int i=0; i<ld.getParameterData().nLoci(); i++)
			{
				int[] c = ld.downCode(i,harddowncode);
				for (int j=1; j<c.length; j++)
					System.out.print(" "+c[j]);
				System.out.println();
			}

			ld.getParameterData().writeTo(parout);
			parout.flush();
			ld.getPedigreeData().writeTo(pedout);
			pedout.flush();

			parout.close();
			pedout.close();
		}
		catch (Exception e)
		{
			System.err.println("Caught in DownCodeAlleles:main()");
			e.printStackTrace();
		}
	}
}
