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
 Returns a constant integer value.
*/
public class DegenerateIntegerRV extends IntegerValuedRV
{
	static { className = "DegenerateIntegerRV"; }

/**
 Creates a new degenerate distribution that always returns 0.
*/
	public DegenerateIntegerRV()
	{
		this(0);
	}

/**
 Creates a new degenerate distribution that always returns 
 the specified integer value.
*/
	public DegenerateIntegerRV(int i)
	{
		set(i);
	}

/**
 Sets the return value.
*/
	public void set(int i)
	{
		a = i;
	}

/**
 Returns the constant value.
*/
	public int nextI()
	{
		return a;
	}

	private int a = 0;
}
