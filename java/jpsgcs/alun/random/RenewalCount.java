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
 This class represents the state of a renewal process.
*/
public class RenewalCount extends Count
{
/**
 This parameter is the time of the last event.
*/
	public double last = 0;

/**
 Creates a new state with the given time and count.
*/
	public RenewalCount(double t, double l, int c)
	{
		super(t,c);
		last = l;
	}

/**
 Returns a string representation of the process.
*/
	public String toString()
	{
		return "{"+time+","+last+","+count+"}";
	}
}
