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
 This class implements the general inversion method.
 The random variable using it has to provide the inverse
 distribution function.
*/
public class Inversion extends GenerationMethod implements IntegerMethod
{
/** 
 Creates a new inversion scheme.
*/
	public Inversion(InverseDistribution i)
	{
		x = i;
	}

/**
 Applies the method and returns the result.
*/
	public double apply()
	{
		return x.inverseDistribution(U());
	}

/**
 Applies the method and returns the result as an integer.
*/
	public int applyI()
	{
		return (int) (apply() + 0.5);
	}

	private InverseDistribution x = null;
}
