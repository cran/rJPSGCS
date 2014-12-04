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


import jpsgcs.alun.linkage.LinkageParameterData;
import jpsgcs.alun.linkage.LinkageFormatter;

/**
        This program reads in a LINKAGE parameter, checks that it is correctly
	foratted and then outputs the checked file.
<ul>
        Usage : <b> java CheckParameters < input.par > output.par </b> </li>
</ul>
        where
<ul>
<li> <b> input.par </b> is the input LINKAGE parameter file. </li>
<li> <b> output.par </b> is the checked output LINKAGE parameter file.
</ul>

<p>
        Error and warning messages are printed to the screen. The checks made by this program are
        made whenever data is input.

<p>
	This program is effectively the first part of the <a href="CheckFormat.html"> CheckFormat </a>
	program that checks a combination of LINKAGE parameter and pedigree files.

<p>
        See my <a href="linkage.html"> LINKAGE format page </a> for information
        about the way this suite of programs uses this format.

*/

public class CheckParameters
{
	public static void main(String[] args)
	{
		try
		{
			LinkageParameterData p = new LinkageParameterData(new LinkageFormatter());
			p.writeTo(System.out);
		}
		catch (Exception e)
		{
			System.err.println("Caught in CheckParameters:main().");
			System.err.println("Likely to be an unexpected type of data formatting error.");
			e.printStackTrace();
		}
	}
}
