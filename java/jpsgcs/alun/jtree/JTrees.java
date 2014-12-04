/*
 Copyright 2011 Alun Thomas.

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

import jpsgcs.alun.graph.Graph;
import jpsgcs.alun.graph.Graphs;
import jpsgcs.alun.util.Pair;

import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Iterator;

/**
	This is a class of static methods for manipulating graphs and junction trees.
*/
public class JTrees
{
	public static <V,E> List<Pair<V,V>> edgeRecomposition(Graph<V,E> g)
	{
		List<V> l = maximumCardinality(g);
		Set<V> p = new LinkedHashSet<V>(l);
		List<Pair<V,V>> result = new ArrayList<Pair<V,V>>();

		for (V v : l)
		{
			p.remove(v);
			Collection<V> s = new LinkedHashSet<V>(g.getNeighbours(v));
			s.retainAll(p);
			if (!Graphs.isClique(g,s))
				return null;
			
			for (V u : s)
				result.add(new Pair<V,V>(v,u));
		}

		Collections.reverse(result);

		return result;
	}

/**
	Finds the links of the juction tree of a decomposable graph.
	If the graph is not decomposable, a null is returned.
*/
	public static <V,E> Map<Clique<V>,Clique<V>> cliques(Graph<V,E> g)
	{
		return cliques(g,g.getVertices());
	}

	public static <V,E> Map<Clique<V>,Clique<V>> cliques(Graph<V,E> g, Collection<V> ord)
	{
		List<V> l = maximumCardinality(g,ord);

		Set<V> p = new LinkedHashSet<V>(l);
		Map<Clique<V>,Clique<V>> h = new LinkedHashMap<Clique<V>,Clique<V>>();
		Map<Clique<V>,Clique<V>> hh = new LinkedHashMap<Clique<V>,Clique<V>>();

		for (V v : l)
		{
			p.remove(v);
			Clique<V> s = new Clique<V>(g.getNeighbours(v));
			s.retainAll(p);

			if (!Graphs.isClique(g,s))
				return null; 
		
			Clique<V> c = new Clique<V>(s);
			c.add(v);

			Clique<V> y = null;
			for (Clique<V> z : h.keySet())
				if (h.get(z).containsAll(c))
				{
					y = z;
					break;
				}

			if (y != null)
			{
				h.put(y,s);
				continue;
			}

			Set<Clique<V>> done = new LinkedHashSet<Clique<V>>();
			for (Clique<V> x : h.keySet())
				if (c.containsAll(h.get(x)))
				{
					hh.put(x,c);
					done.add(x);
				}

			for (Clique<V> x : done)
				h.remove(x);

			h.put(c,s);
		}

		hh.putAll(h);

		return hh;
	}
	
/**
	Finds a maximum cardinality order for the vertices of the given graph.
*/
	public static <V,E> List<V> maximumCardinality(Graph<V,E> g)
	{
		return maximumCardinality(g,g.getVertices());
	}

	public static <V,E> List<V> maximumCardinality(Graph<V,E> g, Collection<V> ord)
	{
		if (g.getVertices().isEmpty())
			throw new RuntimeException("JTrees:maximumCardinality() supplied graph has no vertices.");

		if (!g.getVertices().equals(ord))
		{
			throw new RuntimeException("JTrees:maximumCardinality() order collection does not match graph vertices");
		}

		//CardinalitySorter<V> c = new CardinalitySorter<V>(g.getVertices());
		CardinalitySorter<V> c = new CardinalitySorter<V>(ord);

		ArrayList<V> b = new ArrayList<V>(g.getVertices().size());

		for (V v = c.next(); v != null; v = c.next())
		{
			b.add(v);
			for (V u : g.getNeighbours(v))
				c.add(u);
		}

		Collections.reverse(b);
		return b;
	}

/**
	Returns true if the order of the vertices listed is a simple elimination
	scheme for the graph. 
*/
	public static <V,E> boolean isPerfectEliminationOrder(Graph<V,E> g, List<V> l)
	{
		Set<V> s = new LinkedHashSet<V>(l);

		if (l.size() != g.getVertices().size())
			return false;

		for (V v : l)
		{
			if (!g.contains(v))
				return false;
			s.remove(v);
			Set<V> n = new LinkedHashSet<V>(g.getNeighbours(v));
			n.retainAll(s);
			if (!Graphs.isClique(g,n))
				return false;
		} 
	
		return true;
	}

/**
	Returns true if the given graph is decomposable, aka chorded,
	aka triangulated.
*/

	public static <V,E> boolean isTriangulated(Graph<V,E> g)
	{
		return isPerfectEliminationOrder(g,maximumCardinality(g));
	}
}
