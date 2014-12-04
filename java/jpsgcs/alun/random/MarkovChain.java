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
 This is the superclass of the various ways of implementing 
 Markov chains.
*/
abstract public class MarkovChain extends DiscreteStochasticProcess
{
/**
 Returns the current state.
 The runtime type of the state is MarkovState.
*/
	public StochasticState getState()
	{
		return new MarkovState(count,state);
	}

/**
 Sets the current state.
 The state must be a MarkovState.
*/
	public void setState(StochasticState s)
	{
		MarkovState c = (MarkovState)s;
		count = c.count;
		time = count;
		state = c.state;
	}

/**
 Advances the chain n steps.
*/
	public void next(int n)
	{
		for (int i=0; i<n; i++)
			next();
	}

/**
 Advances the chain by t time units.
*/
	public void advance(double t)
	{
		time += t;
		int n = (int)(time-count);
		next(n);
	}

	protected double time = 0;
	protected int count = 0;
	protected int state = 0;
}
