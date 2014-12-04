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


package jpsgcs.alun.markov;

/**
 A Variable can take only positive integer values, and
 setStates can only restrict the possible states among those
 that were possible on construction.

 No checks are made when setting the current state or state space
 that the given values are valid. If checking is needed use
 CarefulVariable
*/
public class Variable implements Comparable<Variable>
{
/**
 Create a new Variable with nn states indexed 0 to nn-1.
*/
	public Variable(int nn)
	{
		m = new int[nn];
		v = new int[nn];
		for (int i=0; i<v.length; i++)
			m[i] = v[i] = i;

		id = count++;
	}

	public Variable(int[] s)
	{
		setStates(s);
		id = count++;
	}

	public int compareTo(Variable x)
	{
		return id < x.id ? -1 : id > x.id ? 1 : 0 ;
	}

	public String toString()
	{
		return ""+id;
	}

/**
 Initialize the variable.
*/
	public void init()
	{
		state = -1;
	}

/**
 Advance the variable to its next state.
*/
	public boolean next()
	{
		return ++state < v.length;
	}

/**
 Returns the number of states the variable takes.
*/
	public int getNStates()
	{
		return v.length;
	}

/**
 Returns the current state.
*/
	public int getState()
	{
		return v[state];
	}

/**
 Sets the current state to the given value. No check is made 
 to ensure that the value is valid.
*/
	public void setState(int i)
	{
		state = m[i];
	}

/**
 Returns true if the given integer represents a valid current state
 for the variable.
*/
	public boolean hasState(int i)
	{
		return i < m.length && m[i] >= 0;
	}

/**
 Sets the states to the given array of values.
*/
	public void setStates(int[] x)
	{
		int max = 0;
		for (int i=0; i<x.length; i++)
			if (max < x[i])
				max = x[i];
		m = new int[max+1];

		for (int i=0; i<m.length; i++)
			m[i] = -1;

		v = new int[x.length];
		for (int i=0; i<v.length; i++)
		{
			v[i] = x[i];
			m[v[i]] = i;
		}
	}

// 	Private data and methods.

	public int state = 0;
	protected int[] v = null;
	protected int[] m = null;

	private int id = 0;
	private static int count = 0;
}
