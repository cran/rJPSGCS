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
 This class produces integers sampled without replacement from 0 to n-1.
*/
public class IntWithoutReplacement extends IntSampling
{
	static { className = "IntWithoutReplacement"; }

/**
 Default constructor.
*/
	public IntWithoutReplacement()
	{
		this(10);
	}

/**
 Constructs a new sampling scheme with limits of sampling from 
 0 to n-1 inclusive.
*/
	public IntWithoutReplacement(int n)
	{
		set(n);
	}

/**
 Sets the limits of sampling from 0 to n-1 inclusive.
*/
	public void set(int n)
	{
		x = new int[n];
		for (int i=0; i<x.length; i++)
			x[i] = i;
		restart();
	}

/**
 Replaces all sampled objects back in the population.
*/
	public void restart()
	{
		next = 0;
	}

/**
 Returns the next sampled integer.
 If there are no more left to be sampled -1 is returned.
*/
	public int nextI()
	{
		if (next >= x.length)
			return -1;

		int j = (int)(next + U()*(x.length - next));
		int t = x[j];
		x[j] = x[next];
		return (x[next++] = t);
	}

/**
 Returns the next k sampled integers. If there are fewer than
 k integers left to be sampled, the array is packed with -1s.
*/
	public int[] nextI(int k)
	{
		int[] y = new int[k];
		for (int i=0; i<y.length; i++)
			y[i] = nextI();
		return y;
	}

	private int[] x = null;
	private int next = 0;
}
