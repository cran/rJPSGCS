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

/**
 	This program reads in LINKAGE a pedigree file, and checks that the pedigree
	information is correctly formatted and self consistent. 

<ul>
	Usage : <b> java CheckFormat < input.ped > output.ped [-pre] </b> </li>
</ul>
	where 
<ul>
<li> <b> input.ped </b> is the input LINKAGE pedigree file. </li>
<li> <b> output.ped </b> is the checked output LINKAGE pedigree file. 
<li> <b> -pre </b> If this option is specified the data is assumed to be in 
	so called "pre-makeped" format and interpreted appropritately. 
	The output will be in regular format not "pre-makeped" format.  </li>
</ul>

<p> 	The checked file is written to the standard output stream. 
	Error and warning messages are printed to the screen. 

<p>
	Note that only the pedigree information is checked for correct formatting.
	To check that the phenotype and genotype information is correctly formatted
	it's necessary to know the contents of the appropriate LINKAGE parameter
	file. So use <a href="CheckFormat.html"> CheckFormat </a> for this.

*/

public class CheckPedigree
{
	public static void main(String[] args)
	{
		try
		{
			boolean premake = false;
			
			String[] bargs = Main.strip(args,"-pre");
			if (bargs != args)
			{
				premake = true;
				args = bargs;
			}

			LinkagePedigreeData p = new LinkagePedigreeData(new LinkageFormatter(),null,premake);
			p.writeTo(System.out);
		}
		catch (Exception e)
		{
			System.err.println("Caught in CheckPedigree:main().");
			System.err.println("Likely to be an unexpected type of data formatting error.");
			e.printStackTrace();
		}
	}
}
