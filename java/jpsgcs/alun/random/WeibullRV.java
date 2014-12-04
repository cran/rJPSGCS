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
 This class delivers replicates from a Weibull distribution.
 The method used is inversion.
*/
public class WeibullRV extends RandomVariable implements InverseDistribution
{
	static { className = "WeibullRV"; }

/**
 Creates a new Weibull random variable with parameter 1.
*/
	public WeibullRV()
	{
		this(1.0);
	}

/**
 Creates a new Weibull random variable with the given parameter.
*/
	public WeibullRV(double beta)
	{
		set(beta);
		setMethod(new Inversion(this));
	}

/**
 Sets the beta parameter.
*/
	public void set(double beta)
	{
		if (beta <= 0) 
			throw new ParameterException("Weibull paramter must be positive");
		b = 1/beta;
	}

/**
 Implements an inverse distribution function as required by the 
 inversion method.
*/
	public double inverseDistribution(double u)
	{
		return Math.pow(-Math.log(u),b);
	}

	private double b = 1.0;
}
