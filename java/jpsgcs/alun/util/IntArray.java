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

public class IntArray
{
	public IntArray(int[] b)
	{
		a = new int[b.length];
		hash = 0;
		for (int i=0; i<a.length; i++)
		{
			a[i] = b[i];
			hash = hash + hash + a[i];
		}
	}

	public IntArray(IntArray b)
	{
		this(b.asArray());
	}

	public IntArray(int a, int b, int c)
	{
		this(toArray(a,b,c));
	}

	public final int hashCode()
	{
		return hash;
	}

	public final int size()
	{
		return a.length;
	}

	public final int get(int i)
	{
		return a[i];
	}

	public String toString()
	{
		return toString(" ");
	}

	public String toString(String sep)
	{
		StringBuffer s = new StringBuffer();
		for (int i=0; i<a.length; i++)
		{
			s.append(a[i]);
			if (i < a.length -1 )
				s.append(sep);
		}
		return s.toString();
	}

	public final boolean equals(Object y)
	{
		if (!(y instanceof IntArray))
			return false;

		IntArray x = (IntArray) y;

		if (a.length != x.a.length)
			return false;

		for (int i=0; i<a.length; i++)
			if (a[i] != x.a[i])
				return false;
	
		return true;
	}

	public final int[] asArray()
	{
		return a;
	}

	public static int[] toArray(int a, int b, int c)
	{
		int[] x = {a, b, c};
		return x;
	}

	private int[] a = null;
	private int hash = 0;
}
