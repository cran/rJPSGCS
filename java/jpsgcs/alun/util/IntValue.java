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
 A wrapper around a modifiable int value.
*/

public class IntValue implements Comparable<IntValue>
{
        public int i = 0;

	public IntValue()
	{
	}

        public IntValue(int j)
        {
                i = j;
        }

	public void add(int x)
	{
		i = i+x;
	}

	public int value()
	{
		return i;
	}

        public final int compareTo(IntValue x)
        {
                return ( i<x.i ? -1 : ( i>x.i ? 1 : 0 ) );
        }

        public final int hashCode()
        {
                return i;
        }

        public final boolean equals(Object o)
        {
                return o instanceof IntValue && ((IntValue)o).i == i;
        }

        public String toString()
        {
                return i+"";
        }
}
