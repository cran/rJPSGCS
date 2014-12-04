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
 This class implements sampling with replacement.
 It delegates to a uniform integer random variable.
*/
public class IntWithReplacement extends IntSampling
{
	static { className = "IntWithReplacement"; }
	
/**
 Create a new sampling scheme with default population size 0.
*/
	public IntWithReplacement()
	{
		this(0);
	}

/**
 Creates a new sampling scheme sampling from 0 to n-1 inclusive.
*/
	public IntWithReplacement(int n)
	{
		set(n);
	}

/**
 Creates a new sampling scheme sampling from 0 to p.length -1
 with probabilities proportional to the positive weights
 given in p.
*/
	public IntWithReplacement(double[] p)
	{
		I = new IntegerValuedRV(p);
	}

/**
 Sets the population size.
*/
	public void set(int n)
	{
		I = new UniformIntegerRV(0,n-1);
	}

/**
 Does nothing. Included only for compatibility with sampling
 without replacement.
*/
	public void restart()
	{
	}

/**
 Returns the next sampled integer.
*/
	public int nextI()
	{
		return I.nextI();
	}

/**
 Returns an array of the next k sampled integers.
*/
	public int[] nextI(int k)
	{
		int[] x = new int[k];
		for (int i=0; i<x.length; i++)
			x[i] = I.nextI();
		return x;
	}

	private IntegerValuedRV I = null;
}
