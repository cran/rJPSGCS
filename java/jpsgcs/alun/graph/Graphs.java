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


package jpsgcs.alun.graph;

import java.util.Set;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
	A class containing static methods for basic graph manipulations. 
	Analagous to the the Collections class that manipulates a Collecction.
*/

public class Graphs
{

	static public <V,E> int countEdges(Graph<V,E> g)
	{
		int a = 0;
		int b = 0;
		for (V v : g.getVertices())
		{
			Collection<V> n = g.getNeighbours(v);
			a += n.size();
			if (n.contains(v))
				b++;
		}

		return (a+b)/2;
	}

	static public <V,E> void triangulate(MutableGraph<V,E> g)
	{
		triangulate(g, new LinkedHashSet<V>());
	}

/**
 	Triangulates a graph using greedy algorithm for minimum fill ins.
*/
	static public <V,E> void triangulate(MutableGraph<V,E> g, Collection<V> keep)
	{
		Set<V> peel = new LinkedHashSet<V>(g.getVertices());
		peel.removeAll(keep);

		Network<V,E> h = new Network<V,E>();
		for (V v : g.getVertices())
		{
			h.add(v);
			for (V u : g.getNeighbours(v))
				h.connect(u,v);
		}

		while (!peel.isEmpty())
		{
			V v = null;
			double best = Double.MAX_VALUE;
			
			for (V u : peel)
			{
				double c = cost(h,u);
				if (c == 0)
				{
					v = u;
					break;
				}
				if (c < best)
				{
					v = u;
					best = c;
				}
			}

			for (V u : h.getNeighbours(v))
				if (u != v)
					for (V w : h.getNeighbours(v))
						if (w != v)
							if (u != w && !h.connects(u,w))
							{
								h.connect(u,w);
								g.connect(u,w);
							}
	
			h.remove(v);
			peel.remove(v);
		}
/*
		while (!peel.isEmpty())
		{
			V v = null;
			double best = Double.MAX_VALUE;
			
			Set<V> done = new LinkedHashSet<V>();

			for (V u : peel)
			{
				double c = cost(h,u);
				if (c == 0)
				{
					//v = u;
					done.add(u);
				}
				else if (c < best)
				{
					v = u;
					best = c;
				}
			}

			if (!done.isEmpty())
			{
				for (V x : done)
				{
					h.remove(x);
					peel.remove(x);
				}
			}
			else
			{
				if (v == null)
					continue;

				for (V u : h.getNeighbours(v))
					if (u != v)
						for (V w : h.getNeighbours(v))
							if (w != v)
								if (u != w && !h.connects(u,w))
								{
									h.connect(u,w);
									g.connect(u,w);
								}
	
				h.remove(v);
				peel.remove(v);
			}
		}
*/
	}

	static private <V,E> double cost(Graph<V,E> g, V v)
	{
		V[] n = (V[]) g.getNeighbours(v).toArray(new Object[0]);
		double count = 0;
		for (int i=0; i<n.length; i++)
			for (int j=i+1; j<n.length; j++)
				if (!g.connects(n[i],n[j]))
					count += 1;
		return count;
	}
	
/** 
	Returns that sub component of g that contains v.
*/
	static public <V,E> Collection<V> component(Graph<V,E> g, V v)
	{
		if (!g.contains(v))
			return null;

		ArrayList<V> a = new ArrayList<V>();
		Set<V> s = new LinkedHashSet<V>();
		s.add(v);
		a.add(v);

		for (int i=0; i<a.size(); i++)
			for (V u : g.getNeighbours(a.get(i)))
				if (!s.contains(u))
				{
					s.add(u);
					a.add(u);
				}
		
		return s;
	}

/**
	Returns true if all pairs of verticies in x are connected in g.
	Takes twice as much time as necessary for undirected graphs 
	because pairs are checked in both orientations.
*/
	static public <V,E> boolean isClique(Graph<V,E> g, Collection<V> x)
	{
		for (V u : x)
			for (V v : x)
				if (u != v && !g.connects(u,v))
					return false;
		return true;
	}

/**
	Reads a graph from standard input.
*/
	static public Network<String,Object> read() throws IOException
	{
		return read(new BufferedReader(new InputStreamReader(System.in)));
	}
	
/**
	Reads a graph from the given BufferedReader.
	The input is interpreted as an adjacency list. The first String on each line
	is connected to all the following Strings.
*/
	static public Network<String,Object> read(BufferedReader b) throws IOException
	{
		Map<String,String> map = new LinkedHashMap<String,String>();

		Network<String,Object> g = new Network<String,Object>();
		for (String s = b.readLine(); s != null; s = b.readLine())
		{
			StringTokenizer t = new StringTokenizer(s);
			if (t.hasMoreTokens())
			{
				String v = standard(t.nextToken(),map);
				g.add(v);
				while (t.hasMoreTokens())
				{
					String u = standard(t.nextToken(),map);
					g.connect(v,u);
				}
			}
		}
		return g;
	}

	static private <T> T standard(T x, Map<T,T> map)
	{
		T y = map.get(x);

		if (y == null)
		{
			map.put(x,x);
			return x;
		}

		return y;
	}

/**
	Reads a graph from standard input as for read(), but the Strings specified in the
	input must be iterpretable as Integers.
*/
	static public Network<Integer,Object> readAsIntegers() throws IOException
	{
		return readAsIntegers(new BufferedReader(new InputStreamReader(System.in)));
	}

	
/**
	Reads a graph from the give BufferedReader read(), but the Strings specified in the
	input must be iterpretable as Integers.
*/
	static public Network<Integer,Object> readAsIntegers(BufferedReader b) throws IOException
	{
		Map<Integer,Integer> map = new LinkedHashMap<Integer,Integer>();
		Network<Integer,Object> g = new Network<Integer,Object>();
		for (String s = b.readLine(); s != null; s = b.readLine())
		{
			StringTokenizer t = new StringTokenizer(s);
			if (t.hasMoreTokens())
			{
				Integer v = standard(new Integer(t.nextToken()),map);
				g.add(v);
				while (t.hasMoreTokens())
					g.connect(v,standard(new Integer(t.nextToken()),map));
			}
		}
		return g;
	}

/**
	Make a graph from the given collection of cliques.
*/
	static public <V,E> Network<V,E> graphFromCliques(Collection<? extends Set<V>> cl)
	{
		Network<V,E> g = new Network<V,E>();
		
		for (Set<V> c : cl)
		{
			for (V a : c)
			{
				g.add(a);
				for (V b : c)
					if (a != b)
						g.connect(a,b);
			}
		}

		return g;
	}
}
