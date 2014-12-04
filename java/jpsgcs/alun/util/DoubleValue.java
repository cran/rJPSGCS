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

/**
 A wrapper around a modifiable double.
*/

public class DoubleValue implements Comparable<DoubleValue>
{
	public double x = 0;

	public DoubleValue()
	{
	}
	
	public DoubleValue(double d)
	{
		x = d;
	}

	public double value()
	{
		return x;
	}

        public final int compareTo(DoubleValue a)
        {
                return ( x<a.x ? -1 : ( x>a.x ? 1 : 0 ) );
        }

        public final boolean equals(Object o)
        {
               	return o instanceof DoubleValue && ((DoubleValue)o).x == x;
        }

        public String toString()
        {
                return x+"";
        }
}
