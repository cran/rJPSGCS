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

/**
 This class generates random permutation of integers.
*/
public class RandomPermutation extends RandomObject
{
/**
 Default contstructor which creates a new object to permute 10 integers.
*/
	public RandomPermutation()
	{
		this(10);
	}

/**
 Creates a new object to permute p integers.
*/
	public RandomPermutation(int p)
	{
		I = new IntWithoutReplacement(p);
		set(p);
	}

/**
 Sets the number of integers to permute.
*/
	public void set(int p)
	{
		n = p;
		I.set(n);
	}

/**
 Returns the next premutation as an object.
 The runtime type is int array.
*/
	public Object next()
	{
		return nextI();
	}

/**
 Returns the next permutation as an  int array.
*/
	public int[] nextI()
	{
		I.restart();
		return I.nextI(n);
	}

	private IntWithoutReplacement I = null;
	private int n = 0;

/**
 Test main.
*/
	public static void main(String[] args)
	{
		RandomPermutation p = new RandomPermutation(10);
		for (int i=0; i<10; i++)
		{
			int[] x = (int[])p.next();
			for (int j=0; j<x.length; j++)
				System.out.print(" "+x[j]);
			System.out.println();
		}
	}
}
