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


package jpsgcs.alun.markov;

import jpsgcs.alun.graph.Network;
import jpsgcs.alun.graph.Graphs;
import jpsgcs.alun.jtree.JTrees;
import jpsgcs.alun.jtree.JTree;
import jpsgcs.alun.jtree.GraphNotDecomposableException;
import jpsgcs.alun.jtree.Clique;

import java.util.Collection;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.TreeSet;


public class MarkovRandomField
{
	public MarkovRandomField()
	{
		g = new Network<Object,Object>();
	}

	public MarkovRandomField(Collection<Function> f)
	{
		this();
		for (Function g : f)
			add(g);
	}

	public Set<Function> getFunctions()
	{
		Set<Function> f = new LinkedHashSet<Function>();
		
		for (Object x : g.getVertices())
			if (x instanceof Function)
				f.add((Function)x);

		return f;
	}

	public Network<Variable,Object> markovGraph()
	{
		Set<Function> f = new LinkedHashSet<Function>();
		for (Object x : g.getVertices())
			if (x instanceof Function)
				f.add((Function)x);

		return Functions.markovGraph(f);
	}

	public ModelInfo[] compile()
	{
		// Find all the vertices and functions in the variable/function graph.

		Set<Function> f = new LinkedHashSet<Function>();
		Set<Variable> v = new LinkedHashSet<Variable>();
		
		for (Object x : g.getVertices())
		{
			if (x instanceof Function)
				f.add((Function)x);
			if (x instanceof Variable)
				v.add((Variable)x);
		}

		// Make the Markov graph from the list of functions.
		// Triangulate it if necessary. 
		// And find the cliques.

		Network<Variable,Object> m = Functions.markovGraph(f);
		Set<Variable> togo = new LinkedHashSet<Variable>();
		for (Variable u : m.getVertices())
			if (!v.contains(u))
				togo.add(u);
		for (Variable u : togo)
			m.remove(u);

		JTree<Variable> jt = null;
		try
		{
			jt = new JTree<Variable>(m);
		}
		catch (GraphNotDecomposableException e)
		{
			System.err.print("Triangulating ... ");
			//System.err.print(m.getVertices().size()+"  "+Graphs.countEdges(m)+"   ");
			Graphs.triangulate(m);
			jt = new JTree<Variable>(m);
			//System.err.print(jt.getCliques().size()+"   ");
			//System.err.println("done.");
		}

// This is quadratic. Should you do it????
//		JTreeRandomizer.randomize(jt);
		Map<Clique<Variable>,Clique<Variable>> map = jt.cliqueElimination();

		// Map each clique set representation to a plain Tree Set.
		Map<Clique<Variable>,Set<Variable>> clean = new LinkedHashMap<Clique<Variable>,Set<Variable>>();
		for (Clique<Variable> c : map.keySet())
			clean.put(c,new TreeSet<Variable>(c));

		// Find the functions associated with each clique.
		ModelInfo[] output = new ModelInfo[map.size()];
		int n = 0;

		for (Clique<Variable> c : map.keySet())
		{
			Set<Function> fc = getFunctions(c);
			fc.retainAll(f);

			Set<Function> func = new LinkedHashSet<Function>();
			Set<Function> evid = new LinkedHashSet<Function>();

			for (Function ff : fc)
			{
				Set<Variable> u = Functions.asSet(ff.getVariables());

				if (c.containsAll(u))
				{
					func.add(ff);
					continue;
				}

				u.retainAll(v);

				if (c.containsAll(u))
				{
					evid.add(ff);
					continue;
				}
			}

			f.removeAll(func);
			f.removeAll(evid);

			output[n++] = new ModelInfo(clean.get(c),clean.get(map.get(c)),func,evid);
		}

		return output;
	}

	public Set<Object> getElements()
	{
		return g.getVertices();
	}

	public void add(Function f)
	{
		g.add(f);
		for (Variable v : f.getVariables())
			g.connect(f,v);
	}

	public void remove(Object v)
	{
		g.remove(v);
	}

	public void clear()
	{
		g.clear();
	 }

	public MarkovRandomField replicate(Map<Variable,Variable> map)
	{
		MarkovRandomField rep = new MarkovRandomField();
		for (Function f : getFunctions())
			rep.add(replicate(map,f));

		Set<Variable> rem = new LinkedHashSet<Variable>();
		for (Object x : rep.g.getVertices())
			if (x instanceof Variable)
				rem.add((Variable)x);

		for (Object x : g.getVertices())
			if (x instanceof Variable)
				rem.remove(map.get(x));

		for (Object x : rem)
			rep.remove(x);

		return rep;
	}

	public Function replicate(Map<Variable,Variable> map, Function f)
	{
		Set<Variable> v = new LinkedHashSet<Variable>();
		for (Variable u : f.getVariables())
			v.add(map.get(u));
		
		MultiVariable w = new MultiVariable(v);
		DenseTable t = new DenseTable(w);
		t.allocate();
		for (w.init(); w.next(); )
		{
			for (Variable u : f.getVariables())
				u.setState(map.get(u).getState());
			t.setValue(f.getValue());
		}

		return t;
	}

	public Network<Object,Object> bipartiteGraph()
	{
		return g;
	}

// Private data and methods.

	protected Network<Object,Object> g = null;

	private Set<Function> getFunctions(Collection<Variable> vs)
	{
		Set<Function> f = new LinkedHashSet<Function>();
		for (Variable v : vs)
		{
			Collection<Object> fn = g.getNeighbours(v);
			if (fn == null)
				continue;
			for (Object x : fn)
				f.add((Function) x);
		}

		return f;
	}
}
