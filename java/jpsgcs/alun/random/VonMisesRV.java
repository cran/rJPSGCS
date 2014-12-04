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
 This class delivers replicates from a von Mises distribution.
 The method used is rejection.
*/
public class VonMisesRV extends RandomVariable implements DensityFunction
{
	static { className = "VonMisesRV"; }

/**
 Creates a new von Mises random variable with default parameter 1.
*/
	public VonMisesRV()
	{
		this(1);
	}

/**
 Creates a new von Mises random variable with specified pararmeter value.
*/
	public VonMisesRV(double kappa)
	{
		set(kappa);
		Envelope X = new UniformRV(-Math.PI,Math.PI);
		setMethod(new Rejection(this,X,2*Math.PI));
	} 

/**
 Sets the parameter to the given value.
*/
	public void set(double kappa)
	{
		if (kappa <= 0)
			throw new ParameterException("Von Mises kappa parameter must be positive");
		k = kappa;
	}

/**
 Provides a function proportional to the density function as required by 
 the rejection method.
*/
	public double densityFunction(double x)
	{
		return x > -Math.PI && x <= Math.PI ? Math.exp(k*(Math.cos(x)-1)) : 0 ;
	}

	private double k = 1;
}
