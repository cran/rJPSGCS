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


package jpsgcs.alun.zeroloop;

import jpsgcs.alun.graph.Network;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.ArrayList;

public class RandomPedTree<V,E> 
{
	public RandomPedTree(Set<V> m, Set<V> i)
	{
		this(m,i,false);
	}

	public RandomPedTree(Set<V> m, Set<V> i, boolean monog)
	{
		if (i.size() < 2*m.size()+1)
			throw new RuntimeException("Zero loop ped error: # individuals must be >= 1 + 2* #marriages.");

		B = m;
		W = i;
		v = new ArrayList<V>();
		v.addAll(B);
		v.addAll(W);

		mono = monog;
	}

	public Network<V,E> next()
	{
		int m = B.size();
		int n = W.size();

		int[] p = getPrufer();

		int[] deg = new int[v.size()];
		for (int i=0; i<p.length-1; i++)
			deg[p[i]]++;

		Network<V,E> g = new Network<V,E>();

		for (int i=0, k=n-1; i<n-1 || k<n+m-1; )
		{
			int s = 0;
			for ( ; deg[s] != 0; s++);
			int r = (s < m ? p[k++] : p[i++]) ;
			deg[s]--;
			deg[r]--;
			g.connect(v.get(s),v.get(r));
		}

		return g;
	}

// Private data and methods.

	protected Set<V> B = null;
	protected Set<V> W = null;
	protected ArrayList<V> v = null;
	private boolean mono = false;

	protected int[] getPrufer()
	{
		int m = B.size();
		int n = W.size();
		int[] p = new int[n+m-1];

		int j = 0;
		for (int i=0; i<m; i++)
		{
			p[j++] = i;
			p[j++] = i;
		}
		
		while (j < n-1)
			p[j++] = (int) (Math.random() * m);

		for (int i=0; i<n-1; i++)
		{
			int k = i + (int)(Math.random() * (n-1 - i));
			int temp = p[k];
			p[k] = p[i];
			p[i] = temp;
		}

		if (mono)
		{
			int[] ind = new int[n];
			for (int i=0; i<ind.length; i++)
				ind[i] = m + i;

			for (int i=0; i<ind.length; i++)
			{
				int k = (int)(Math.random() * (ind.length - i));
				int temp = ind[k];
				ind[k] = ind[i];
				ind[i] = temp;
			}
		
			for (int i = 0; i<m-1; i++)
				p[j++] = ind[i];
		}
		else
		{
			for (int i=0; i<m-1; i++)
				p[j++] = m + (int)(Math.random() * n);
		}

		p[j++] = n+m-1;

		return p;
	}

/**
 Test main.
*/
	public static void main(String[] args)
	{
		try
		{
			Set<Integer> odds = new LinkedHashSet<Integer>();
			Set<Integer> evens = new LinkedHashSet<Integer>();
			for (int i=0; i<20; i++)
				odds.add(1+2*i);

			for (int i=0; i<42; i++)
				evens.add(2*i);

			RandomPedTree<Integer,Object> r = new RandomPedTree<Integer,Object>(odds,evens,true);
			
			System.out.println(r.next());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
}
