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

public class Sampling extends RandomObject
{
	public Sampling()
	{
		set(new Object[0], new double[0]);
	}

/**
 Creates a new scheme to sample from the given population.
 Sampling is with or without replacement according to the value of the boolean:
 true => with, false => without.
*/
	public Sampling(Object[] population, boolean withReplacement)
	{
		set(population, withReplacement);
	}
	
/**
 Creates a new scheme for sampling with replacement from the 
 given population with probabilities proportional to the weights 
 given in the double array.
*/
	public Sampling(Object[] population, double[] weights)
	{
		set(population, weights);
	}

/**
 Sets the population and the replacement strategy.
*/
	public void set(Object[] population, boolean withReplacement)
	{
		p = population;
		if (withReplacement)
			I = new IntWithReplacement(p.length);
		else
			I = new IntWithoutReplacement(p.length);
		w = null;
	}

/**
 Sets the population and the weights for sampling with replacement.
*/
	public void set(Object[] population, double[] weights)
	{
		p = population;
		w = weights;
		if (weights.length > 0)
			I = new IntWithReplacement(weights);
	}

/**
 Adds an object to be sampled with the given weight.
 Can only be done if the sampling is currently set up as
 a weighted sampling.
*/
	public void add(Object x, double wt)
	{
		if (w == null)
			throw new ParameterException("Sampling is unweighted");
		
		Object[] y = new Object[p.length+1];
		double[] t = new double[p.length+1];
		for (int i=0; i<p.length; i++)
		{
			y[i] = p[i];
			t[i] = w[i];
		}
		y[p.length] = x;
		t[p.length] = wt;
		set(y,t);
	}

/**
 Replaces all sampled objects back in the population and restarts
 the sampling scheme.
*/
	public void restart()
	{
		I.restart();
	}

/**
 Returns the next sampled object, or null if there are no more left.
*/
	public Object next()
	{
		int i = I.nextI();
		if (i < 0)
			return null;
		else
			return p[i];
	}

/**
 Returns the next k sampled objects. Will pad the array with nulls if
 there are not enough objects to sample.
*/
	public Object[] next(int n)
	{
		Object[] x = new Object[n];
		for (int i=0; i<x.length; i++)
			x[i] = next();
		return x;
	}

	private Object[] p = null;
	private IntSampling I = null;
	private double[] w = null;

/**
 Test main.
*/
	public static void main(String[] args)
	{
		int n = 20;

		Integer[] b = new Integer[10];
		for (int i=0; i<b.length; i++)
			b[i] = new Integer(i);

		Sampling t = new Sampling(b,false);
		for (int i=0; i<n; i++)
			System.out.print(t.next()+" ");
		System.out.println();
		t.restart();
		for (int i=0; i<n; i++)
			System.out.print(t.next()+" ");
		System.out.println();

		t = new Sampling(b,true);
		for (int i=0; i<n; i++)
			System.out.print(t.next()+" ");
		System.out.println();
		t.restart();
		for (int i=0; i<n; i++)
			System.out.print(t.next()+" ");
		System.out.println();

		double[] p = new double[b.length];
		for (int i = 0; i<p.length; i++)
			p[i] = 1;
		p[0] = 9;
		t = new Sampling(b,p);
		for (int i=0; i<n; i++)
			System.out.print(t.next()+" ");
		System.out.println();
		t.restart();
		for (int i=0; i<n; i++)
			System.out.print(t.next()+" ");
		System.out.println();
	}
}
