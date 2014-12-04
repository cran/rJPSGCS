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
 This is an abstract class for random variables that
 return integer values. 
 The next() method is overwritten to return the value 
 of nextI() as a double. The nextI() method by default
 returns the result of applying an integer method.
*/
public class IntegerValuedRV extends RandomVariable
{
	static { className = "IntegerValuedRV"; }

/**
 A null constructor for subclassing only.
*/
	public IntegerValuedRV()
	{
	}


/**
 Sets the method to be alias rejection returning integers
 0 to p.length -1 with probabilities proportional to the
 values given in p. The values need to be non negative.
*/
	public IntegerValuedRV(double[] p)
	{
		set(p);
	}

	public void set(double[] p)
	{
		setMethod(new AliasRejection(p));
	}

/**
 Returns the value of nextI() as a double.
*/
	public final double next()
	{
		return nextI();
	}

/**
 Returns the result of applying an integer transformation method.
*/
	public int nextI()
	{
		return ((IntegerMethod)getMethod()).applyI();
	}
}
