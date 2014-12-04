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
import jpsgcs.alun.markov.Trivial;

public class AlleleSegregation extends GenepiFunction implements Iterable, Function, Trivial 
{
	public AlleleSegregation(Allele pat, Allele mat, Allele mine)
	{
		Variable[] u = {pat, mat, mine};
		v = u;
		
		Variable[] w = {pat, mat};
		m = new MultiVariable(w);
	}

	public void init()
	{
		m.init();
		gotone = false;
	}

	public boolean next()
	{
		while (true)
		{
			if (!gotone)
			{
				if (!m.next())
					return false;

				int s = v[0].getState();

				if (s != v[1].getState())
					gotone = true;
				
				if (v[2].hasState(s))
				{
					v[2].setState(s);
					return true;
				}
			}
			else
			{
				gotone = false;

				int s = v[1].getState();
				if (v[2].hasState(s))
				{
					v[2].setState(s);
					return true;
				}
			}
		}
	}

	public int getNStates()
	{
		return 2*m.getNStates();
	}

	public double getValue()
	{
/*
		int s = v[2].getState();
		return s == v[0].getState() || s == v[1].getState() ? 1 : 0;
*/
		return 1;
	}

	public String toString()
	{
		return "AS"+super.toString();
	}

// Private data.

	private MultiVariable m = null;
	private boolean gotone = false;
}
