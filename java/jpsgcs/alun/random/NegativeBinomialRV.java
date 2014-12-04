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
 This class provides replicates from the Negative Binomial distribution.
 The method used is inversion.
*/
public class NegativeBinomialRV extends IntegerValuedRV implements InverseDistribution
{
	static { className = "NegativeBinomialRV"; }

/**
 Creates a new Negative Binomial random variable waiting for 1 success,
 with success probability 0.5, that is equivalent to Geometric(0.5).
*/
	public NegativeBinomialRV()
	{
		this(1,0.5);
	}
	
/**
 Creates a new Negative Binomial random variable waiting for the given
 number of successes, with given success probability.
*/
	public NegativeBinomialRV(int nsuccess, double psuccess)
	{
		set(nsuccess,psuccess);
		setMethod(new Inversion(this));
	}

/**
 Sets the number of successes to wait for and the probability of 
 a success.
*/
	public void set(int nsuccess, double psuccess)
	{
		if (nsuccess <= 0)
			throw new ParameterException("Negative Binomial number of successes must be positive");
		if (psuccess < 0 || psuccess > 1)
			throw new ParameterException("Negative Binomial probability of success must be in [0,1]");
		k = nsuccess;
		p = psuccess;
		m = (int) (k/p);
		pm = k*Math.log(p) + (m-k)*Math.log(1-p);
		for (int i=1; i<k; i++)
			pm += Math.log((m-i)/((double)(k-i)));
		pm = Math.exp(pm);
	}

/**
 Implements the inverse distribution function as required by the inversion method.
 The order the range is searched is permuted to put the most probable terms first
 and so speed the search.
*/
	public double inverseDistribution(double u)
	{
		int i = m;
		int j = m;
		int l = m;
		double pi = pm;
		double pj = pm;
		double q = pm;

		while (u > q)
		{
			j++;
			pj *= (1-p)*(j-1)/((double)(j-k));
			q += pj;
			l = j;
			
			if (u>q && i>k)
			{
				pi *= (i-k)/((double)(i-1))/(1-p);
				i--;
				q += pi;
				l = i;
			}
		}
		
		return l;
	}

	private double p = 0;
	private int k = 0;
	private int m = 0;
	private double pm = 0;
}
