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


package jpsgcs.alun.jtree;

import jpsgcs.alun.markov.Variable;
import jpsgcs.alun.util.InputFormatter;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;
import java.util.Set;
import java.util.LinkedHashSet;

public class DataMatrix implements IntegerMatrix
{
	public DataMatrix() throws IOException
	{
		this(new InputFormatter());
	}

	public DataMatrix(InputFormatter f) throws IOException
	{
		Vector<int[]> v = new Vector<int[]>();

		int n = -1;

		while (f.newLine())
		{
			int[] t = new int[f.itemsLeftOnLine()];
			for (int i=0; i<t.length; i++)
				t[i] = f.nextInt();

			v.add(t);

			if (n < 0)
			{
				n = t.length;
				continue;
			}

			if (t.length != n)
				throw new RuntimeException("Data row length mismatch in DataMatrix constructor");
		}

		dat = (int[][]) v.toArray(new int[0][]);

		vars = new Variable[n];

		for (int i=0; i<vars.length; i++)
		{
			Set<Integer> val = new LinkedHashSet<Integer>();
			for (int j=0; j<dat.length; j++)
				val.add(dat[j][i]);

			int[] iii = new int[val.size()];
			n = 0;
			for (Integer j : val)
				iii[n++] = j.intValue();

			vars[i] = new Variable(iii.length);
			vars[i].setStates(iii);
		}
	}

	public void writeTo(PrintStream p)
	{
		for (int i=0; i<dat.length; i++)
		{
			for (int j=0; j<dat[i].length; j++)
				p.print(" "+dat[i][j]);
			p.println();
		}
	}
	
/*
	public int[][] getMatrix()
	{
		return dat;
	}
*/

	public int nRows()
	{
		return dat.length;
	}

	public int value(int i, int j)
	{
		return dat[i][j];
	}

	public Variable[] getVariables()
	{
		return vars;
	}

	private int[][] dat = null;
	private Variable[] vars = null;
}
