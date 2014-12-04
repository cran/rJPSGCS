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


package jpsgcs.alun.jtree;

import jpsgcs.alun.markov.Variable;
import jpsgcs.alun.markov.MultiVariable;
import jpsgcs.alun.markov.DenseTable;
import jpsgcs.alun.markov.Table;
import jpsgcs.alun.markov.MarkovRandomField;

import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.LinkedHashMap;

public class MultinomialMLEScorer extends CliqueScorer<Variable>
{
	public MultinomialMLEScorer(Variable[] x, IntegerMatrix y, double pen)
	{
		this(x,y,pen,1);
	}

	public MultinomialMLEScorer(Variable[] x, IntegerMatrix y, double pen, double t)
	{
		temp = t;
		alpha = pen;

		v = x;
		map = new LinkedHashMap<Variable,Integer>();
		for (int i=0; i<x.length; i++)
			map.put(v[i],i);

		data = y;

		store = new LinkedHashMap<Set<Variable>,Double>();
	}

	public double score(Set<Variable> s)
	{
		Double d = store.get(s);
		if (d != null)
			return d.doubleValue();

		DenseTable t = getTable(s);
		double x = t.logLikelihood() - alpha * t.degreesOfFreedom();
		store.put(s,x);
		return x;
	}

	public double temperature()
	{
		return temp;
	}

	public void setTemperature(double t)
	{
		temp = t;
	}

	public void setPenalty(double a)
	{
		alpha = a;
	}

	public double getPenalty()
	{
		return alpha;
	}

	public void clear()
	{
		store.clear();
	}

	public DenseTable getTable(Set<Variable> s)
	{
		DenseTable t = new DenseTable(s);
		t.allocate();

		for (int i=0; i<data.nRows(); i++)
		{
			for (Variable v : s)
				v.setState(data.value(i,index(v)));
			t.increase(1);
		}

		return t;
	}
	
	public MarkovRandomField fitModel(JTree<Variable> jt)
	{
		MarkovRandomField mod = new MarkovRandomField();

		Map<Clique<Variable>,Clique<Variable>> seq = jt.cliqueElimination();
		
		for (Clique<Variable> c : seq.keySet())
		{
			Set<Variable> s = null;
			if (seq.get(c) != null)
				s = jt.connection(c,seq.get(c));

			DenseTable tc = getTable(c);
			
			if (s == null)
			{
				mod.add(tc);
				continue;
			}

			DenseTable ts = new DenseTable(s);
			ts.allocate();
			
			MultiVariable m = tc.getMultiVariable();
			for (m.init(); m.next(); )
				ts.increase(tc.getValue());

			for (m.init(); m.next(); )
			{
				double x = tc.getValue();
				if (x > 0)
				{
					x /= ts.getValue();
					tc.setValue(x);
				}
			}
			
			mod.add(tc);
		}

		return mod;
	}

// Private data.

	private double temp = 1;
	private double alpha = 1;
	private IntegerMatrix data = null;
	private Variable[] v = null;
	private Map<Variable,Integer> map = null;
	private Map<Set<Variable>,Double> store = null;

	private int index(Variable x)
	{
		return map.get(x).intValue();
	}
}
