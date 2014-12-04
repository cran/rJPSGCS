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
import jpsgcs.alun.markov.MultiVariable;
import jpsgcs.alun.markov.Iterable;
import jpsgcs.alun.markov.Function;

public class AlleleTransmission extends GenepiFunction implements Iterable, Function
{
	public AlleleTransmission(Allele pat, Allele mat, Inheritance inh, Allele mine)
	{
		Variable[] u = {pat, mat, inh, mine};
		v = u;
		
		Variable[] w = {pat, mat, inh};
		m = new MultiVariable(w);
	}

	public void init()
	{
		m.init();
	}

	public boolean next()
	{
		while(true)
		{
			if (!m.next())
				return false;
			
			int s = v[2].getState() == 0 ? v[0].getState() : v[1].getState();

			if (v[3].hasState(s))
			{
				v[3].setState(s);
				return true;
			}
		}
	}

	public int getNStates()
	{
		return m.getNStates();
	}

	public double getValue()
	{
		return v[3].getState() == v[ v[2].getState() ].getState() ? 1 : 0 ;
	}

	public String toString()
	{
		return "AT"+super.toString();
	}

// Private data.

	private MultiVariable m = null;
}
