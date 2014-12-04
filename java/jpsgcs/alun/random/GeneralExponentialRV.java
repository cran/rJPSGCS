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
 This class provides replicates from the Generalised Exponential distribution.
 The log density of this distribution is a straight line over an arbitrary
 range that cannot be infinite on both sides.
 The method used is inversion.
 The class implements the density function so that it can be used as an 
 envelope.
*/
public class GeneralExponentialRV extends RandomVariable implements InverseDistribution, Envelope
{
	static { className = "GeneralExponentialRV"; }

/**
 Create a new General Exponential random variable that is equivalent to the 
 standard exponential.
*/
	public GeneralExponentialRV()
	{
		this(-1,0,0,Double.POSITIVE_INFINITY);
	}

/**
 Create a new General Exponential random variable defined in log space by
 the straight line with the specified slope and intercept, on the specified
 range.
*/
	public GeneralExponentialRV(double slope, double intercept, double low, double high)
	{
		set(slope,intercept,low,high);
	}

/**
 Sets the parameters of the distribution.
*/
	public void set(double slope, double intercept, double low, double high)
	{
		if (low >= high)
			throw new ParameterException("General Exponential lower bound must be less than upper.");
		if (Double.isInfinite(low) && Double.isInfinite(high))
			throw new ParameterException("General Exponential bounds cannot both be infinite.");
		if (slope > 0 && Double.isInfinite(high))
			throw new ParameterException("General Exponential upper bound cannot be infinite if slope > 0");
		if (slope < 0 && Double.isInfinite(low))
			throw new ParameterException("General Exponential lower bound cannot be infinite if slope < 0");
		if (slope == 0 && (Double.isInfinite(low) || Double.isInfinite(high)))
			throw new ParameterException("General Exponential neither bound can be infinite if slope == 0");
			
		a = slope;
		b = intercept;
		l = low;
		h = high;
		if (a == 0)
		{
			area = Math.exp(b)*(h-l);
			setMethod(new Inversion(new SpecialCase()));
		}
		else	
		{
			lower = Math.exp(a*l);
			range = Math.exp(a*h) - lower;
			area = Math.exp(b)/a * range;
			setMethod(new Inversion(this));
		}
	}

/**
 Implements the inverse distribution as required by the inversion method.
*/
	public double inverseDistribution(double u)
	{
		return Math.log(lower+u*range)/a;
	}

/**
 Implements the envelope interface so that it can be used in rejection.
*/
	public Point nextPoint()
	{
		double x = next();
		return new Point (x, Math.exp(a*x+b));
	}

/**
 Returns the value of the area under the density function.
*/
	public double areaUnder()
	{
		return area;
	}

	private double a = 0;
	private double b = 0;
	private double l = 0;
	private double h = 0;
	private double area = 0;
	private double lower = 0;
	private double range = 0;

	private class SpecialCase implements InverseDistribution
	{
		public double inverseDistribution(double u)
		{
			return l + u*(h-l);
		}
	}
}
