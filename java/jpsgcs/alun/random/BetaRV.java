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
 This class provides replicates from a Beta distribution.
 The method used is to return X/(X+Y) where X and Y 
 are independent Gammas.
*/
public class BetaRV extends RandomVariable
{
	static { className = "BetaRV"; }

/**
 Creates a new Beta random variable with default parameters 1 and 1.
*/
	public BetaRV()
	{
		this(1,1);
	}

/**
 Creates a new Beta random variable with given parameters.
*/
	public BetaRV(double alpha, double beta)
	{
		set(alpha,beta);
	}

/**
 Sets the parameters to the given values.
*/
	public void set(double alpha, double beta)
	{
		if (alpha <= 0 || beta <= 0)
			throw new ParameterException("Beta parameters must be positive");
		X = new GammaRV(alpha);
		Y = new GammaRV(beta);
	}

/**
 Returns the next generated value.
*/
	public double next()
	{
		double x = X.next();
		return x / (x + Y.next());
	}

	private GammaRV X = null;
	private GammaRV Y = null;
}
