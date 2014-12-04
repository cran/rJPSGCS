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
 This class provides variates from the Cauchy distribution.
 The method used is ration of uniforms.
*/
public class CauchyRV extends RandomVariable implements RatioRegion
{
	static { className = "CauchyRV"; }

/**
 Creates a new Cauchy random variable.
*/
	public CauchyRV()
	{
		setMethod(new RatioOfUniforms(this,new UniformRV(0,1),new UniformRV(-1,1)));
	}

/**
 Returns true iff the given point is contained in a half circle.
*/
	public boolean containsInRatioRegion(double x, double y)
	{
		return x*x + y*y <= 1;
	}
}
