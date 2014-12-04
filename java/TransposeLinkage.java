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


import jpsgcs.alun.linkage.*;
import jpsgcs.alun.genio.*;
import jpsgcs.alun.util.*;
import java.io.PrintStream;

public class TransposeLinkage
{
	public static void main(String[] args)
	{
		PrintStream flipped_ped = System.out;

		try
		{
			LinkageParameterData par = new LinkageParameterData(new LinkageFormatter(args[0]));
			LinkagePedigreeData ped = new LinkagePedigreeData(new LinkageFormatter(args[1]),par);
			if (args.length == 3) flipped_ped = new PrintStream(args[2]);
			ped.writeTranspose(flipped_ped);
			flipped_ped.close();
		}
		catch(RuntimeException e) {}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
