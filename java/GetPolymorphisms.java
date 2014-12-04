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
import jpsgcs.alun.genio.GeneticDataSource;
import jpsgcs.alun.linkage.LinkageInterface;

import java.io.PrintStream;
import java.util.Vector;

/**
	This program selects from the input data the marker loci for which at
	least two alleles are observed. 

<ul>
	Usage : <b> java GetPolymorphisms input.par input.ped [output.par] [output.ped] </b> </li>
</ul>
	where 
<ul>
<li> <b> input.par </b> is the input LINKAGE parameter file. </li>
<li> <b> input.ped </b> is the input LINKAGE pedigree file. </li>
<li> <b> output.par </b> is the output LINKAGE parameter file. 
	The default is to write to standard output.</li>
<li> <b> output.ped </b> is the output LINKAGE pedigree file. 
	The default is to write to standard output.</li>
</ul>

*/

public class GetPolymorphisms
{
	public static void main(String[] args)
	{
		try
		{
			boolean harddowncode = true;

			LinkageDataSet ld = null;
			PrintStream parout = System.out;
			PrintStream pedout = System.out;

			switch(args.length)
			{
			case 4: pedout = new PrintStream(args[3]);
			case 3: parout = new PrintStream(args[2]);
			case 2: ld = new LinkageDataSet(args[0],args[1]);
				break;
			default:
				System.err.println("Usage: java GetPolymorphisms input.par input.ped [output.par] [output.ped}");
				System.exit(0);
			}

			ld.downCode(harddowncode);
			ld.countAlleleFreqs();
			GeneticDataSource d = new LinkageInterface(ld);

			Vector<Integer> out = new Vector<Integer>();
			for (int i=0; i<d.nLoci(); i++)
				if (d.nAlleles(i) > 1)
					out.add(new Integer(i));

			int[] x = new int[out.size()];
			for (int i=0; i<out.size(); i++)
				x[i] = out.get(i).intValue();
			
			ld = new LinkageDataSet(ld,x);

			ld.getParameterData().writeTo(parout);
			ld.getPedigreeData().writeTo(pedout);
		}
		catch (Exception e)
		{
			System.err.println("Caught in GetPolymorphisms:main()");
			e.printStackTrace();
		}
	}
}
