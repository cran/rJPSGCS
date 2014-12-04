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
import java.io.PrintStream;
import java.util.Vector;

/**
 	This program selects a subset of loci from LINKAGE parameter and pedigree
	files.

<ul>
        Usage : <b> java SelectLoci input.par input.ped output.par output.ped l1 [l2] ...</b> </li>
</ul>
        where
<ul>
<li> <b> input.par </b> is the original LINKAGE parameter file </li>
<li> <b> input.ped </b> is the original LINKAGE pedigree file </li>
<li> <b> output.par </b> is the file where the selected loci will be put.</li>
<li> <b> output.ped </b> is the file where the matching pedigree data will be put.</li>
<li> <b> l1 [l2] ... </b> is a list of at least one locus index. The indexes match the order of the 
	loci in the input parameter file. The first locus has index 0, the last of n has index n-1.</li>
</ul>

*/
	
public class SelectLoci
{
	public static void main(String[] args)
	{
		try
		{
			if (args.length < 5)
				System.err.println("Usage: java SelectLoci input.par input.ped output.par output.ped l1 [l2] ...");

			LinkageDataSet l = new LinkageDataSet(args[0],args[1]);
			PrintStream parout = new PrintStream(args[2]);
			PrintStream pedout = new PrintStream(args[3]);
		
			Vector<Integer> out = new Vector<Integer>();
			for (int i=4; i<args.length; i++)
			{
				if (args[i].contains("-"))
				{
					String[] s = args[i].split("-");
					int a = new Integer(s[0]).intValue();
					int b = 0;
					if (s.length == 2)
						b = new Integer(s[1]).intValue();
					else
						b = l.getParameterData().nLoci()-1;

					if (b > l.getParameterData().nLoci()-1)
						b = l.getParameterData().nLoci()-1;

					for (int k=a; k<=b; k++)
						out.add(new Integer(k));
				}
				else
					out.add(new Integer(args[i]));
			}

			int[] x = new int[out.size()];
			for (int i=0; i<x.length; i++)
				x[i] = out.get(i).intValue();

			l = new LinkageDataSet(l,x);
			l.getParameterData().writeTo(parout);
			l.getPedigreeData().writeTo(pedout);
		}
		catch (Exception e)
		{
			System.err.println("Caught in SelectLoci.main()");
			e.printStackTrace();
		}
	}
}
