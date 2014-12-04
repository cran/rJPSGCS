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


package jpsgcs.alun.util;

/**
 A holder for a pair of objects.
*/
public class Pair<E,F>
{
/**
 Creates a new null pair.
*/
	public Pair()
	{
	}

/**
 Creates a new pair for the given two objects.
*/
	public Pair(E a, F b)
	{
		x = a;
		y = b;
	}

/**
 Returns the object specified first in the construtor.
*/
	public E getX()
	{
		return x;
	}

/** 
 Returns the object specified second in the constructor.
*/
	public F getY()
	{
		return y;
	}

/** 
 Returns a string representation of the pair.
*/
	public String toString()
	{
		return x + " " + y;
	}

/**
 The objects are directly accessible.
*/
	public E x = null;
	public F y = null;
}
