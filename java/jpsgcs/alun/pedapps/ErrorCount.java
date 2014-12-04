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


package jpsgcs.alun.pedapps;

import jpsgcs.alun.pedmcmc.Error;

public class ErrorCount
{
	public ErrorCount(Error[][] error)
	{
		e = error;

		int mx = 0;
		for (int i=0; i<e.length; i++)
			if (e[i] != null && e[i].length > mx)
				mx = e[i].length;
		c = new double[e.length][mx];

		n = 0;
	}

	public void update()
	{
		for (int i=0; i<e.length; i++)
			if (e[i] != null)
			{
				for (int j=0; j<e[i].length; j++)
					if (e[i][j] != null)
					{
						c[i][j] += e[i][j].getState();
					}
			}
		n += 1;
	}

	public double[][] results()
	{
		double[][] res = new double[c.length][c[0].length];

		for (int i=0; i<c.length; i++)
			for (int j=0; j<c[i].length; j++)
				res[i][j] = c[i][j] / n;

		return res;
	}

// Private data and methods.

	private Error[][] e = null;
	private double[][] c = null;
	private double n = 0;
}
