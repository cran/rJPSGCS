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
 This class creates a mixture of general exponential random variables.
 Each random variable is chosen with probability proportional to the 
 area under the curve.
*/
public class ExponentialMixture extends Mixture implements Envelope
{
	public ExponentialMixture(GeneralExponentialRV[] vars)
	{
		super(areasUnder(vars),vars);
	}

/**
 Sets the mixture of general exponentials.
*/
	public void set(GeneralExponentialRV[] vars)
	{
		super.set(areasUnder(vars),vars);
	} 

/**
 Return the next generated value and the value of the density corresponding.
*/
	public Point nextPoint()
	{
		return ((GeneralExponentialRV)X[A.applyI()]).nextPoint();
	}

/**
 Returns the array of areas under the component exponentials.
*/
	private static double[] areasUnder(GeneralExponentialRV[] vars)
	{
		double[] p = new double[vars.length];
		for (int i=0; i<vars.length; i++)
			p[i] = vars[i].areaUnder();
		return p;
	}
}
