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
 This class creates a ratio of uniforms scheme to generate
 replicates of a random variable.
*/
public class RatioOfUniforms extends GenerationMethod
{
/**
 Creates a new ratio of uniforms scheme by simulating (x,y) from (U,V)
 until the point is contained in the random variable's region.
 then the value y/x is returned.
*/
	public RatioOfUniforms(RatioRegion a, UniformRV U, UniformRV V)
	{
		X = U;
		Y = V;
		r = a;
	}

/**
 Applies the method and returns the result.
*/
	public double apply()
	{
		double x = 0;
		double y = 0;
		do
		{
			x = X.next();
			y = Y.next();
		}
		while (!r.containsInRatioRegion(x,y));
		return y/x;
	}

	private RatioRegion r = null;
	private UniformRV X = null;
	private UniformRV Y = null;
}
