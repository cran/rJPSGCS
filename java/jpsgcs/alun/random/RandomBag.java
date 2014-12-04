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


package jpsgcs.alun.random;

import java.util.Vector;
import java.util.Collection;
import java.util.Iterator;

/**
 This class mimics a bag into which objects are put 
 and then drawn out at random.
*/
public class RandomBag extends RandomObject
{
/**
 Creates a  new empty bag.
*/
	public RandomBag()
	{
		v = new Vector<Object>(); 
	}

/**
 Adds the object to the bag.
*/
	public synchronized void add(Object o)
	{
		v.addElement(o);
	}

/**
 Adds an array of objects to the bag.
*/
	public synchronized void addAll(Object[] o)
	{
		for (int i=0; i<o.length; i++)
			v.addElement(o[i]);
	}

	public synchronized void addAll(Collection s)
	{
		for (Iterator i=s.iterator(); i.hasNext(); )
			add(i.next());
	}

/**
 Removes the object from the bag if it contained in it.
*/
	public synchronized void remove(Object o)
	{
		v.removeElement(o);
	}

/**
 Draws an object from the bag at random and replaces it in the bag.
 Returns null if the bag is empty.
*/
	public synchronized Object next()
	{
		if (v.isEmpty())
			return null;
		else
			return v.elementAt(I());
	}

/**
 Draws an object at random from the bag and removes it from the bag.
 Returns null if the bag is empty.
*/
	public synchronized Object draw()
	{
		if (v.isEmpty())
			return null;
		else
		{
			int j = I();
			Object b = v.elementAt(j);
			v.removeElementAt(j);
			return b;
		}
	}

/**
 Returns true iff the bag contains no objects.
*/
	public boolean isEmpty()
	{
		return v.isEmpty();
	}

/**
 Removes all the objects in the bag.
*/
	public void clear()
	{
		v.removeAllElements();
	}

/**
 Returns the number of objects currently in the bag.
*/
	public int size()
	{
		return v.size();
	}

/**
 Returns the contents of the bag in an array.
*/
	public Object[] contents()
	{
		return v.toArray();
	}

	private Vector<Object> v = null;

/**
 Returns the index of a random object in the bag.
*/
	private int I()
	{
		int j = 0;
		do 
		{ 
			j = (int)(U()*v.size()); 
		}
		while (j==v.size());
		return j;
	}

/**
 Test main.
*/
	public static void main(String[] args)
	{
		try
		{
			RandomBag b = new RandomBag();

			for (int i=0; i<10; i++)
				b.add(new Integer(i));
	
			for (int i=0; i<20; i++)
				System.out.print(b.next()+" ");
			System.out.println();
	
			for (int i=0; i<15; i++)
				System.out.print(b.draw()+" ");
			System.out.println();
		}
		catch (Exception e)
		{
			System.out.println("Caught in RandomBag:main()");
			e.printStackTrace();
		}
	}
}
