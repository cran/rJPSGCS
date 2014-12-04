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

import jpsgcs.alun.util.Pair;
import jpsgcs.alun.util.DoubleValue;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.LinkedHashSet;

public class UniformDecomposable<V> extends UniformJTree<V> 
{
	public UniformDecomposable(JTree<V> jtree)
	{
		this(jtree,null);
	}
	
	public UniformDecomposable(JTree<V> jtree, CliqueScorer<V> cs)
	{
		super(jtree,cs);
		storedEnum = jt.logEnumerate();
	}

	public int proposeConnection(V x, V y, Clique<V> Cx, Clique<V> Cy, Separator<V> S, double Aij)
	{
		if (Math.random() > Aij)
			return 3;

		if (Math.log(Math.random()) * temperature() > deltaScore(S,x,y))
			return 4;

		Clique<V> Cxy = connect(x,y,Cx,Cy,S);

		double newEnum = jt.logEnumerate();
	
		if (Math.log(Math.random()) > storedEnum - newEnum)
		{
			Pair<Clique<V>,Clique<V>> p = jt.cliquePair(x,y,Cxy);

			boolean[] pick = null;
			if (p.x == null && p.y == null)
			{
				pick = new boolean[jt.getNeighbours(Cxy).size()];
				for (int i=0; i<pick.length; i++)
					pick[i] = Math.random() < 0.5;
			}

			disconnect(x,y,p.x,p.y,Cxy,pick);

			return 5;
		}

		storedEnum = newEnum;

		return 0;
	}
	
	public int proposeDisconnection(V x, V y, Clique<V> Cx, Clique<V> Cy, Clique<V> Cxy, double Aij, boolean[] pick)
	{
		if (Math.random() > Aij)
			return 7;

		if (Math.log(Math.random()) * temperature() > deltaScore(Cxy,x,y))
			return 8;

		Separator<V> Sxy = disconnect(x,y,Cx,Cy,Cxy,pick);

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

// Private data.

	private double storedEnum = 0;
}
