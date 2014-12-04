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
import jpsgcs.alun.markov.GraphicalModel;
import jpsgcs.alun.markov.Potential;

import java.util.Set;
import java.util.LinkedHashSet;

public class AuxLocusFunction extends GenepiFunction implements Function
{
	public AuxLocusFunction (AuxVariable x, GraphicalModel g)
	{
		v = new Variable[1];
		v[0] = x;
		m = g;
		tab = new double[v[0].getNStates()];

		Set<Variable> s = new LinkedHashSet<Variable>();
		for (Variable u : x.p)
			s.add(u);
		for (Variable u : x.m)
			s.add(u);

		p = g.splitByOutvol(s);
	}

	Potential[][] p = null;

	public void prepare()
	{
		m.preLogPeel(p[0],true);

		for (v[0].init(); v[0].next(); )
			tab[v[0].getState()] = m.postLogPeel(p[1],true);

		m.cleanUpOutputs();

/*
		for (v[0].init(); v[0].next(); )
			tab[v[0].getState()] = m.logPeel(true);
*/

		double t = tab[0];

		for (int i=0; i<tab.length; i++)
			tab[i] = Math.exp(tab[i]-t);
	}

	public double getValue()
	{
		return tab[v[0].getState()];
	}

	public String toString()
	{
		return "ALOC"+super.toString();
	}

// Private data.

	private double[] tab = null;
	private GraphicalModel m = null;
}
