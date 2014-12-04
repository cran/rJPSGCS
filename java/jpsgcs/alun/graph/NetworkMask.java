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
import java.util.LinkedHashSet;

public class NetworkMask<V,E> implements MaskedGraph<V,E>, MutableGraph<V,E>
{
	public NetworkMask(Network<V,E> graph)
	{
		g = graph;
		show = new LinkedHashSet<V>(g.getVertices());
		hide = new LinkedHashSet<V>();
	}

// Mask interface

	public Network<V,E> completeGraph()
	{
		return g;
	}

	public void show(V x)
	{
		if (hide.contains(x))
		{
			hide.remove(x);
			show.add(x);
		}
	}

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

	public void showAll()
	{
		show.addAll(hide);
		hide.clear();
	}

	public void hideAll()
	{
		hide.addAll(show);
		show.clear();
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

// MutableGraph

	public void clear()
	{
		g.clear();
	}

	public boolean add(V v)
	{
		return g.add(v);
	}

	public boolean connect(V x, V y)
	{
		return g.connect(x,y);
	}

	public boolean connect(V x, V y, E e)
	{
		return g.connect(x,y,e);
	}

	public boolean  disconnect(Object x, Object y)
	{
		return g.disconnect(x,y);
	}

	public boolean remove(Object x)
	{
		return g.remove(x);
	}
	

// Private data
	
	private Network<V,E> g = null;
	private Set<V> show = null;
	private Set<V> hide = null;
}
