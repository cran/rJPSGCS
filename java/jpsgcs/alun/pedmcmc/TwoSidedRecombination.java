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
import jpsgcs.alun.pedapps.GeneticMap;

public class TwoSidedRecombination extends GenepiFunction implements Function
{
	public TwoSidedRecombination(Inheritance x, Inheritance y, Inheritance z, double lt, double rt)
	{
		v = new Variable[3];
		v[0] = x;
		v[1] = y;
		v[2] = z;

		fix(lt,rt);
	}

	public void fix(double lt, double rt)
	{
		left = lt;
		right = rt;
		all = GeneticMap.sumTheta(lt,rt);
	}

	public double getValue()
	{
		double x = v[0].getState() == v[1].getState() ? 1-left : left;
		x *= v[1].getState() == v[2].getState() ? 1-right : right;
		x /= v[0].getState() == v[2].getState() ? 1-all : all;

		return x;
	}

	public String toString()
	{
		return "REC"+super.toString();
	}

// Private data.

	private double left = 0;
	private double right = 0;
	private double all = 0;
}
