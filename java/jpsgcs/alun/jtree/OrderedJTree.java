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

import java.util.Set;
import java.util.Map;
import java.util.LinkedHashMap;

public class OrderedJTree<V> extends OnePairSampler<V>
{
	public OrderedJTree(JTree<V> jt, V[] o, double max, Set<V> ok)
	{
		this(jt,o,max,ok,null);
	}

	public OrderedJTree(JTree<V> jt, V[] o, double mx, Set<V> ok, CliqueScorer<V> cs)
	{
		super(jt,cs,false);
		map = new LinkedHashMap<V,Integer>();
		for (int i=0; i< o.length; i++)
			map.put(o[i],i);
		ord = o;
		max = mx;

		allowed = ok;
	}

	public int randomConnection()
	{
		if (jt.separators.isEmpty())
			return 1;

		Separator<V> S = jt.separators.next();

		Clique<V> Cx = S.getX();
		V x = Cx.next();
		while (S.contains(x)) 
			x = Cx.next();

		Clique<V> Cy = S.getY();
		V y = Cy.next();
		while (S.contains(y)) 
			y = Cy.next();

		if (!(allowed.contains(x) && allowed.contains(y)))
			return 1;

		if (Math.abs(rank(x)-rank(y)) > max)
			return 1;

		double Aij = jt.separators.size() * (Cx.size()-S.size()) * (Cy.size()-S.size());

		if (Cx.size() == S.size() + 1)
		{
			if (Cy.size() == S.size() + 1)
			{
				Aij /= (jt.cliques.size()-1) * (S.size()+2) * (S.size()+1) / 2.0;

				for (Clique<V> N : jt.getNeighbours(Cx))
					if (N != Cy && !N.contains(x))
						Aij /= 2;

				for (Clique<V> N : jt.getNeighbours(Cy))
					if (N != Cx && !N.contains(y))
						Aij /= 2;
			}
			else
			{
				Aij /= jt.cliques.size() * (S.size()+2) * (S.size()+1) / 2.0;
			}
		}
		else
		{
			if (Cy.size() == S.size() + 1)
			{
				Aij /= jt.cliques.size() * (S.size()+2) * (S.size()+1) / 2.0;
			}
			else
			{
				Aij /= (jt.cliques.size()+1) * (S.size()+2) * (S.size()+1) / 2.0;
			}
		}

		if (Math.random() > Aij)
			return 3;

		if (Math.log(Math.random()) * temperature() > deltaScore(S,x,y))
			return 4;

                return proposeConnection(x,y,Cx,Cy,S);
        }

	public int randomDisconnection()
	{
		if (jt.cliques.isEmpty())
			return 1;

		Clique<V> Cxy = jt.cliques.next();
		if (Cxy.size() < 2)
			return 2;

		V x = Cxy.next();
		V y = Cxy.next();
		while (y == x) 
			y = Cxy.next();

		if (!(allowed.contains(x) && allowed.contains(y)))
			return 1;

		int N = 0;
		int Nx = 0;
		int Ny = 0;
		Clique<V> Cx = null;
		Clique<V> Cy = null;

		Cxy.remove(x);
		Cxy.remove(y);

		for (Clique<V> P : jt.getNeighbours(Cxy))
		{
			if (P.contains(x))
			{
				if (P.contains(y))
				{
					Cxy.add(x);
					Cxy.add(y);
					return 3;
				}
				else
				{
					Nx++;
					if (jt.connection(P,Cxy).containsAll(Cxy))
						Cx = P;
				}
			}
			else
			{
				if (P.contains(y))
				{
					Ny++;
					if (jt.connection(P,Cxy).containsAll(Cxy))
						Cy = P;
				}
				else
				{
					N++;
				}
			}
		}

		Cxy.add(x);
		Cxy.add(y);

		double Aij = jt.cliques.size() * Cxy.size() * (Cxy.size() - 1) / 2.0;

		if (Cx == null)
		{
			if (Cy == null)
			{
				Aij /= jt.separators.size()+1;
				Aij /= Math.pow(0.5,N);
			}
			else
			{
				if (Ny > 1)
					return 4;
				Aij /= jt.separators.size() * (Cy.size() - Cxy.size() + 2);
			}
		}
		else
		{
			if (Cy == null)
			{
				if (Nx > 1)
					return 5;
				Aij /= jt.separators.size() * (Cx.size() - Cxy.size() + 2);
			}
			else
			{
				if (Nx > 1 || Ny > 1 || N > 0)
					return 6;
				Aij /= (jt.separators.size()-1) * (Cx.size() - Cxy.size() + 2) * (Cy.size() - Cxy.size() + 2);
			}
		}

		if (Math.random() > Aij)
			return 7;

		if (Math.log(Math.random()) * temperature() > deltaScore(Cxy,x,y))
			return 9;

		return proposeDisconnection(x,y,Cx,Cy,Cxy);
	}

// Private data and methods.

	private V[] ord = null;
	private Map<V,Integer> map = null;
	protected double max = 0;
	protected Set<V> allowed = null;

	protected int rank(V x)
	{
		return map.get(x).intValue();
	}

	protected V ranked(int i)
	{
		if (i < 0 || i >= ord.length)
			return null;
		return ord[i];
	}

	protected int topRank(Clique<V> s)
	{
		int r = rank(s.next())+1;
		for ( ; r < ord.length && s.contains(ord[r]); r++);
		return r-1;
	}

	protected V bot(Clique<V> s)
	{
		if (s.isEmpty())
			return null;

		V x = s.next();
		int r = rank(x) - 1;
		while (r >= 0 && s.contains(ord[r]))
		{
			x = ord[r];
			r--;
		}
		return x;
	}

	protected V top(Clique<V> s)
	{
		if (s.isEmpty())
			return null;

		V x = s.next();
		int r = rank(x) + 1;
		while (r < ord.length && s.contains(ord[r]))
		{
			x = ord[r];
			r++;
		}
		return x;
	}
}
