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

import jpsgcs.alun.util.Pair;

import java.util.Collection;
import java.util.Set;
import java.util.LinkedHashSet;

public class OnePairSampler<V> extends AbstractJTreeSampler<V>
{
	private double storedEnum = 0;
	protected boolean uniformDecom = false;

	public OnePairSampler(JTree<V> jt, CliqueScorer<V> cs, boolean u)
	{
		super(jt,cs);
		uniformDecom = u;
	
		if (uniformDecom)
			storedEnum = jt.logEnumerate();
	}

	public int proposeDisconnection(V x, V y, Clique<V> Cx, Clique<V> Cy, Clique<V> Cxy)
	{
		if (!uniformDecom)
		{
			disconnect(x,y,Cx,Cy,Cxy);
			return 0;
		}

		Separator<V> Sxy = disconnect(x,y,Cx,Cy,Cxy);
		double newEnum = jt.logEnumerate();

		if (Math.log(Math.random()) > storedEnum - newEnum)
		{
			Pair<Clique<V>,Clique<V>> p = jt.cliquePair(x,y,Sxy);
			connect(x,y,p.x,p.y,Sxy);
			return 9;
		}

		storedEnum = newEnum;
		return 0;
	}

	public int proposeConnection(V x, V y, Clique<V> Cx, Clique<V> Cy, Separator<V> S)
	{
		if (!uniformDecom)
		{
			connect(x,y,Cx,Cy,S);
			return 0;
		}

		Clique<V> Cxy = connect(x,y,Cx,Cy,S);
		double newEnum = jt.logEnumerate();
	
		if (Math.log(Math.random()) > storedEnum - newEnum)
		{
			Pair<Clique<V>,Clique<V>> p = jt.cliquePair(x,y,Cxy);
			disconnect(x,y,p.x,p.y,Cxy);
			return 5;
		}

		storedEnum = newEnum;
		return 0;
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
}
