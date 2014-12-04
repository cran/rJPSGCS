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
 This class provides replicates from the Chi Squared distribution.
 It is implemented as a special case of the Gamma.
*/
public class ChiSquaredRV extends GammaRV
{
	static { className = "ChiSquaredRV"; }

/**
 Creates a new Chi Squared random variable with 1 degree of freedom.
*/
	public ChiSquaredRV()
	{
		this(1);
	}

/**
 Creates a new Chi Squared random variable with the specified
 degrees of freedom.
*/
	public ChiSquaredRV(double df)
	{
		super(df*0.5,0.5);
	}
}
