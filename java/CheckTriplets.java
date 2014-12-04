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
import jpsgcs.alun.linkage.LinkageIndividual;

/**
 	This program checks a pedigree specified by a list of triplets for self 
	consisency and completeness. 

<ul>
        Usage : <b> java CheckTriplets < triplet.file > output.ped </b> </li>
</ul>
        where
<ul>
<li> <b> triplet.file </b> is a file specifiying the pedigree as a list
	of triplet. One triplet per line. Order is assumed to be individual id,
	father's id, mother's id. Any other data following this is ignored.  </li>
<li> <b> output.ped </b> is the checked pedigree output file. </li>
</ul>

<p>	Error messages are output to the screen.
	The checked pedigree is output in LINKAGE format.

*/

public class CheckTriplets
{
	public static void main(String[] args)
	{
		try
		{
			LinkagePedigreeData p = new LinkagePedigreeData(new LinkageFormatter(),null,LinkageIndividual.TRIPLET);
			p.writeTo(System.out);
		}
		catch (Exception e)
		{
			System.err.println("Caught in CheckTriplets:main()");
			e.printStackTrace();
		}
	}
}
