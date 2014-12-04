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

public class Separator<V> extends Clique<V>
{
	public Separator(Clique<V> a, Clique<V> b)
	{
		this(a,b,-1);
	}

	public Separator(Clique<V> a, Clique<V> b, int ii)
	{
		super();
		x = a;
		y = b;

		for (V z : x)
			if (y.contains(z))
				add(z);
	}

	public Clique<V> getX()
	{
		return x;
	}

	public Clique<V> getY()
	{
		return y;
	}

	public void setX(Clique<V> xx)
	{
		x = xx;
	}

	public void setY(Clique<V> yy)
	{
		y = yy;
	}

	private Clique<V> x = null;
	private Clique<V> y = null;
}
