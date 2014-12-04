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

public class AuxLinkFunction extends GenepiFunction implements Function
{
	public AuxLinkFunction(AuxVariable xx, AuxVariable yy, double ptheta, double mtheta)
	{
		x = xx;
		y = yy;

		v = new Variable[2];
		v[0] = x;
		v[1] = y;

		pt = ptheta;
		mt = mtheta;
	}

	public double getValue()
	{
		double a = 1;

		for (int i=0; i<x.p.length; i++)
			a *= x.p[i].getState() == y.p[i].getState() ? 1-pt : pt;

		for (int i=0; i<x.m.length; i++)
			a *= x.m[i].getState() == y.m[i].getState() ? 1-mt : mt;

		return a;
	}

	public String toString()
	{
		return "ALINK"+super.toString();
	}
	
	private AuxVariable x = null;
	private AuxVariable y = null;
	private double pt = 0;
	private double mt = 0;
}
