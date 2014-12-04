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
//package java.util;

import java.io.*;
import java.util.Collection;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.AbstractSet;
import java.util.AbstractCollection;

public class IdentityHashMap<K,V> //extends AbstractMap<K,V> //implements Map<K,V>
{
public static int loops = 0;
public static int calls = 0;

    	//private static final int DEFAULT_CAPACITY = 32;
    	private static final int DEFAULT_CAPACITY = 4;
    	private static final int MINIMUM_CAPACITY = 4;
    	private static final int MAXIMUM_CAPACITY = 1 << 29;
    	private static final Object NULL_KEY = new Object();

	public K randomKey()
	{
		if (size == 0)
			return null;

		int i = 0;
		Object result = null;
		do
		{
			i = 2 * (int)(Math.random() * table.length / 2.0);
			result = table[i];
//loops++;
		}
		while (result == null);

//calls++;
		return (K) result;
	}

    	public IdentityHashMap() 
	{
        	init(DEFAULT_CAPACITY);
    	}

    	public IdentityHashMap(int expectedMaxSize) 
	{
        	if (expectedMaxSize < 0)
            		throw new IllegalArgumentException("expectedMaxSize is negative: " + expectedMaxSize);
        	init(capacity(expectedMaxSize));
    	}

    	public int size() 
	{
        	return size;
    	}

    	public V put(K key, V value) 
	{
        	Object k = maskNull(key);
        	Object[] tab = table;
        	int len = tab.length;
        	int i = hash(k, len);

        	Object item;
        	while ( (item = tab[i]) != null) 
		{
            		if (item == k) 
			{
				V oldValue = (V) tab[i + 1];
                		tab[i + 1] = value;
                		return oldValue;
            		}
            		i = nextKeyIndex(i, len);
        	}

        	modCount++;
        	tab[i] = k;
        	tab[i + 1] = value;
        	if (++size >= threshold)
            		resize(len);

        	return null;
    	}

    	public V remove(Object key) 
	{
        	Object k = maskNull(key);
        	Object[] tab = table;
        	int len = tab.length;
        	int i = hash(k, len);
	
        	while (true) 
		{
            		Object item = tab[i];
            		if (item == k) 
			{
                		modCount++;
                		size--;
                		V oldValue = (V) tab[i + 1];
                		tab[i + 1] = null;
                		tab[i] = null;
                		closeDeletion(i);
                		return oldValue;
            		}
            		if (item == null)
                		return null;
            		i = nextKeyIndex(i, len);
        	}
    	}

    	/**
     	* Rehash all possibly-colliding entries following a
     	* deletion. This preserves the linear-probe
     	* collision properties required by get, put, etc.
     	*
     	* @param d the index of a newly empty deleted slot
     	*/
    	private void closeDeletion(int d) 
	{
        	// Adapted from Knuth Section 6.4 Algorithm R
        	Object[] tab = table;
        	int len = tab.length;
	
        	// Look for items to swap into newly vacated slot
        	// starting at index immediately following deletion,
        	// and continuing until a null slot is seen, indicating
        	// the end of a run of possibly-colliding keys.

        	Object item;
        	for (int i = nextKeyIndex(d, len); (item = tab[i]) != null; i = nextKeyIndex(i, len) ) 
		{
            		// The following test triggers if the item at slot i (which
            		// hashes to be at slot r) should take the spot vacated by d.
            		// If so, we swap it in, and then continue with d now at the
            		// newly vacated i.  This process will terminate when we hit
            		// the null slot at the end of this run.
            		// The test is messy because we are using a circular table.

            		int r = hash(item, len);
            		if ((i < r && (r <= d || d <= i)) || (r <= d && d <= i)) 
			{
                		tab[d] = item;
                		tab[d + 1] = tab[i + 1];
                		tab[i] = null;
                		tab[i + 1] = null;
                		d = i;
            		}
        	}
    	}

	private class MyKeyIterator implements Iterator<K>
	{
		private int i = 0;
		private int j = 0;

		public MyKeyIterator()
		{
			j = i = -2;
			do { i += 2; } while (i < table.length && table[i] == null);
		}

		public boolean hasNext()
		{
			return i < table.length;
		}

		public K next()
		{
			j = i;
			do { i += 2; } while (i < table.length && table[i] == null);
			return (K) unmaskNull(table[j]);
		}

		public void remove()
		{
			throw new UnsupportedOperationException("IdentityHashMap.MyKeyIterator doesn't do removing");
		}
	}

	private class KeyIterator implements Iterator<K>
	{
        	public K next() 
		{
            		return (K) unmaskNull(traversalTable[nextIndex()]);
		}

        	int index = (size != 0 ? 0 : table.length); // current slot.
        	int expectedModCount = modCount; // to support fast-fail
        	int lastReturnedIndex = -1;      // to allow remove()
        	boolean indexValid; // To avoid unnecessary next computation
		Object[] traversalTable = table; // reference to main table or copy

        	public boolean hasNext() 
		{
            		Object[] tab = traversalTable;
            		for (int i = index; i < tab.length; i+=2) 
			{
                		Object key = tab[i];
                		if (key != null) 
				{
                    			index = i;
                    			return indexValid = true;
                		}
            		}
            		index = tab.length;
            		return false;
        	}

        	protected int nextIndex() 
		{
            		if (modCount != expectedModCount)
                		throw new ConcurrentModificationException();
            		if (!indexValid && !hasNext())
                		throw new NoSuchElementException();
		
            		indexValid = false;
            		lastReturnedIndex = index;
            		index += 2;
            		return lastReturnedIndex;
        	}
	
		// This method is unreliable. It can result in size not matching the actual
		// cardinality of the set.
        	public void remove() 
		{
            		if (lastReturnedIndex == -1)
                		throw new IllegalStateException();
            		if (modCount != expectedModCount)
                		throw new ConcurrentModificationException();
	
            		expectedModCount = ++modCount;
            		int deletedSlot = lastReturnedIndex;
            		lastReturnedIndex = -1;
            		size--;
            		// back up index to revisit new contents after deletion
            		index = deletedSlot;
            		indexValid = false;
	
       	     		// Removal code proceeds as in closeDeletion except that
       	     		// it must catch the rare case where an element already
            		// seen is swapped into a vacant slot that will be later
            		// traversed by this iterator. We cannot allow future
            		// next() calls to return it again.  The likelihood of
            		// this occurring under 2/3 load factor is very slim, but
            		// when it does happen, we must make a copy of the rest of
            		// the table to use for the rest of the traversal. Since
            		// this can only happen when we are near the end of the table,
            		// even in these rare cases, this is not very expensive in
            		// time or space.
	
            		Object[] tab = traversalTable;
            		int len = tab.length;
	
            		int d = deletedSlot;
            		K key = (K) tab[d];
            		tab[d] = null;        // vacate the slot
            		tab[d + 1] = null;
	
            		// If traversing a copy, remove in real table.
            		// We can skip gap-closure on copy.
            		if (tab != IdentityHashMap.this.table) 
			{
                		IdentityHashMap.this.remove(key);
                		expectedModCount = modCount;
                		return;
            		}
		
       	     		Object item;
            		for (int i = nextKeyIndex(d, len); (item = tab[i]) != null; i = nextKeyIndex(i, len)) 
			{
                		int r = hash(item, len);
                		// See closeDeletion for explanation of this conditional
                		if ((i < r && (r <= d || d <= i)) || (r <= d && d <= i)) 
				{
	
                    			// If we are about to swap an already-seen element
                    			// into a slot that may later be returned by next(),
                    			// then clone the rest of table for use in future
                    			// next() calls. It is OK that our copy will have
                    			// a gap in the "wrong" place, since it will never
                    			// be used for searching anyway.
			
                    			if (i < deletedSlot && d >= deletedSlot && traversalTable == IdentityHashMap.this.table) 
					{
                        			int remaining = len - deletedSlot;
                        			Object[] newTable = new Object[remaining];
                        			System.arraycopy(tab, deletedSlot, newTable, 0, remaining);
                        			traversalTable = newTable;
                        			index = 0;
                    			}
	
                    			tab[d] = item;
                    			tab[d + 1] = tab[i + 1];
                    			tab[i] = null;
                    			tab[i + 1] = null;
                    			d = i;
                		}
            		}
        	}
    	}

    	public boolean isEmpty() 
	{
        	return size == 0;
    	}

    	public boolean containsKey(Object key) 
	{
        	Object k = maskNull(key);
        	Object[] tab = table;
        	int len = tab.length;
        	int i = hash(k, len);
        	while (true) 
		{
            		Object item = tab[i];
            		if (item == k)
                		return true;
            		if (item == null)
                		return false;
            		i = nextKeyIndex(i, len);
        	}
    	}

    	public void clear() 
	{
        	modCount++;
        	Object[] tab = table;
        	for (int i = 0; i < tab.length; i++)
            	tab[i] = null;
        	size = 0;
    	}

    	public boolean equals(Object o) 
	{
        	if (o == this) 
            		return true;

		if (o instanceof Map) 
		{
            		Map m = (Map)o;

			for (int i=0; i<table.length; i += 2)
				if (!m.containsKey(table[i]) || m.get(table[i]) != table[i+1])
					return false;
			return true;
        	} 

      		return false; 
    	}

    	public int hashCode() 
	{
        	int result = 0;
        	Object[] tab = table;
        	for (int i = 0; i < tab.length; i +=2) 
		{
            		Object key = tab[i];
            		if (key != null) 
			{
                		Object k = unmaskNull(key);
                		result += System.identityHashCode(k) ^ System.identityHashCode(tab[i + 1]);
            		}
        	}
        	return result;
    	}

	public Iterator<K> iterator()
	{
		return new MyKeyIterator();
	}

// Private data and methods.

    	private transient Object[] table;
    	private int size;
    	private transient volatile int modCount;
    	private transient int threshold;

    	private static Object maskNull(Object key) 
	{
        	return (key == null ? NULL_KEY : key);
    	}

    	private static Object unmaskNull(Object key) 
	{
        	return (key == NULL_KEY ? null : key);
    	}

    	private int capacity(int expectedMaxSize) 
	{
        	int minCapacity = (3 * expectedMaxSize)/2;
	
        	int result;
        	if (minCapacity > MAXIMUM_CAPACITY || minCapacity < 0) 
		{
            		result = MAXIMUM_CAPACITY;
        	} 
		else 	
		{
            		result = MINIMUM_CAPACITY;
            		while (result < minCapacity)
                		result <<= 1;
        	}
        	return result;
    	}

    	private void init(int initCapacity) 
	{
        	// assert (initCapacity & -initCapacity) == initCapacity; // power of 2
        	// assert initCapacity >= MINIMUM_CAPACITY;
        	// assert initCapacity <= MAXIMUM_CAPACITY;
        	threshold = (initCapacity * 2)/3;
        	table = new Object[2 * initCapacity];
    	}

    	private static int hash(Object x, int length) 
	{
        	int h = System.identityHashCode(x);
        	// Multiply by -127, and left-shift to use least bit as part of hash
        	return ((h << 1) - (h << 8)) & (length - 1);
    	}

    	private static int nextKeyIndex(int i, int len) 
	{
        	return (i + 2 < len ? i + 2 : 0);
    	}

    	private void resize(int newCapacity) 
	{
        	// assert (newCapacity & -newCapacity) == newCapacity; // power of 2
        	int newLength = newCapacity * 2;
		
		Object[] oldTable = table;
        	int oldLength = oldTable.length;
        	if (oldLength == 2*MAXIMUM_CAPACITY) 
		{ 			// can't expand any further
            		if (threshold == MAXIMUM_CAPACITY-1)
                		throw new IllegalStateException("Capacity exhausted.");
            		threshold = MAXIMUM_CAPACITY-1;  // Gigantic map!
            		return;
        	}

        	if (oldLength >= newLength)
	            	return;

		Object[] newTable = new Object[newLength];

		// NB since capacity = 0.5*length, this means that resize occurs when 
		// we reach 2/3 capacity.
        	threshold = newLength / 3;
	
        	for (int j = 0; j < oldLength; j += 2) 
		{
            		Object key = oldTable[j];
            		if (key != null) 
			{
                		Object value = oldTable[j+1];
                		oldTable[j] = null;
                		oldTable[j+1] = null;
                		int i = hash(key, newLength);
                		while (newTable[i] != null)
                    			i = nextKeyIndex(i, newLength);
                		newTable[i] = key;
                		newTable[i + 1] = value;
            		}
        	}
        	table = newTable;
    	}
}
