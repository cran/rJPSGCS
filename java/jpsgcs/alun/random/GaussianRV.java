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
 This class provides replicates from the Gaussian or Normal distribution.
 The method used is ratio of uniforms.
*/
public class GaussianRV extends RandomVariable implements RatioRegion
{
	static { className = "GaussianRV"; }

/**
 Creates a new Gaussian random variable with mean 0 and variance 1.
*/
	public GaussianRV()
	{
		this(0,1);
	}

/**
 Creates a new Gaussian random variable with given mean and variance.
*/
	public GaussianRV(double mean, double variance)
	{
		set(mean,variance);
		setMethod(new RatioOfUniforms(this,new UniformRV(),new UniformRV(-C1,C1)));
	}

/**
 Sets the mean and variance parameters.
*/
	public void set(double mean, double variance)
	{
		if (variance < 0)
			throw new ParameterException("Gaussian variance must be positive");
		m = mean;
		s = Math.sqrt(variance);
	}

/**
 Provides the next replicate.
*/
	public double next()
	{
		return m + s*getMethod().apply();
	}

/**
 Returns true iff the given point is contained in the appropriate region.
 See Ripley's Stochastic Simulation, page 82.
*/
	public boolean containsInRatioRegion(double x, double y)
	{
		double b = y/x*y/x/4;
		
		if (b < 1-x)
			return true;
		else if (b > C2/x - C3)
			return false;
		else if (b > -Math.log(x))
			return false;
		else
			return true;
	}

	private static final double C1 = Math.sqrt(2/Math.E);
	private static final double C2 = Math.exp(-1.35);
	private static final double C3 = 1+Math.log(C2);
	private double m = 0;
	private double s = 1;
}
