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
import jpsgcs.alun.linkage.LinkageInterface;
import jpsgcs.alun.genio.GeneticDataSource;
import jpsgcs.alun.linkage.LinkageIndividual;

import java.util.Set;
import java.util.LinkedHashSet;

/**
	This program removes individuals from the pedigree unless they have
	a minimum number of loci at least partially observed or they are
	ancestors of someone else that is observed.

<ul>
	Usage : <b> java TrimPed input.par input.ped min_calls </b> 
</ul>
	where
<ul>
<li> <b> input.par </b> is the original LINKAGE parameter file </li>
<li> <b> input.ped </b> is the original LINKAGE pedigree file </li>
<li> <b> min_calls </b> is the minimum number of loci with some information </li>
</ul>
	A new LINKAGE pedigree data file is written to standard output.
*/

public class TrimPed
{
	public static void main(String[] args)
	{
		try
		{
			int mingenos = 0;
			
			switch(args.length)
			{
			case 3: mingenos = new Integer(args[2]).intValue();
				break;
			default:
				System.err.println("Usage: java TrimPed input.par input.ped min_calls");
				System.exit(1);
			}

			LinkageDataSet data = new LinkageDataSet(args[0],args[1]);
			LinkagePedigreeData ped = data.getPedigreeData();
			GeneticDataSource gen = new LinkageInterface(data);
			
			LinkageIndividual[] ind =  ped.getIndividuals();
			Set<LinkageIndividual> keep = new LinkedHashSet<LinkageIndividual>();

			for (int i=0; i<ind.length; i++)
			{
				int count = 0;
				for (int j=0; j<gen.nLoci() && count < mingenos; j++)
				{
					if (gen.penetrance(i,j) != null)
						count++;
				}

				if (count >= mingenos)
					put(ind[i],keep,ped);
			}

			new LinkagePedigreeData(keep).writeTo(System.out);
		}
		catch (Exception e)
		{
			System.err.println("Caught in TrimPed:main()");
			e.printStackTrace();
		}
	}

	public static void put(LinkageIndividual a, Set<LinkageIndividual> s, LinkagePedigreeData p)
	{
		if (a != null && !s.contains(a))
		{
			s.add(a);
			put(p.pa(a),s,p);
			put(p.ma(a),s,p);
		}
	}
}
