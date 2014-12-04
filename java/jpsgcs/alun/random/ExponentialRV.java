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
 This class provides variates from the exponential random variable
 with specified rate.
 The method used is inversion.
*/
public class ExponentialRV extends RandomVariable implements InverseDistribution
{
	static { className = "ExponentialRV"; }

/**
 Create an Exponential random variable with rate 1.
*/
	public ExponentialRV()
	{
		this(1.0);
	}

/**
 Create an Exponential random variable with the given rate.
 Rate is 1/mean.
*/
	public ExponentialRV(double rate)
	{
		set(rate);
		setMethod(new Inversion(this));
	}

/**
 Resets the rate parameter of this random variable.
*/
	public void set(double rate)
	{
		if (rate <= 0)
			throw new ParameterException("Exponential paramter must be positive");
		l = rate;
	}

/**
 Provides the inverse of the distribution function as required by the inversion method.
*/
	public double inverseDistribution(double u)
	{
		return -Math.log(u)/l;
	}

	private double l = 1.0;
}
