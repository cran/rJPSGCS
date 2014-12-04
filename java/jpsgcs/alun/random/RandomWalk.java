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
 This class simlulates a random walk on the integers.
*/
public class RandomWalk extends MarkovChain
{
	static { className = "RandomWalk"; }

/**
 Creates a new symetric random walk.
*/
	public RandomWalk()
	{
		this(0.5);
	}

/**
 Creates a new random walk with P(+1) = p, P(-1) = 1-p.
*/
	public RandomWalk(double p)
	{
		set(p);
	}

/**
 Sets P(+1) to the given value.
*/
	public void set(double p)
	{
		if (p < 0 || p > 1)
			throw new ParameterException("RandomWalk probability must be in [0,1]");
		prob = p;
	}

/**
 Advances the walk one step.
*/
	public void next()
	{
		if (U() < prob)
			state++;
		else
			state--;
		count++;
		if (count > time)
			time = count;
	}

	private double prob = 0.5;
}
