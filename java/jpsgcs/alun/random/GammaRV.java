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
 This class provides simulation from the Gamma distribution.
 The methods used are Ahrens Dieter when the shape parameter 
 is less than or equal to 1, and Cheng Feast when it is
 greater than 1.
 See Ripley's Stochastic Simulation page 88.
*/
public class GammaRV extends RandomVariable
{
	static { className = "GammaRV"; }

/**
 Creates a new Gamma random variable with shape parameter 1
 and rate parameter 1.
*/
	public GammaRV()
	{
		this(1.0);
	}

/**
 Creates a new Gamma random variable with given shape parameter
 and rate parameter 1.
*/
	public GammaRV(double shape)
	{
		this(shape, 1);
	}

/**
 Creates a new Gamma random variable with the given shape and
 rate parameters.
*/
	public GammaRV(double shape, double rate)
	{
		set(shape,rate);
	}

/**
 Set the shape and rate parameters of the distribution.
*/
	public void set(double shape, double rate)
	{
		if (shape <= 0)
			throw new ParameterException("Gamma shape parameter must be positive");
		if (rate <= 0)
			throw new ParameterException("Gamma rate parameter must be positive");
		a = shape;
		b = 1.0/rate;

		if (a <= 1.0)
			setMethod(new AhrensDieter(a));
		else
			setMethod(new ChengFeast(a));
	}
		
/**
 Provides the next replicate.
*/
	public double next()
	{
		return getMethod().apply()*b;
	}

	private double a = 1;
	private double b = 1;
}
