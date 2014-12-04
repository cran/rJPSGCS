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


package jpsgcs.alun.hashing;

import jpsgcs.alun.util.RandomBag;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Collection;
import java.util.Set;

/**
	A set, backed by an jpsgcs.alun.hashing.IdentityHashMap, that allows random selection of an element.
*/

public class RandomIdentitySet<E> extends AbstractSet<E> implements Set<E>, RandomBag<E>
//public class RandomIdentitySet<E> implements Set<E>, RandomBag<E>
{
    	public RandomIdentitySet() 
	{
		map = new IdentityHashMap<E,Object>();
	}

    	public RandomIdentitySet(Collection<? extends E> c) 
	{
		map = new IdentityHashMap<E,Object>();
		addAll(c);
    	}

	public E next()
	{
		return map.randomKey();
	}

	public E draw()
	{
		E x = next();
		if (x != null)
			remove(x);
		return x;
	}

    	public Iterator<E> iterator() 
	{
		//return map.keySet().iterator();
		return map.iterator();
    	}

    	public int size() 
	{
		return map.size();
    	}

	// removeAll and retainAll are written to avoid using the remove()
	// method of the iterator which seems to be unreliable.
	public boolean removeAll(Collection<?> c)
	{
		int s = size();
		for (Object x : c)
			remove(x);
		return size() != s;
	}

	public boolean retainAll(Collection<?> c)
	{
		RandomIdentitySet<E> chuck = new RandomIdentitySet<E>();
		for (E x : this)
			if (!c.contains(x))
				chuck.add(x);
		return removeAll(chuck);
	}

	public boolean addAll(Collection<? extends E> c)
	{
		boolean change = false;
		for (E x : c)
			change = add(x) || change;
		return change;
	}

	public boolean containsAll(Collection<?> c)
	{
		if (size() < c.size())
			return false;

		for (Object x : c)
			if (!contains(x))
				return false;
		return true;
	}

    	public boolean isEmpty() 
	{
		return map.isEmpty();
    	}

    	public boolean contains(Object o) 
	{
		return map.containsKey(o);
    	}

    	public boolean add(E o) 
	{
		return map.put(o, PRESENT)==null;
    	}

    	public boolean remove(Object o) 
	{
		return map.remove(o)==PRESENT;
    	}

    	public void clear() 
	{
		map.clear();
    	}

// Private data.

    	private IdentityHashMap<E,Object> map;
    	private static final Object PRESENT = new Object();
}
