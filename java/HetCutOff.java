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
	This program selects from the input data the marker loci for which the
	heterozygosity score is higher than a given threshold.
<ul>
	Usage : <b> java HetCutOff input.par input.ped [output.par] [output.ped] [thresh] </b> </li>
</ul>
	where 
<ul>
<li> <b> input.par </b> is the input LINKAGE parameter file. </li>
<li> <b> input.ped </b> is the input LINKAGE pedigree file. </li>
<li> <b> output.par </b> is the output LINKAGE parameter file. 
	The default is to write to standard output.</li>
<li> <b> output.ped </b> is the output LINKAGE pedigree file. 
	The default is to write to standard output.</li>
<li> <b> thresh </b> is the minimum hetrozygosity required. 
	The defaule value is 0.1.</li>
</ul>

<p>
	The heterozygosity
	score is 1 minus the sum of the squared allele frequencies. This value
	is between 0 and 0.5.

*/

public class HetCutOff
{
	public static void main(String[] args)
	{
		try
		{
			double thresh = 0.1;

			LinkageDataSet ld = null;
			PrintStream parout = System.out;
			PrintStream pedout = System.out;

			switch(args.length)
			{
			case 5: thresh = new Double(args[4]).doubleValue();
			case 4: pedout = new PrintStream(args[3]);
			case 3: parout = new PrintStream(args[2]);
			case 2: ld = new LinkageDataSet(args[0],args[1]);
				break;
			default:
				System.err.println("Usage: java HetCutOff input.par input.ped [output.par] [output.ped] [thresh]");
				System.exit(0);
			}

			GeneticDataSource d = new LinkageInterface(ld);

			Vector<Integer> out = new Vector<Integer>();
			for (int i=0; i<d.nLoci(); i++)
			{
				double[] a = d.alleleFreqs(i);
				double h = 0;
				for (int j=0; j<a.length; j++)
					h += a[j];
				for (int j=0; j<a.length; j++)
					a[j] /= h;
				h = 1;
				for (int j=0; j<a.length ;j++)
					h -= a[j]*a[j];
				
				if (h > thresh)
					out.add(new Integer(i));
			}

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
