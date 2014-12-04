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
import jpsgcs.alun.linkage.LinkagePedigreeData;
import jpsgcs.alun.linkage.LinkagePhenotype;
import jpsgcs.alun.linkage.NumberedAllelePhenotype;
import jpsgcs.alun.linkage.LinkageIndividual;
import jpsgcs.alun.util.InputFormatter;

import java.util.LinkedHashSet;

/**
	This programs marks inividuals in a pedigree file as being or not being
	probands. 

<ul>
	<li> Usage : <b> java MakeProbands input.par input.ped [mincount] </b> </li>
	<li> Usage : <b> java MakeProbands input.par input.ped [probandfile] </b> </li>
</ul>
	where 
<ul>
<li> <b> input.par </b> is the input LINKAGE parameter file. </li>
<li> <b> input.ped </b> is the input LINKAGE pedigree file. </li>
<li> <b> mincount </b> this is an optional integer parameter specifying the minimum number
	of called genotypes that an idividuals must have to be made a proband. </li>
<li> <b> probandfile </b> if the argument after the specified LINKAGE files cannot be 
	read as an integer, it is assumed to be the name of a file specifying the
	probands. The file must contain one line for each proband and each line
	must have the kindred id number followed by the individual number.
</ul>

<p> This program is designed to make 
	<a ref="SGS.html"> SGS </a>,
	<a ref="SimSGS.html"> SimSGS </a>,
	<a ref="HGS.html"> HGS </a> and 
	<a ref="SimHGS.html"> SimHGS </a>
	easier to use.
	The default action is to make each individual who has at least 10 genotypes
	called a proband, and all the others non-probands. This threshold can be
	changed with a command line argument.
	It is also possible to specify a file of individuals which will be made probands.

*/

public class MakeProbands
{
	public static void main(String[] args)
	{
		try
		{
			int mincount = 10;
			InputFormatter f = null;
			
			LinkageDataSet data = null;

			switch(args.length)
			{
				case 3: try
					{
						mincount = Integer.parseInt(args[2]);
					}
					catch (NumberFormatException e)
					{
						f = new InputFormatter(args[2]);
					}

				case 2: data = new LinkageDataSet(args[0],args[1]);
					break;

				default:
					System.err.println("Usage: java MakeProbands input.par input.ped [nmarkers]");
					System.err.println("Usage: java MakeProbands input.par input.ped [probandfile]");
					System.exit(1);
			}


			LinkagePedigreeData ped = data.getPedigreeData();
			LinkageIndividual[] ind = ped.getIndividuals();
			LinkedHashSet<String> names = new LinkedHashSet<String>();

			if (f == null)
			{
				for (int i=0; i<ind.length; i++)
				{
					ind[i].proband = 0;
					int count = 0;
					LinkagePhenotype[]  phen = new LinkagePhenotype[data.getParameterData().nLoci()];
					if (phen != null)
						for (int j=0; j<phen.length && count < mincount; j++)
						{
							phen[j] = ind[i].getPhenotype(j);
							if (phen[j] instanceof NumberedAllelePhenotype)
							{
								NumberedAllelePhenotype ph = (NumberedAllelePhenotype)(phen[j]);
								if (ph.a1 > 0 && ph.a2 > 0)
									count++;
							}
						}
	
					if (count >= mincount)
					{
						ind[i].proband = 1;
						names.add(ind[i].pedid+" "+ind[i].id);
					}
				}
			}
			else
			{
				LinkedHashSet<String> prob = new LinkedHashSet<String>();
				while (f.newLine())
				{
					String kind = f.nextString();
					String name = f.nextString();
					prob.add(kind+" "+name);
				}

				for (int i=0; i<ind.length; i++)
				{
					ind[i].proband = 0;
					if (prob.contains(ind[i].pedid+" "+ind[i].id))
					{
						ind[i].proband = 1;
						names.add(ind[i].pedid+" "+ind[i].id);
					}
				}
			}

			for (String s : names)
				System.err.println(s+"\t");

			ped.writeTo(System.out);
		}
		catch (Exception e)
		{
			System.err.println("Caught in MakeProbands:main()");
			e.printStackTrace();
		}
	}
}
