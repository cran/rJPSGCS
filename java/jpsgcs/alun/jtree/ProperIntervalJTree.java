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
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.LinkedHashMap;

public class ProperIntervalJTree<V> extends OrderedJTree<V>
{
	public ProperIntervalJTree(JTree<V> jt, V[] o, double max, Set<V> ok)
	{
		this(jt,o,max,ok,null);
	}

	public ProperIntervalJTree(JTree<V> jt, V[] o, double mx, Set<V> ok, CliqueScorer<V> cs)
	{
		super(jt,o,mx,ok,cs);
		uniformDecom = false;
	}

	public void randomize()
	{
	}

	protected Clique<V> arbitraryChoice(Clique<V> P, Clique<V> Cxy, Clique<V> Cx, Clique<V> Cy)
	{
		return topRank(P) < topRank(Cxy) ? Cx : Cy;
	}

	public int randomConnection()
	{
		if (jt.separators.isEmpty())
			return 1;

		Separator<V> S = jt.separators.next();

		Clique<V> Cx = S.getX();
		Clique<V> Cy = S.getY();
		if (topRank(Cx) > topRank(Cy))
		{
			Clique<V> temp = Cx;
			Cx = Cy;
			Cy = temp;
		}

		int r = topRank(Cx);
		while (S.contains(ranked(r)))
			r--;
		V x = ranked(r);
		r = r+1;
		while (S.contains(ranked(r)))
			r++;
		V y = ranked(r); 


		if (!(allowed.contains(x) && allowed.contains(y)))
			return 1;

		if (Math.abs(rank(x)-rank(y)) > max)
			return 1;

		double Aij = jt.separators.size();

		if (Cx.size() == S.size() + 1)
		{
			if (Cy.size() == S.size() + 1)
			{
				Aij /= (jt.cliques.size()-1);
			}
			else
			{
				Aij /= jt.cliques.size();
			}
		}
		else
		{
			if (Cy.size() == S.size() + 1)
			{
				Aij /= jt.cliques.size();
			}
			else
			{
				Aij /= (jt.cliques.size()+1);
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

		V x = bot(Cxy);
		V y = top(Cxy);

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
					// Shouldn't get here.
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

		double Aij = jt.cliques.size();

		if (Cx == null)
		{
			if (Cy == null)
			{
				Aij /= jt.separators.size()+1;
			}
			else
			{
				if (Ny > 1)
				{
					// Shouldn't get here.
					return 4;
				}
				Aij /= jt.separators.size();
			}
		}
		else
		{
			if (Cy == null)
			{
				if (Nx > 1)
				{
					// Shouldn't get here.
					return 5;
				}
				Aij /= jt.separators.size();
			}
			else
			{
				if (Nx > 1 || Ny > 1 || N > 0)
				{
					// Shouldn't get here.
					return 6;
				}
				Aij /= (jt.separators.size()-1);
			}
		}

		if (Math.random() > Aij)
			return 7;

		if (Math.log(Math.random()) * temperature() > deltaScore(Cxy,x,y))
			return 9;

		return proposeDisconnection(x,y,Cx,Cy,Cxy);
	}
}
