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
 This class is specifically for generating Gamma variates
 when the shape parameter is > 1, and the rate parameter is 1.
*/
public class ChengFeast extends GenerationMethod
{
/** 
 Creates a new Cheng-Feast scheme.
*/
	public ChengFeast(double alpha)
	{
		a = alpha;
		c1 = a-1;
		c2 = (a-1.0/6.0/a)/c1;
		c3 = 2/c1;
		c4 = c3+2;
		c5 = 1/Math.sqrt(a);
	}

/**
 Applies the method and returns the result.
*/
	public double apply()
	{
		double u, v, w;

		do
		{
			do
			{
				u = U();
				v = U();
				if (a > B1)
					u = v + c5 * (1-B2*u);
			}
			while (u>1 || u<0);
			w = c2*v/u;
		}
		while (c3*u+w+1/w > c4 && c3*Math.log(u)-Math.log(w)+w > 1);

		return c1*w;
	}

	private static final double B1 = 2.5;
	private static final double B2 = 1.86;
	private double a = 0;
	private double c1, c2, c3, c4, c5;
}
