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
 This class implements a Markov chain on a finite 
 number of states where the transitions can be
 described as a square matrix.
*/
public class FiniteStateMC extends MarkovChain
{
/**
 Creates a finite state Markov chain with the given 
 transition matrix.
*/
	public FiniteStateMC(double[][] matrix)
	{
		set(matrix);
	}

/**
 Sets the transition matrix.
*/
	public void set(double[][] matrix)
	{
		for (int i=0; i<matrix.length; i++)
		{
			if (matrix.length != matrix[i].length)
				throw new ParameterException("FiniteMarkovChain matrix must be square");
			double tot = 0;
			for (int j=0; j<matrix[i].length; j++)
			{
				if (matrix[i][j] < 0)
					throw new ParameterException("FiniteMarkovChain can't have negative transitions");
				tot += matrix[i][j];
			}
			if (tot <= 0)
				throw new ParameterException("FiniteMarkovChain can't have all zero row of transtitions");
		}

		A = new AliasRejection[matrix.length];
		for (int i=0; i<A.length; i++)
			A[i] = new AliasRejection(matrix[i]);
	}

/**
 Advances the chain one step.
*/
	public void next()
	{
		state = A[state].applyI();
		count++;
	}

	private AliasRejection[] A = null;

/**
 Test main.
*/
	public static void main(String[] args)
	{
		double[][] matrix = 
		{
			{0.5, 0.5, 0.0, 0.0, 0.0},
			{0.0, 0.5, 0.5, 0.0, 0.0},
			{0.0, 0.0, 0.5, 0.5, 0.0},
			{0.0, 0.0, 0.0, 0.5, 0.5},
			{0.5, 0.0, 0.0, 0.0, 0.5}
		};

		MarkovChain M = new FiniteStateMC(matrix);
		for (int i=0; i<10; i++)
		{
			M.next();
			System.out.println(M.getState());
		}
	}
}
