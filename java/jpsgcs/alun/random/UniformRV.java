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
 Class which generates uniform random variates.
 The method used is a simple linear transformation of 
 the output of the random engine.
 It implements the density function interface so that it can be used as an
 envelope for generating other distributions.
*/
public class UniformRV extends RandomVariable implements Envelope
{
	static { className = "UniformRV"; }

/**
 Constructs a uniform random variable with default range (0,1).
*/
	public UniformRV()
	{
		this(0,1);
	}

/**
 Constructs a random variable which generates observations 
 uniformly in the given range.
*/
	public UniformRV(double minimum, double maximum)
	{
		set(minimum,maximum);
	}

/**
 Resets the range parameters of the random variable.
*/
	public void set(double minimum, double maximum)
	{
		if (minimum >= maximum)
			throw new ParameterException("Uniform minimum must be less than maximum");
		min = minimum;
		max = maximum;
		range = maximum-minimum;
		invrange = 1/range;
	}
/**
 Return the next variate.
 Overrides the standard method to return a linear function of
 the value given by the underlying random engine.
*/
	public double next()
	{
		return min + range * U();
	}

/**
 Returns the density function.
*/
	public double densityFunction(double x)
	{
		return x >= min && x <= max ? invrange : 0;
	} 

/**
 Returns a new point and its density.
*/
	public Point nextPoint()
	{
		double x = next();
		return new Point(x,densityFunction(x));
	}

	private double min = 0;
	private double max = 0;
	private double range = 1;
	private double invrange = 0;
}
