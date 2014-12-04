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
 This class represents the state of two dimensional
 stochastic process.
*/
public class Locus2D extends Locus
{
/**
 The y coordinate in space of the state.
*/
	public double y = 0;

/**
 Creates a new stochastic state with time and location set
 by the given parameters.
*/
	public Locus2D(double time, double x, double y)
	{
		super(time,x);
		this.y = y;
	}

/**
 Returns a string representation of the state.
*/
	public String toString()
	{
		return "{"+time+","+x+","+y+"}";
	}
}
