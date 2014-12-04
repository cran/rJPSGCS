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
 This class is a method specifically for generating Binomial replicates.
 See Ripley's Stochastic Simultion, page 78.
*/
public class AhrensBinomial extends GenerationMethod implements IntegerMethod
{
/**
 Creates a new Ahrens Binomial generating scheme.
*/
	public AhrensBinomial(int num, double prob)
	{
		n = num; 
		p = prob;
	}

/**
 Applies the method and return the result as a double.
*/
	public double apply()
	{
		return applyI();
	}

/**
 Applies the method and returns the result as an integer.
*/
	public int applyI()
	{
		double q = p;
		int k = n;
		int X = 0;
	
		do
		{
			int i = (int)(1+k*q);
			double V = (new BetaRV(i,k+1-i)).next();
			if (q < V)
			{
				q = q/V;
				k = i-1;
			}
			else
			{
				X += i;
				q = (q-V)/(1-V);
				k = k-i;
			}
		}
		while (k > K);
	
		for (int i=0; i<k; i++)
			if (U() <= q) 
				X++;

		return X;
	}

	private int K = 100;
	private int n = 0;
	private double p = 0;
}
