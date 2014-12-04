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

import java.util.Collection;
import java.util.LinkedHashSet;

public class RadixPlaneSorter<E extends CartesianPoint>
{
	public RadixPlaneSorter(double x, double y, int k)
	{
		l = new Lattice<Collection<E>>(k);
		xgap = x;
		ygap = y;
	}

	public void add(E m)
	{
		tile(m.x,m.y).add(m);
	}

	public void remove(E m)
	{
		tile(m.x,m.y).remove(m);
	}

	public Collection<E> getLocal(E a, double r)
	{
		double rr = r*r;
		int xl = (int)( (a.x-r)/xgap );
		int xh = (int)( (a.x+r)/xgap );
		int yl = (int)( (a.y-r)/ygap );
		int yh = (int)( (a.y+r)/ygap );

		Collection<E> s = null;
		Collection<E> v = new LinkedHashSet<E>();

//System.err.println(xl+" "+xh+" "+yl+" "+yh);

		for (int i=xl; i<=xh; i++)
			for (int j=yl; j<=yh; j++)
			{
				s = l.get(i,j);
				if (s != null)
				{
					for (E m : s)
					{
						double xx = m.x - a.x;
						xx = xx*xx;
						double yy = m.y - a.y;
						yy = yy*yy;
						if (xx+yy < rr)
							v.add(m);
					}
				}
			}

		v.remove(a);

		return v;
	}

// Private data.

	private Lattice<Collection<E>> l = null;
	private double xgap = 0;
	private double ygap = 0;

	private Collection<E> tile(double a, double b)
	{
		int i = (int)(a/xgap);
		int j = (int)(b/ygap);
		Collection<E> s = l.get(i,j);
		if (s == null)
		{
			s = new LinkedHashSet<E>();
			l.put(i,j,s);
		}
		return s;
	}
}
