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
import jpsgcs.alun.linkage.Linkage;
import jpsgcs.alun.pedmcmc.LDModel;
import jpsgcs.alun.linkage.LinkageFormatter;
import jpsgcs.alun.pedapps.GeneDropper;
import jpsgcs.alun.pedapps.AlleleSharing;

/**
	This program performs multi locus gene drop simulations in order to assess the
	distribution of the length of  heterozygously shared genomic regions, as
	calculated by <a href="SGS.html"> SGS </a>.

<ul>
	Usage : <b> java SimSGS input.par(ld) input.ped [n] </b> </li>
</ul>
	where 
<ul>
<li> <b> input.par(ld) </b> is the input LINKAGE parameter file which may or may not have and LD model
        appended to it. If no LD model is explicitly specified, linkage equilibrium is assumed. </li>
<li> <b> input.ped </b> is the input LINKAGE pedigree file. </li>
<li> <b> n </b> is an optional parameter specifying the number of simulations to do.
	The default is 1000. </li>
</ul>

<p>
	The input for SimSGS should be exactly the same as that for SGS.
	
<p>
	The output is a table of maximum run lengths. There is one row for each
	simulation, and on each row are the maximum run length over the given markers
	for where the number of sharing individuals, S_i, is equal to n, the number of 
	probands, followed by the maximum run length where S_i >= n-1, n-2, and n-3.
*/

public class SimSGS extends AlleleSharing
{
	public static void main(String[] args)
	{
		try
		{
                        LinkageFormatter parin = null;
                        LinkageFormatter pedin = null;

                        int n_sims = 1000;

                        switch(args.length)
                        {
                        case 3: n_sims = new Integer(args[2]).intValue();

                        case 2: parin = new LinkageFormatter(args[0]);
                                pedin = new LinkageFormatter(args[1]);
                                break;

                        default:
                                System.err.println("Usage: java SimSGS input.par(ld) input.ped [n_sims]");
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

			int np = x.nProbands();

			for (int i=0; i<n_sims; i++)
			{
				g.geneDrop();
				int[] s = hetSharing(x);
	
				int m1 = max(runs(s,np));
				int m2 = max(runs(s,np-1));
				int m3 = max(runs(s,np-2));
				int m4 = max(runs(s,np-3));
		
				System.out.println("\t"+m1+" "+m2+" "+m3+" "+m4);
			}
		}
		catch (Exception e)
		{
			System.err.println("Caught in SimSGS:main().");
			e.printStackTrace();
		}
	}
}
