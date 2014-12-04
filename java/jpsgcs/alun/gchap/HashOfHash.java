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


package jpsgcs.alun.gchap;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;

/**
 Creates a structure of two way hash tables, actually a hashtable
 of hashtables. This allows
 putting and getting of objects by a pair of keys.
 This structure is basically similar to an adjacency matrix
 representation of a graph.
 This implementation is not symetric, that is, the key pair (a,b)
 is not the same as the key pair (b,a).
*/
public class HashOfHash
{
/**
 Create a new hashtable of hashtables.
*/
	public HashOfHash()
	{
		clear();
	}

/**
 Empties the current structure.
*/
	public final void clear()
	{
		h = new Hashtable();
	}

/**
 Puts the third object into a location keys by the first two.
*/
	public final void put(Object a, Object b, Object c)
	{
		put(a);
		get(a).put(b,c);
	}

/**
 Gets the object keyed by the given objects.
 get(a,b) and get(b,a) return the same object.
 If there is no keyed element, it returns null.
*/
	public final Object get(Object a, Object b)
	{
		return get(a) == null ? null : (b== null ? null : get(a).get(b));
	}

/**
 Removes any object keyed to by the given objects.
 remove(a,b) and remove(b,a) have the same effect.
*/
	public final Object remove(Object a, Object b)
	{
		Object c = get(a,b);
		if (c != null)
			get(a).remove(b);
		return c;
	}

/**
 Returns a string representation of the data in the structure.
*/
	public String toString()
	{
		StringBuffer s = new StringBuffer();
		for (Enumeration e = get().keys(); e.hasMoreElements();)
		{
			Object a = e.nextElement();
			s.append(a+":\t");
			for (Enumeration f = get(a).keys(); f.hasMoreElements();)
			{
				Object b = f.nextElement();
				s.append(" "+b+" ("+get(a,b)+")");
			}
			s.append("\n");
		}

		return s.toString();
	}

/**
 If it doesn't already exist this puts a new hashtable keyed to 
 by the object a.
 Returns true iff a new insertion was necessary.
*/
	public final boolean put(Object a)
	{
		if (get(a) != null)
			return false;
		get().put(a,new Hashtable());
		return true;
	}

/**
 Returns the hashtable keyed by the object a.
 get(a,b) is equivalent to get(a).get(b) with some checking;
*/
	public final Hashtable get(Object a)
	{
		return (Hashtable) (a == null ? null : get().get(a));
	}

// Protected methods.

/**
 Returns the hashtable in which the other hashtables are keyed.
*/
	public final Hashtable get()
	{
		return h;
	}

// Private data and methods.

	private Hashtable h = null;
}
