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
        This program reads in a LINKAGE parameter file assuming that the 
	distances between the loci are specified as cMorgans
	and converts these to recombination fractions.

<ul>
        Usage : <b> java cMorgansToTheta < input.par > output.par </b> </li>
</ul>
        where
<ul>
<li> <b> input.par </b> is the input LINKAGE parameter file. </li>
<li> <b> output.par </b> is the checked output LINKAGE parameter file.
</ul>

<p>
	The results are output to a new LINKAGE parameter file.
*/

public class cMorgansToTheta
{
	public static void main(String[] args)
	{
		try
		{
			LinkageParameterData p = new LinkageParameterData(new LinkageFormatter(),true);
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
