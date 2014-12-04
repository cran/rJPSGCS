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
 This class provides replicates from the Bernoulli distribution.
 The method used is simply to compare a standard Uniform with
 the required probability and return 1 if it is less.
*/
public class BernoulliRV extends IntegerValuedRV
{
	static { className = "BernoulliRV"; }

/** 
 Creates a Bernoulli random variable with success probability 0.5.
*/
	public BernoulliRV()
	{
		this(0.5);
	}

/**
 Creates a Bernoulli random variable with the specified probabiliy
 of success.
 Success = 1, failure = 0.
*/
	public BernoulliRV(double prob)
	{
		set(prob);
	}

/**
 Set the success probability to the given value.
*/
	public void set(double prob)
	{
		if (p < 0 || p > 1)
			throw new ParameterException("Bernoulli success probability must be in [0,1]");
		p = prob;
	}

/**
 Return the next value. P(1) = p.
*/
	public int nextI()
	{
		return nextB() ? 1 : 0;
	}
	
/**
 Return the next value as boolean. P(true) = p.
*/
	public boolean nextB()
	{
		return U() <= p;
	}

	private double p = 0;
}
