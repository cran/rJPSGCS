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

import jpsgcs.alun.markov.Function;
import jpsgcs.alun.markov.Variable;

public class ErrorPrior extends GenepiFunction implements Function
{
	public ErrorPrior(Error e, double prob)
	{
		v = new Variable[1];
		v[0] = e;
		p = prob;
	}

	public double getValue()
	{
		return v[0].getState() == 0 ? 1-p : p;
	}

	public String toString()
	{
		return "EPRI"+super.toString();
	}

// Private data.

	private double p = 0;
}
