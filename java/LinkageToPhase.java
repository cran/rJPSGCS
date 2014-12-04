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
import jpsgcs.alun.linkage.LinkageInterface;

/** 
	Takes data from LINKAGE format parameter and pedigree files and
	prepares it for input into the PHASE haplotyping program.

<ul>
	Usage : <b> java LinkageToPhase input.par input.ped </b> </li>
</ul>
	where 
<ul>
<li> <b> input.par </b> is the input LINKAGE parameter file. </li>
<li> <b> input.ped </b> is the input LINKAGE pedigree file. </li>
</ul>

*/

public class LinkageToPhase
{
	public static void main(String[] args)
	{
		try
		{
			switch(args.length)
			{
			case 2:
				LinkageDataSet ld = new LinkageDataSet(args[0],args[1]);
				LinkageInterface l = new LinkageInterface(ld);
				System.out.println(l.toPhase());
				break;
			default:
				System.err.println("Usage: java LinkageToPhase input.par input.ped");
				System.exit(0);
			}

		}
		catch (Exception e)
		{
			System.err.println("Caught in LinkageToPhase:main()");
			e.printStackTrace();
		}
	}
}
