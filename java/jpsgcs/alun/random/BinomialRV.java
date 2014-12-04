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
 This method delivers realisations from the Binomial distribution.
 Two methods are used. For small number of trials, Alias rejection is 
 used, for larger numbers of trials the Ahrens method is used.
 See Ripley's Stochastic Simulation, page 78.
*/
public class BinomialRV extends IntegerValuedRV
{
	static { className = "BinomialRV"; }

/**
 Creates a new Binomial random variable with one trial and 
 success probability 0.5, ie Bernoulli(0.5).
*/
	public BinomialRV()
	{
		this(1,0.5);
	}

/**
 Creates a new Binomial random variable with the given number 
 of trials and the given success probability.
*/
	public BinomialRV(int n, double p)
	{
		set(n,p);
	}

/**
 Sets the number of trials and success probability.
*/
	public void set(int n, double p)
	{
		if (n <= 0)
			throw new ParameterException("Number of Binomial trials must be positive");
		if (p < 0 || p > 1)
			throw new ParameterException("Binomial success probability must be in [0,1]");

		if (n > threshold)
			setMethod(new AhrensBinomial(n,p));
		else
		{
			double[] ps = new double[n+1];
			int x = (int) (n*p);
	
			ps[x] = 0.0;
			for (int i=1; i<=x; i++)
				ps[x] += Math.log(n-i+1)-Math.log(i);
			ps[x] += x*Math.log(p) + (n-x)*Math.log(1-p);
			ps[x] = Math.exp(ps[x]);
	
			for (int i=x+1; i<=n; i++)
				ps[i] = ps[i-1] * p/(1-p) * (n-i+1)/i;
				
			for (int i=x-1; i>=0; i--)
				ps[i] = ps[i+1] * (1-p)/p * (i+1)/(n-i);
	
			setMethod(new AliasRejection(ps));
		}
	}

	private static final int threshold = 10000;
}
