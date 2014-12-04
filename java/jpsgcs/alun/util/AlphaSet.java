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

import java.util.TreeSet;
import java.util.Iterator;
import java.util.Collection;

/**
	This is an extention of a TreeSet that requires the elements
	to be Comparable, but also is Comparable itself based on the
	usual alphabetic rules for sorting the contents.

	Should be useful when we need to deal with sets of sets
	of Comparable objects.

	TreeSet should really do this itself.
*/

public class AlphaSet<E> extends TreeSet<E> implements Comparable<AlphaSet<E>>
{
	public AlphaSet()
	{
		super();
	}

	public AlphaSet(Collection<? extends E> x)
	{
		super(x);
	}

	public int compareTo(AlphaSet<E> x)
	{
		Iterator<E> i = iterator();
		Iterator<E> j = x.iterator();

		while (true)
		{
			if (i.hasNext())
			{
				if (j.hasNext())
				{
					int k = ((Comparable)i.next()).compareTo((Comparable)j.next());
					if (k != 0)
						return k;
				}
				else
				{
					return 1;
				}
			}
			else
			{
				return j.hasNext() ? -1 : 0;
			}
		}
	}

	public String toString()
	{
		StringBuffer s = new StringBuffer();
		s.append("[");

		for (E x : this)
			s.append(x+",");

		if (this.size() > 0)
			s.deleteCharAt(s.length()-1);

		s.append("]");

		return s.toString();
	}

/**
	Test main.
*/
	public static void main(String[] args)
	{
		AlphaSet<String> p = new AlphaSet<String>();
		p.add("cat");
		p.add("bat");
		p.add("plop");
		p.add("a");

		AlphaSet<String> q = new AlphaSet<String>();
		q.add("b");
		q.add("a");
		q.add("alphabet");


		AlphaSet<String> r = new AlphaSet<String>();
		r.addAll(p);
		r.addAll(q);
		r.add("aa");

		AlphaSet<String> s = new AlphaSet<String>();
		s.add("a");

		AlphaSet<AlphaSet<String>> u = new AlphaSet<AlphaSet<String>>();

		u.add(p);
		u.add(q);
		u.add(r);
		u.add(s);

		for (AlphaSet<String> x : u)
			System.out.println(x);
	}
}
