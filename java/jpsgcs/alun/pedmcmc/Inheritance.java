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


package jpsgcs.alun.pedmcmc;

import jpsgcs.alun.markov.Variable;
import java.util.Set;
import java.util.LinkedHashSet;

public class Inheritance extends Variable
{
	public Inheritance()
	{
		super(2);
	}

	public void setStates(int[] x)
	{
	}
	
	public void flip()
	{
		state = 1-state;
	}

	public void remember()
	{
		memo = state;
	}

	public void recall()
	{
		state = memo;
	}

	public String toString()
	{
		return "I("+super.toString()+")"+"["+state+"]";
	}

	private int memo = 0;
}
