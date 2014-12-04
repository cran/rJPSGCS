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
 This counts the number 
 of births and deaths in a birth and death process.
*/
public class PopulationCount extends Count
{
/**
 Counts the number of births.
*/
	public int births = 0;
/**
 Counts the the number of deaths.
*/
	public int deaths = 0;

/**
 Creates a new counter with the given time, number of births 
 and number of deaths.
*/
	public PopulationCount(double t, int b, int d)
	{
		super(t,b+d);
		births = b;
		deaths = d;
	}

/**
 Returns a string representation of the state.
*/
	public String toString()
	{
		return "{"+time+","+count+","+births+","+deaths+"}";
	}
}
