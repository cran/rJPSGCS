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
import jpsgcs.alun.util.Main;

import java.util.Vector;

/**
 	This program reads converts a pedigree file in "premakeped" format into
	a standard LINKAGE pedigree file.
<ul>
	Usage : <b> java MakePed [-1] < input.ped > output.ped </b> </li>
</ul>
	where 
<ul>
<li> <b> input.ped </b> is the input LINKAGE pedigree file. </li>
<li> <b> output.ped </b> is the checked output LINKAGE pedigree file.  </li>
<li> <b> -1 </b> is an option that causes the program to discard the first line
of the input file before processing begins. This option should be specified if the
input file contains a header line. The default action is not to strip the first line.</li>
</ul>

<p>
	The program differs from the standard MAKEPED program in that the
	individuals are not renumbered.

*/

public class MakePed
{
	public static void main(String[] args)
	{
		try
		{
			boolean stripfirstline = false;

			String[] bargs = Main.strip(args,"-1");
			if (bargs != args)
			{
				stripfirstline = true;
				args = bargs;
			}

			switch(args.length)
			{
			case 0: break;
			default:
				System.err.println("Usage: java MakePed [-1] < premakeped > postmakeped");
				System.exit(1);
			}

			LinkageFormatter f = new LinkageFormatter();
			if (stripfirstline)
				f.newLine();
			LinkagePedigreeData p = new LinkagePedigreeData(f,null,true);

			p.writeTo(System.out);
		}
		catch (Exception e)
		{
			System.err.println("Caught in MakePed:main().");
			System.err.println("Likely to be an unexpected type of data formatting error.");
			e.printStackTrace();
		}
	}
}
