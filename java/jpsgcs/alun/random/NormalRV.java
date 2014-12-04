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

public class NormalRV extends GaussianRV implements LogConcave 
//public class NormalRV extends GaussianRV implements DifferentiableLogConcave
{
	static { className = "NormalRV"; }

	public NormalRV()
	{
		super(0,1);
		//double[] x = {-4,-3,-2,-1,-0.5,0,0.5,1,2,3,4};
		double[] x = {-3,0,3};
		//setMethod(new ExponentialSwitching(this,x));
		setMethod(new AdaptiveRejection(this,x));
	}

	public double densityFunction(double x)
	{
		return Math.exp(-x*x/2);
	}

	public double logDensityFunction(double x)
	{
		return -x*x/2;
	}

	public double lowerBound()
	{
		return Double.NEGATIVE_INFINITY;
	}

	public double upperBound()
	{
		return Double.POSITIVE_INFINITY;
	}

	public double derivativeLogDensity(double x)
	{
		return -x;
	}
}
