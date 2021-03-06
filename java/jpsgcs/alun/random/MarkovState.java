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
 This class represents the states of a Markov chain.
*/
public class MarkovState extends Count
{
/**
 The current state of the chain.
*/
	public int state = 0;

/**
 Create a new Markov state which is in state s after
 c transitions.
*/
	public MarkovState(int c, int s)
	{
		super(c,c);
		state = s;
	}

/**
 Returns a string representation of the state.
*/
	public String toString()
	{
		return "{"+count+","+state+"}";
	}
}
