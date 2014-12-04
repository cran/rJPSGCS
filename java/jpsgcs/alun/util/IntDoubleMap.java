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

public class IntDoubleMap
{
	public IntDoubleMap(int capacity)
	{
		x = new IntDoubleItem[capacity];
		head = null;
		used = 0;
	}

	public IntDoubleItem find(int i)
	{
		int h = hash(i);
		
		for (IntDoubleItem y = x[h]; y != null && y.h == h; y = y.next)
			if (y.i == i)
				return y;

		return null;
	}

	public IntDoubleItem force(int i)
	{
		int h = hash(i);

		IntDoubleItem z = null;

		for (IntDoubleItem y = x[h]; y != null && y.h == h; y = y.next)
			if (y.i == i)
			{
				z = y;
				break;
			}

		if (z == null)
		{
			z = new IntDoubleItem(i,0,h);
			insert(z,x[h]);
			x[h] = z;
		}
	
		return z;
	}

	public IntDoubleItem head()
	{
		return head;
	}

	public void put(int i, double d)
	{
		IntDoubleItem y = force(i);
		y.d = d;
	}

	public double get(int i)
	{
		IntDoubleItem y = find(i);
		return y == null ? 0 : y.d;
	}

	public void resize(int capacity)
	{
		IntDoubleItem y = head;

		x = new IntDoubleItem[capacity];
		head = null;
		used = 0;
		
		while (y != null)
		{
			IntDoubleItem z = y;
			y = y.next;
			z.next = z.prev = null;
			z.h = hash(z.i);
			insert(z,x[z.h]);
			x[z.h] = z;
		}
	}

	public void clean()
	{
		for (IntDoubleItem y = head; y != null; )
		{
			IntDoubleItem z = y;
			y = y.next;
			if (!(z.d > 0))
				remove(z);
		}
	}

	public void clear()
	{
		x = new IntDoubleItem[x.length];
		head = null;
		used = 0;
	}
		
	public int size()
	{
		return used;
	}

// Private data and methods.

	private IntDoubleItem[] x = null;
	private IntDoubleItem head = null;
	private int used = 0;

	private int hash(int i)
	{
		return i % x.length;
	}
	
	public void remove(IntDoubleItem z)
	{
		int h = z.h;

		if (x[h] == z)
		{
			x[h] = z.next;
			if (x[h] != null && x[h].h != h)
				x[h] = null;
		}
		
		if (head == z)
			head = z.next;

		if (z.next != null)
			z.next.prev = z.prev;
		if (z.prev != null)
			z.prev.next = z.next;
		
		z.next = z.prev = null;

		used--;
	}

	private void insert(IntDoubleItem z, IntDoubleItem y)
	{
		if (y == null)
		{
			z.next = head;
			if (z.next != null)
				z.next.prev = z;
			head = z;
		}
		else
		{
			z.next = y;
			z.prev = y.prev;
			if (head == y)
				head = z;

			if (z.next != null)
				z.next.prev = z;
			if (z.prev != null)
				z.prev.next = z;
		}
		
		used++;
	}
}
