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
 This class simulates a Markov chain with given stationary
 distribution using the Metropolis method.
*/
public class MetropolitanMC extends MarkovChain implements Metropolitan
{
/**
 Creates a new Metropolis scheme for a chain with the stationary
 distribution proportional to the given array.
*/
	public MetropolitanMC(double[] probs)
	{
		set(probs);
		M = new Metropolis(this);
	}

/**
 Sets the ergodic distribution.
*/
	public void set(double[] probs)
	{
		for (int i=0; i<probs.length; i++)
			if (probs[i] < 0)
				throw new ParameterException("MetropolitanMC needs postitive stationary distribution");
		p = probs;
		I = new UniformIntegerRV(0,p.length-1);
	}

/**
 Advances the chain to the next state.
*/
	public void next()
	{
		M.apply();
		count++;
	}

/**
 This method supplies a proposed next state for the Metropolis scheme.
*/
	public int proposal()
	{
		return I.nextI();
	}
	
/**
 This method returns the acceptance probability of the given state.
*/
	public double acceptance(int t)
	{
		return p[t]/p[state];
	}

	private double[] p = null;
	private Metropolis M = null;
	private UniformIntegerRV I = null;

/**
 Test main.
*/
	public static void main(String[] args)
	{
		double[] ergo = {1.0, 5.0, 5.0, 1.0};
		MarkovChain M = new MetropolitanMC(ergo);
		for (int i=0; i<10; i++)
		{
			M.next();
			System.out.println(M.getState());
		}
	}
}
