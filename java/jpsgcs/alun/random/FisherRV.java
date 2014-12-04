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
 This class provides replicates from Fisher's distribution.
 The method is to transform a Beta random variable.
*/
public class FisherRV extends RandomVariable
{
	static { className = "FisherRV"; }

/**
 Creates a new Fisher random variable with parameters 1 and 1.
*/
	public FisherRV()
	{
		this(1,1);
	}

/**
 Creates a new Fisher random variable with the given parameters.
*/
	public FisherRV(double mu, double nu)
	{
		set(mu,nu);
	}

/**
 Sets the parameters to the given values.
*/
	public void set(double mu, double nu)
	{
		if (mu <= 0 || nu <= 0)
			throw new ParameterException("Fisher's distribution parameters must be positive");
		m = mu;
		n = nu;
		X = new BetaRV(m/2,n/2);
	}

/**
 Returns the next replicate.
*/
	public double next()
	{
		double x = X.next();
		return n*x / m / (1-x);
	}
	
	private BetaRV X = null;
	private double m = 1;
	private double n = 1;
}
