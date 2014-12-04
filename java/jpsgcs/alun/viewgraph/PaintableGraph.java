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


package jpsgcs.alun.viewgraph;

import jpsgcs.alun.graph.Graph;
import jpsgcs.alun.graph.LocatedMaskedGraph;
import jpsgcs.alun.animate.Paintable;
import jpsgcs.alun.graph.Point;
import java.util.Map;
import java.util.LinkedHashMap;
import java.awt.Graphics;
import java.awt.Color;

public class PaintableGraph<V,E> extends LocatedMaskedGraph<V,E> implements Paintable
{
	public PaintableGraph(Graph<V,E> g)
	{
		super(g);
		node = new LinkedHashMap<V,VertexRepresentation>();
		for (V v : g.getVertices())
			node.put(v,new StringNode(v.toString()));
	}

	public PaintableGraph(Graph<V,E> g, Map<V,VertexRepresentation> map)
	{
		super(g);
		node = new LinkedHashMap<V,VertexRepresentation>();
		for (V v : g.getVertices())
			node.put(v, map.get(v) == null ? new StringNode(v.toString()) : map.get(v) );
	}

	public V find(double x, double y)
	{
		for (V v: getVertices())
		{
			Point pv = getPoint(v);
			if (node.get(v).contains(pv.x - x, pv.y - y))
				return v;
		}
		return null;
	}

	public void paint(Graphics g)
	{
		for (V v: getVertices())
		{
			Point pv = getPoint(v);
			for (V u: outNeighbours(v))
			{
				Point pu = getPoint(u);
				if (isDirected())
					arrow(g,pv.x,pv.y,pu.x,pu.y);
				else
					line(g,pv.x,pv.y,pu.x,pu.y);
			}
		}

		for (V v: getVertices())
		{
			Point pv = getPoint(v);
			VertexRepresentation nv = node.get(v);
			nv.paint(g,pv.x,pv.y, getNeighbours(v).containsAll(completeGraph().getNeighbours(v)));
		}
	}

// Private data

	private Map<V,VertexRepresentation> node = null;

	private double arrbs = 6.0;
	private double arrht = 3.0;
	private double ends = 0.4;

	private void line(Graphics g, double x1, double y1, double x2, double y2)
	{
		g.setColor(Color.black);
		g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
	}

	private void arrow(Graphics g, double x1, double y1, double x2, double y2)
	{
		line(g,x1,y1,x2,y2);

		int sg = x1 < x2 ? 1 : -1;
		double th = Math.atan((y2-y1)/(x2-x1));
		double s = arrbs * Math.cos(th);
		double t = arrbs * Math.sin(th);
		double x = ends * x1 + (1-ends) * x2;
		double y = ends * y1 + (1-ends) * y2;
		int[] xx = {(int)(x-t), (int)(x+t), (int)(x + arrht*s*sg)};
		int[] yy = {(int)(y+s), (int)(y-s), (int)(y + arrht*t*sg)};

		g.setColor(Color.white);
		g.fillPolygon(xx,yy,3);
		g.setColor(Color.black);
		g.drawPolygon(xx,yy,3);
	}
}
