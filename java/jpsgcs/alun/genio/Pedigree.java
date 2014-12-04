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


package jpsgcs.alun.genio;

import jpsgcs.alun.util.Triplet;
import jpsgcs.alun.graph.Network;
import java.util.Set;
import java.util.Map;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.Collection;

public class Pedigree
{
	public Pedigree()
	{
		g = new Network<Object,Set<Object>>();
		h = new LinkedHashMap<Object,Triplet>();
		males = new LinkedHashSet<Object>();
		females = new LinkedHashSet<Object>();
	}

	public void addTriplet(Triplet t)
	{
		Set<Object> s = g.connection(t.y,t.z);
		if (t.y != null && t.z != null)
		{
			if (s == null)
			{
				s = new LinkedHashSet<Object>();
				g.connect(t.y,t.z,s);
			}
			s.add(t.x);
		}
		h.put(t.x,t);
		males.add(t.y);
		females.add(t.z);
	}

	public Object pa(Object x)
	{
		Triplet t = h.get(x);
		return t == null ? null : t.y;
	}
	
	public Object ma(Object x)
	{
		Triplet t = h.get(x);
		return t == null ? null : t.z;
	}
	
	public void addTriplets(Collection<Triplet> t)
	{
		for (Triplet x : t)
			addTriplet(x);
	}

	public Triplet getTriplet(Object x)
	{
		return h.get(x);
	}

	public Object[] kids(Object x)
	{
		Collection ss = g.getNeighbours(x);
		if (ss == null)
		{
			Object[] result = {};
			return result;
		}

		Set<Object> k = new LinkedHashSet<Object>();
		for (Object i : ss)
			k.addAll(g.connection(x,i));
		return k.toArray();
	}

	public Family[] nuclearFamilies()
	{
		Network<Object,Set<Object>> n = new Network<Object,Set<Object>>();
		for (Object u : g.getVertices())
		{
			n.add(u);
			for (Object q : g.getNeighbours(u))
				n.connect(u,q,g.connection(u,q));
		}

		Set<Family> f = new LinkedHashSet<Family>();

		for (Object u : g.getVertices())
		{
			if (males.contains(u))
			{
				for (Object v : n.getNeighbours(u))
				{
					Family ff = new Family();
					ff.setPa(u);
					ff.setMa(v);
					for (Object w : n.connection(u,v))
						ff.addKid(w);
					f.add(ff);
				}
				n.remove(u);
			}
		}
		return (Family[]) f.toArray(new Family[0]);
	}

	public Collection<Object> individuals()
	{
		return h.keySet();
	}

	private Network<Object,Set<Object>> g = null;
	private Map<Object,Triplet> h = null;
	private Set<Object> males = null;
	private Set<Object> females = null;
}
