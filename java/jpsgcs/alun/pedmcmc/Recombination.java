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


package jpsgcs.alun.pedmcmc;

import jpsgcs.alun.markov.Variable;
import jpsgcs.alun.markov.Function;

public class Recombination extends GenepiFunction implements Function
{
	public Recombination(Variable x, Variable y, double theta)
	{
		v = new Variable[2];
		v[0] = x;
		v[1] = y;
		t = theta;
		onemint = 1-t;
	}

	public void fix(double th)
	{
		t = th;
		onemint = 1-t;
	}

	public double getValue()
	{
		return v[0].getState() == v[1].getState() ? onemint : t;
	}

	public String toString()
	{
		return "REC"+super.toString();
	}

// Private data.

	private double t = 0;
	private double onemint = 0;
}
