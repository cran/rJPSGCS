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


import jpsgcs.alun.linkage.LinkagePedigreeData;
import jpsgcs.alun.linkage.LinkageFormatter;

/**
	This program selects a subset of kindreds from a LINKAGE pedigree file.
<ul>
        Usage : <b> java SelectKindreds < input.ped [k1] [k2] ... </b> 
</ul>
        where
<ul>
<li> <b> input.ped </b> is the original LINKAGE pedigree file </li>
<li> <b> [k1] [k2] ... </b> is a list of the kindred identifiers specifying which to include in the
	output</li>
</ul>
	The new LINKAGE pedigree data is written to the standard output file.
	You can probably do the same thing with a grep command.
*/

public class SelectKindreds
{
	public static void main(String[] args)
	{
		try
		{
			LinkagePedigreeData p = new LinkagePedigreeData(new LinkageFormatter(),null,false);
			LinkagePedigreeData[] ped = p.splitByPedigree();
		
			for (int j=0; j<args.length; j++)
			{
				int pedid = new Integer(args[j]).intValue();
				for (int i=0; i<ped.length; i++)
					if (pedid == ped[i].firstPedid())
						ped[i].writeTo(System.out);
			}
		}
		catch (Exception e)
		{
			System.err.println("Caught in SelectKindreds:main()");
			e.printStackTrace();
		}
	}
}
