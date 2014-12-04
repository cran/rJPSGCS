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
 This degenerate random variable always yeilds the same fixed
 value. This is just to allow us to use a constant when a random
 variable is called for.
*/
public class DegenerateRV extends RandomVariable
{
	static { className = "DegenerateRV"; }

/**
 Default constructor sets the return value to zero.
*/
	public DegenerateRV()
	{
		this(0);
	}
	
/**
 Creates a new degenerate random variable that always return the
 specified value.
*/
	public DegenerateRV(double d)
	{
		set(d);
	}

/**
 Sets the return value.
*/
	public void set(double d)
	{
		x = d;
	}

/**
 Returns the next value.
*/
	public double next()
	{
		return x;
	}

	private double x = 0;
}
