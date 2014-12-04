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

import jpsgcs.alun.graph.Point;
import jpsgcs.alun.animate.MouseKeyListener;
import jpsgcs.alun.animate.Loop;
import jpsgcs.alun.graph.Graphs;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.LinkedHashSet;

public class GraphListener<V,E> extends MouseKeyListener
{
	public GraphListener(GraphAnimator<V,E> ga)
	{
		a = ga;
	}

	public void keyPressed(KeyEvent e)
	{
		if (!e.isShiftDown())
			return;

 		switch(e.getKeyCode())
		{
		case KeyEvent.VK_DOWN:
			scale(a.getGraph().getPoints(), scaledown, scaledown);
			break;
		case KeyEvent.VK_UP:
			scale(a.getGraph().getPoints(), scaleup, scaleup);
			break;
		case KeyEvent.VK_LEFT:
			rotate(a.getGraph().getPoints(), -rotation);
			break;
		case KeyEvent.VK_RIGHT:
			rotate(a.getGraph().getPoints(), +rotation);
			break;
		case KeyEvent.VK_DELETE:
			a.getLoop().flip();
			break;
		}

		a.getCanvas().repaint();
	}

	public void mouseClicked(MouseEvent e)
	{
		if (!e.isControlDown())
		{
			if (e.getClickCount() == 2)
			{
				if (e.isShiftDown())
					a.getLoop().flip();
				else
					a.getLocator().set(a.getGraph());
			}
			a.getCanvas().repaint();
			return;
		}

		if (e.getClickCount() == 2)
		{
			Collection<V> all = a.getGraph().completeGraph().getVertices();
			switch(button(e))
			{
				case 1: a.getGraph().show(all);
					break;
				case 2: a.getGraph().hide(all);
					break;
			}
			a.getCanvas().repaint();
			return;
		}

		V v = a.getGraph().find(e.getX(),e.getY());
		if (v == null)
			return;
		
		Collection<V> p = null;

		switch(button(e))
		{
		case 3: return;

		case 2: if (e.isShiftDown())
				p = Graphs.component(a.getGraph(),v);
			else
			{
				p = new LinkedHashSet<V>();
				p.add(v);
			}

			if (p == null)
				return;
			a.getGraph().hide(p);
			break;

		case 1: if (e.isShiftDown())
				p = Graphs.component(a.getGraph().completeGraph(),v);
			else
				p = a.getGraph().completeGraph().getNeighbours(v);
			
			if (p == null)
				return;
			a.getGraph().show(p);
			break;
		}

		a.getCanvas().repaint();
 	}
/*
	public void mouseClicked(MouseEvent e)
	{
		switch(button(e))
		{
		case 3:
			if (e.isShiftDown())
				ed.peelAll();
			else if (e.isControlDown())
				ed.peel(v,true);
			else
				ed.peel(v,false);
			ed.flash();
			break;
		}
 	}
*/

	public void mouseReleased(MouseEvent e)
	{
		if (e.isControlDown())
			return;

		if (points == null)
			return;

		switch(button(e))
		{
		case 1: fix(points,true);
		case 2:
		case 3: 
			shift(points, e.getX()-x, e.getY()-y);
		}

		points = null;
		a.getCanvas().repaint();
	}

	public void mousePressed(MouseEvent e)
	{
		if (e.isControlDown())
			return;

		x = e.getX();
		y = e.getY();
		V v = a.getGraph().find(x,y);

		switch(button(e))
		{
		case 1: 
		case 2: if (v != null)
			{
				if (e.isShiftDown())
					points = a.getGraph().getShownComponentPoints(v);
				else
					points = a.getGraph().getPoints(v);
				fix(points,false);
			}
			break;

		case 3: if (e.isShiftDown())
				points = a.getGraph().getShownPoints();
			break;
		}
	}

	public void mouseDragged(MouseEvent e)
	{
		if (e.isControlDown())
			return;

		if (points == null)
			return;

		switch(button(e))
		{
		case 1:
		case 2:
		case 3:
			shift(points,e.getX()-x, e.getY()-y);
		}

		x = e.getX();
		y = e.getY();
		a.getCanvas().repaint();
	}

// Private data.

	private GraphAnimator<V,E> a = null;
	private double scaleup = 10.0/9.0;
	private double scaledown = 9.0/10.0;
	private double rotation = Math.PI/12.0;
	private double x = 0;
	private double y = 0;
	private Collection<Point> points = null;

	private void shift(Collection<Point> c, double s, double t)
	{
		for (Point p : c)
		{
			p.x += s;
			p.y += t;
		}
	}

	private void scale(Collection<Point> c, double s, double t)
	{
		for (Point p : c)
		{
			p.x *= s;
			p.y *= t;
		}
	}

	private void rotate(Collection<Point> c, double s)
	{
		for (Point p : c)
		{
			double  r = Math.sqrt(p.x*p.x + p.y*p.y);
			double t = Math.atan2(p.y,p.x);
			t += s;
			p.x = r * Math.cos(t);
			p.y = r * Math.sin(t);
		}
	}

	private void fix(Collection<Point> c, boolean b)
	{
		for (Point p : c)
		{
			p.m = b;
		}
	}
}
