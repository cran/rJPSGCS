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


package jpsgcs.alun.util;

import java.io.PrintStream;

public class Monitor
{
	private static Runtime r = null;
	private static double start = 0;
	private static double curtime = 0;
	private static double mb = 10*1024;
	private static boolean quiet = false;

	static
	{
		start = System.currentTimeMillis()/1000.0;
		curtime = start;
		r = Runtime.getRuntime();
		quiet = false;
	}

	public static void quiet(boolean b)
	{
		quiet = b;
	}

	public static void show()
	{
		show("Usage: ",System.err);
	}

	public static void show(String s)
	{
		show(s,System.err);
	}

	public static void show(String s, PrintStream ps)
	{
		if (quiet)
			return;

		double time = System.currentTimeMillis()/1000.0;
		ps.print(s+"\t"+(time-curtime)+"\t"+(time-start)+"\t");
		curtime = time;

/*
		double store = 0;

		store = r.freeMemory()/mb;
		ps.print(store+"\t");

		store = ( r.totalMemory() - r.freeMemory() ) / mb;
		ps.print(store+"\t");
*/

		ps.println();
	}
}
