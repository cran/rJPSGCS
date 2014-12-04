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
 This class implements Metropolis's method of proposal
 and accptance updates for a Markov chain.
*/
public class Metropolis extends GenerationMethod
{
/**
 Creates a new Metropolis update scheme for the given
 Markov chain which must implement the Metropolitan interface.
*/
	public Metropolis(Metropolitan Z)
	{
		X = (MarkovChain) Z;
		Y = Z;
	}
	
/**
 Apply the method.
 Returns 0.
*/
	public double apply()
	{
		int t = Y.proposal();
		if (U() < Y.acceptance(t))
			X.state = t;
		return 0;
	}
	
	private MarkovChain X = null;
	private Metropolitan Y = null;
}
