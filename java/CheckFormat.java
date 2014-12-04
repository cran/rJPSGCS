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
import jpsgcs.alun.util.Main;

import java.io.PrintStream;

/**
 	This program reads in LINKAGE parameter and pedigree files, checks that
	they are readable and then outputs the checked files. 
<ul>
	Usage : <b> java CheckFormat input.par input.ped [output.par] [output.ped] [-pre] </b> </li>
</ul>
	where 
<ul>
<li> <b> input.par </b> is the input LINKAGE parameter file. </li>
<li> <b> input.ped </b> is the input LINKAGE pedigree file. </li>
<li> <b> output.par </b> is the checked output LINKAGE parameter file. 
	The default is to write to standard output.</li>
<li> <b> output.ped </b> is the checked output LINKAGE pedigree file. 
	The default is to write to standard output.</li>
<li> <b> -pre </b> If this option is specified the data is assumed to be in 
	so called "pre-makeped" format and interpreted appropritately. 
	The output will be in regular format not "pre-makeped" format.  </li>
</ul>

<p>
	Error and warning
	messages are printed to the screen. The checks made by this program are
	made whenever data is input.

<p>
	The LINKAGE format page describes the difference between "pre-makeped" and standard "post-makeped" formats.

*/

public class CheckFormat
{
	public static void main(String[] args)
	{
		try
		{
			boolean premake = false;
			PrintStream parout = System.out;
			PrintStream pedout = System.out;
			LinkageDataSet x = null;

			String[] bargs = Main.strip(args,"-pre");
			if (bargs != args)
			{
				premake = true;
				args = bargs;
			}

			switch(args.length)
			{
			case 4: pedout = new PrintStream(args[3]);

			case 3: parout = new PrintStream(args[2]);

			case 2: x = new LinkageDataSet(args[0],args[1],premake);
				break;

			default:
				System.err.println("Usage: java CheckFormat input.par input.ped [output.par] [output.ped] [-pre]");
				System.exit(1);
			}

			x.getParameterData().writeTo(parout);
			parout.flush();
			x.getPedigreeData().writeTo(pedout);
			pedout.flush();

			parout.close();
			pedout.close();
		}
		catch(RuntimeException e) {}
		catch (Exception e)
		{
			System.err.println("Caught in CheckFormat:main().");
			System.err.println("Likely to be an unexpected type of data formatting error.");
			e.printStackTrace();
		}
	}
}
