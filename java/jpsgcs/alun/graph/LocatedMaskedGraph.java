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

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.Map;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;

public class LocatedMaskedGraph<V,E> implements MaskedGraph<V,E>, LocatedGraph<V,E>
{
	public LocatedMaskedGraph(Graph<V,E> graph)
	{
		g = graph;
		show = new LinkedHashSet<V>();
		hide = new LinkedHashSet<V>();

		point = new LinkedHashMap<V,Point>();
		for (V v : g.getVertices())
		{
			show.add(v);
			point.put(v,new Point());
		}
	}

	public Collection<Point> getPoints(V v)
	{
		Set<Point> s = new LinkedHashSet<Point>();
		s.add(point.get(v));
		return s;
	}

	public Collection<Point> getPoints(Collection<V> c)
	{
		Set<Point> s = new LinkedHashSet<Point>();
		for (V v : c)
			s.add(point.get(v));
		return s;
	}

	public Collection<Point> getPoints()
	{
		return point.values();
	}

	public Collection<Point> getShownPoints()
	{
		Set<Point> s = new LinkedHashSet<Point>();
		for (V v : show)
			s.add(point.get(v));
		return s;
	}

	public Collection<Point> getComponentPoints(V v)
	{
		Set<Point> s = new LinkedHashSet<Point>();
		for (V u : Graphs.component(completeGraph(),v))
			s.add(point.get(u));
		return s;
	}

	public Collection<Point> getShownComponentPoints(V v)
	{
		Set<Point> s = new LinkedHashSet<Point>();
		for (V u : Graphs.component(this,v))
			s.add(point.get(u));
		return s;
	}

// Located interface

	public V find(double x, double y)
	{
		V u = null;
		double dd = Double.MAX_VALUE;
		for (V v : getVertices())
		{
			Point p = point.get(v);
			double d = (x-p.x)*(x-p.x) + (y-p.y)*(y-p.y);
			if (d < dd)
			{
				dd = d;
				u = v;
			}
		}
		return u;
	}

	public Point getPoint(V v)
	{
		return point.get(v);
	}

// Mask interface

	public Graph<V,E> completeGraph()
	{
		return g;
	}

	//synchronized public void show(V x)
	public void show(V x)
	{
		if (hide.contains(x))
		{
			hide.remove(x);
			show.add(x);
		}
	}

	//synchronized public void hide(V x)
	public void hide(V x)
	{
		if (show.contains(x))
		{
			show.remove(x);
			hide.add(x);
		}
	}

	public void show(Collection<V> c)
	{
		for (V v : c)
			show(v);
	}

	public void hide(Collection<V> c)
	{
		for (V v : c)
			hide(v);
	}

// Graph interface

	public boolean contains(Object x)
	{
		return show.contains(x);
	}

	public boolean connects(Object x, Object y)
	{
		return show.contains(x) && show.contains(y) && g.connects(x,y);
	}

	public E connection(Object x, Object y)
	{
		return show.contains(x) && show.contains(y) ? g.connection(x,y) : null ;
	}	

	public Collection<E> connections(Object x)
	{
		return show.contains(x) ? g.connections(x) : null ;
	}

	public Set<V> getVertices()
	{
		return Collections.unmodifiableSet(show);
	}

	public Collection<V> getNeighbours(Object x)
	{
		Collection<V> n = new LinkedHashSet<V>(g.getNeighbours(x));
		n.retainAll(show);
		return n;
	}

	public Collection<V> inNeighbours(Object x)
	{
		Collection<V> n = new LinkedHashSet<V>(g.inNeighbours(x));
		n.retainAll(show);
		return n;
	}

	public Collection<V> outNeighbours(Object x)
	{
		Collection<V> n = new LinkedHashSet<V>(g.outNeighbours(x));
		n.retainAll(show);
		return n;
	}

	public boolean isDirected()
	{
		return g.isDirected();
	}

// Private data
	
	private Graph<V,E> g = null;
	private Set<V> show = null;
	private Set<V> hide = null;
	private Map<V,Point> point = null;
}
