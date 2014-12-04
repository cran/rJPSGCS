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

import jpsgcs.alun.util.IntValue;
import jpsgcs.alun.graph.Graph;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Collection;

public class CardinalitySorter<V>
{
	public CardinalitySorter(Collection<V> x)
	{
		a = new ArrayList<LinkedHashSet<V>>();
		a.add(new LinkedHashSet<V>());
		
		h = new LinkedHashMap<V,IntValue>();
		for (V v : x)
		{
			h.put(v,new IntValue());
			a.get(0).add(v);
		}
	}

	public V next()
	{
		int i = a.size()-1;
		if (i < 0)
			return null;

		V  x = a.get(i).iterator().next();

		a.get(i).remove(x);
		for (int j=i; j>=0 && a.get(j).isEmpty(); j--)
			a.remove(j);

		h.remove(x);

		return x;
	}

	public void add(V v)
	{
		IntValue x = h.get(v);
		if (x == null)
			return;

		a.get(x.value()).remove(v);
		x.add(1);
		if (x.value() == a.size())
			a.add(new LinkedHashSet<V>());
		a.get(x.value()).add(v);
	}
		
// Private data.

	private LinkedHashMap<V,IntValue> h = null;
	private ArrayList<LinkedHashSet<V>> a = null;
}
