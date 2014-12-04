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

public class AlleleErrorPenetrance extends AllelePenetrance
{
	public AlleleErrorPenetrance(Allele paternal, Allele maternal, Error err)
	{
		this(paternal,maternal,err,null);
	}

	public AlleleErrorPenetrance(Allele paternal, Allele maternal, Error err, double[][] penet)
	{
		v = new Variable[3];
		v[0] = paternal;
		v[1] = maternal;
		v[2] = err;
		fix(penet);
	}

	public double getValue()
	{
		return v[2].getState() == 1 ? q : p[v[0].getState()][v[1].getState()] ;
	}

	public String toString()
	{
		return "EPEN"+super.toString();
	}

	public void fix(double[][] penet)
	{
		p = penet;
		if (p != null)
			q = 1.0 / (p.length*p.length);
	}

// Private data.

	private double q = 0;
}
