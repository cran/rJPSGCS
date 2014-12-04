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


import jpsgcs.alun.pedapps.AlleleSharing;
import jpsgcs.alun.genio.GeneticDataSource;
import jpsgcs.alun.linkage.Linkage;
import jpsgcs.alun.util.Main;


/**
	This program calculates the runs of heterozygous sharing for a set of
	individuals in a pedigree. 

<ul>
	Usage : <b> java SGS input.par input.ped [-r] </b> </li>
</ul>
	where 
<ul>
<li> <b> input.par </b> is the input LINKAGE parameter file. </li>
<li> <b> input.ped </b> is the input LINKAGE pedigree file. </li>
<li> <b> -r </b> if this optional parameter is specified the lengths of
        the runs where H_i = n are output, as required to estimate the
        distribution of run lengths </li>
</ul>

<p> The individuals to consider are specified
	by having a 1 in the proband field of the input LINKAGE pedigree file.
	The <a href="MakeProbands.html"> MakeProbands </a> program can be
	used to set this field.

<p>
	The output is a table with 1 line for each locus in the input LINKAGE
	parameter file. On each line appear

	<ul>
		<li> The name of the locus </li>
		<li> The largest number of individuals at that locus that can share one alleles.
			We denote this by S_i. </li>
		<li> The longest run containing the locus for which S_i = n, where n is the number
		of probands. </li>
		<li> The longest run containing the locus for which S_i >= n-1. </li>
		<li> The longest run containing the locus for which S_i >= n-2. </li>
		<li> The longest run containing the locus for which S_i >= n-3. </li>
	</ul>
<p>
	Note that this format choice means that a run of length r will appear r times in
	the output: once for each locus in the run. This should be taken into account
	in any estimates of the run length distribution.
        Alternatively use the <b> -r </b> option below to list each run only once.


<p>
	The largest runs for which S_i = n, S_i >= n-1, etc seen across the whole data set
	is written to the screen.
*/

public class SGS extends AlleleSharing
{
	public static void main(String[] args)
	{
		try
		{
			GeneticDataSource x = null;
			boolean runs = false;

			String[] bargs = Main.strip(args,"-r");
			if (bargs != args)
			{
				runs = true;
				args = bargs;
			}

			switch(args.length)
			{
			case 2: x = Linkage.read(args[0],args[1]);
				break;
			default:
				System.err.println("Usage: java SGS input.par input.ped [-r]");
				System.exit(1);
			}

			int[] s = hetSharing(x);
			int np = x.nProbands();
			int[] r = runs(s,np);

			if (runs)
			{
				for (int i=0; i<r.length; )
				{
					System.out.println(r[i]);
					if (r[i] == 0)
						i += 1;
					else
						i += r[i];
				}
			}
			else
			{
				int[] t = runs(s,np-1);
				int[] u = runs(s,np-2);
				int[] v = runs(s,np-3);
	
				for (int i=0; i<s.length; i++)
					System.out.println(x.locusName(i)+"\t"+s[i]+"\t"+r[i]+"\t"+t[i]+"\t"+u[i]+"\t"+v[i]);

				System.err.println("\t"+max(r)+"\t"+max(t)+"\t"+max(u)+"\t"+max(v));
			}

		}
		catch (Exception e)
		{
			System.err.println("Caught in SGS:main().");
			e.printStackTrace();
		}
	}
}
