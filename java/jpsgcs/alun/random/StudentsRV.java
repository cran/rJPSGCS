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
 This class provides replicates from Student's t distribution.
 The method used is to take the ratio of a Gaussian and a  
 Chi Squared.
*/
public class StudentsRV extends RandomVariable
{
	static { className = "StudentsRV"; }

/**
 Creates a new Student's t random variable with 1 degree of freedom.
 This is the same as a Cauchy random variable.
*/
	public StudentsRV()
	{
		this(1);
	}

/**
 Creates a new Student's t distribution with the given degrees of freedom.
*/
	public StudentsRV(double df)
	{
		set(df);
		X = new GaussianRV();
	}

/**
 Sets the degrees of freedom parameter to the given value.
*/
	public void set(double df)
	{
		if (df <= 0)
			throw new ParameterException("Student's t degrees of freedom must be positive");
		Y = new ChiSquaredRV(df);
		d = 1.0/df;
	}

/**
 Returns the next replicate.
*/
	public double next()
	{
		return X.next()/Math.sqrt(Y.next()*d);
	}

	private double d = 1;
	private RandomVariable X = null;
	private RandomVariable Y = null;
}
