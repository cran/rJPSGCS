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
 This class provides realisations from the geometric 
 distribution.
 The method used is to take the integer part of an Exponential
 random variable.
 See Ripley's Stochastic Simulation, page 77.
*/
public class GeometricRV extends IntegerValuedRV
{
	static { className = "GeometricRV"; }

/**
 Creates a new Geometric random variable with success probability 0.5.
*/
	public GeometricRV()
	{
		this(0.5);
	}

/**
 Creates a new Geometric random variable with the given success probability.
*/
	public GeometricRV(double p)
	{
		set(p);
	}

/**
 Sets the success probability to the given value.
*/
	public void set(double p)
	{

		if (p < 0 || p > 1)
			throw new ParameterException("Geometric success probability must be in [0,1]");
		X = new ExponentialRV(-Math.log(1-p));
	}

/**
 Returns the next waiting time to success.
*/
	public int nextI()
	{
		return 1 + (int) X.next();
	}

	private RandomVariable X = null;
}
