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

import jpsgcs.alun.util.RadixPlaneSorter;
import java.util.Collection;
import java.util.LinkedHashSet;

public class DAGLocator<V,E> extends GraphLocator<V,E>
{
	public DAGLocator()
	{
		par = new Parameter[3];
		par[0] = new Parameter("X-Repulsion",0,500,100);
		par[1] = new Parameter("Gravity",0,10,0);
		par[2] = new Parameter("Y-Repulsion",0,500,100);
	}

	public double move(LocatedGraph<V,E> g)
	{
		double differ = 0;
		double d = par[0].getValue();
		double gamma = d*d;
		double alpha = d < Double.MIN_VALUE ? 0 : 1/3.0;
		d = par[1].getValue();
		double beta = d*d*d;
		double delta = par[2].getValue();
		
		Collection<V> vertices = new LinkedHashSet<V>(g.getVertices());

		RadixPlaneSorter<Point> p = null;
		if (alpha > Double.MIN_VALUE)
		{
			p = new RadixPlaneSorter<Point>(Math.sqrt(gamma),Math.sqrt(gamma),40);
			for (V a : vertices)
				p.add(g.getPoint(a));
		}	
			
		for (V a : vertices)
		{
			Point pa = g.getPoint(a);
			if (!pa.m)
				continue;

			Derivatives D = new Derivatives();
			
			addDerivatives(D, squaredAttractions(pa, g.getPoints(g.getNeighbours(a))), 1);

			if (p != null)
			{
				p.remove(pa);
				addDerivatives(D, localRepulsions(pa, p.getLocal(pa,Math.sqrt(gamma)), gamma), alpha);
			}

			if (beta > 0)
			{
				addDerivatives(D, verticalGenerations(pa, g.getPoints(g.outNeighbours(a)), delta), beta);
				addDerivatives(D, verticalGenerations(pa, g.getPoints(g.inNeighbours(a)), -delta), beta);
			}

			differ += update(pa,D);

			if (p != null)
				p.add(pa);
		}

		return differ;
        }
}
