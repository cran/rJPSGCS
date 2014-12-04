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
 This class provides integers uniformly generated in
 a given range.
 The method used is a simple linear transformation of 
 the output of the random engine, forced to be an integer.
*/
public class UniformIntegerRV extends IntegerValuedRV 
{
	static { className = "UniformIntegerRV"; }

/**
 Creates a Uniform Integer random variable returning values
 between -1 and 1, inclusive.
*/
	public UniformIntegerRV()
	{
		this(-1,1);
	}

/**
 Creates a new Uniform Integer random variable returning values
 between the specified maximum and minimum, inclusive.
*/

	public UniformIntegerRV(int minimum, int maximum)
	{
		set(minimum, maximum);
	}

/**
 Sets the minimum and maximum values as specified.
*/
	public void set(int minimum, int maximum)
	{ 
		if (minimum > maximum)
			throw new ParameterException("Uniform Integer random variable minimum must be <= maximum");
		min = minimum;
		range = 1+maximum-minimum;
	}

/**
 Returns the next realisation.
*/
	public int nextI()
	{
		return min + (int)(range*U());
	}

	private int min = 0;
	private int range = 0;
}
