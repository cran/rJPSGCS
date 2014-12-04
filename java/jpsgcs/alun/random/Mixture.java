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
 This class combines random variables into mixtures.
 A distribution is chosen with a certain probability 
 and a relisation is generated from it. Successive calls
 choose distributions independently.
 Alias rejection is used to choose the random variable.
*/
public class Mixture extends RandomVariable
{
/**
 Generate a new mixture of the given random variables chosen
 with probabilities proportional to the given double values.
*/
	public Mixture(double[] props, RandomVariable[] vars)
	{
		set(props,vars);
	}

/**
 Set the mixture probabilities and the random variables.
*/
	public void set(double[] props, RandomVariable[] vars)
	{
		if (props.length != vars.length)
			throw new ParameterException("Mixture proportion and random variable array lengths must match.");
		if (props.length <1)
			throw new ParameterException("Mixtures must have at least one element");
		for (int i=0; i<props.length; i++)
			if (props[i] < 0)
				throw new ParameterException("Mixture proportions must be positive");
		X = vars;
		A = new AliasRejection(props);
	}

/**
 Choose a random variable at random and return the next realisation
 from it.
*/
	public double next()
	{
		last = A.applyI();
		return X[last].next();
	}

	public int lastI()
	{
		return last;
	}

	public double next(int i)
	{
		return X[i].next();
	} 

	protected RandomVariable[] X = null;
	protected AliasRejection A = null;

	private int last = 0;
}
