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
 This class delivers replicates from a Poisson distribution.
 The method used is inversion.
*/
public class PoissonRV extends IntegerValuedRV implements InverseDistribution
{
	static { className = "PoissonRV"; }

/**
 Creates a new Poisson random variable with rate 1.
*/ 
	public PoissonRV()
	{
		this(1);
	}

/**
 Creates a new Poisson random variable with the given rate.
*/
	public PoissonRV(double rate)
	{
		set(rate);
		setMethod(new Inversion(this));
	}

/**
 Sets the rate parameter to the given value.
*/
	public void set(double rate)
	{
		if (rate <= 0)
			throw new ParameterException("Poisson rate must be positive");
		l = rate;
		mode = (int) l;
		pmode = mode * Math.log(l) - l;
		for (int i=mode; i>0; i--)
			pmode -= Math.log(i);
		pmode = Math.exp(pmode);
	}

/**
 Implements the inverse distribution function as required by the inversion method.
 The values of the range are permuted so that the most probable values are tried 
 first, in order to speed up the function.
*/
	public double inverseDistribution(double u)
	{
		int i = mode;
		int j = mode;
		int k = mode;
		double pi = pmode;
		double pj = pmode;
		double q = pmode;
		
		while (u > q)
		{
			j++;
			pj *= l/j;
			q += pj;
			k = j;
			
			if (u>q && i>0)
			{
				pi *= i/l;
				i--;
				q += pi;
				k = i;
			}
		}
		
		return k;
	}

	private double l = 1;
	private int mode = 0;
	private double pmode = 0;
}
