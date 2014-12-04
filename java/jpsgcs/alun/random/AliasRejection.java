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

import java.util.Stack;

/**
 This class implements the general alias rejection method.
*/
public class AliasRejection extends GenerationMethod implements IntegerMethod
{
/**
 Creates a new alias rejection scheme to generate values from 0 to pp.length-1
 with the probabilities given in pp.
 There is some overhead in setting this up.
*/
	public AliasRejection(double[] pp)
	{
		int n = pp.length;
		I = new UniformIntegerRV(0,n-1);
		q = new double[n];
		a = new int[n];

		Stack<Integer> lo = new Stack<Integer>();
		Stack<Integer> hi = new Stack<Integer>();
		
		double tot = 0;
		
		for (int i=0; i<n; i++)
			tot += pp[i];

		for (int i=0; i<n; i++)
		{
			q[i] = n*pp[i]/tot;
			if (q[i] < 1.0)
				lo.push(new Integer(i));
			else
				hi.push(new Integer(i));
		}

		while (!lo.isEmpty())
		{
			int j = lo.pop().intValue();
			
			if (!hi.isEmpty())
			{
				int k = hi.peek().intValue();
				a[j] = k;
				q[k] -= 1-q[j];
				if (q[k] <= 1.0)
					lo.push(hi.pop());
			}
		}
	}

/**
 Applies the method and returns the result as a double.
*/
	public double apply()
	{
		return applyI();
	}

/**
 Applies the method and returns the result as an integer.
*/
	public int applyI()
	{
		int i = I.nextI();
		return U() <= q[i] ? i : a[i];
	}

	private UniformIntegerRV I = null;
	private double[] q = null;
	private int[] a = null;
}
