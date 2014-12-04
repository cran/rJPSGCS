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
 This is the superclass of all objects that define
 the state of a stochastic process.
*/
public class StochasticState
{
/**
 The point in time of the state.
*/
	public double time = 0;

/**
 Creates a new stochastic process with time set 
 to the given value.
*/
	public StochasticState(double t)
	{
		time = t;
	}

/**
 Returns a string representation of the state.
*/
	public String toString()
	{
		return "{"+time+"}";
	}
}
