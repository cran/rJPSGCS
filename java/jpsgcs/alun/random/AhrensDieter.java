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
 This is a generation method for use specifically 
 for the Gamma distribution with shape parameter in (0,1)
 and with rate parameter 1.
*/
public class AhrensDieter extends GenerationMethod
{
/**
 Creates a new Ahrens-Dieter scheme.
*/
	public AhrensDieter(double alpha)
	{
		a = alpha;
	}

/**
 Applies the method and returns the result.
*/
	public double apply()
	{
		while(true)
		{
			double u = U();
	
			if (u <= e/(a+e))
			{
				double x = Math.pow(((a+e)*u/e),1/a);
				if (U() <= Math.exp(-x))
					return x;
			}
			else
			{
				double x = -Math.log((a+e)*(1-u)/a/e);
				if (U() <= Math.pow(x,a-1))
					return x;
			}
		}
	}

	private static final double e = Math.E;
	private double a = 0;
}
