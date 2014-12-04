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

import jpsgcs.alun.util.SafeRunnable;
import jpsgcs.alun.graph.Graph;
import jpsgcs.alun.graph.GraphLocator;
import jpsgcs.alun.animate.ActiveCanvas;
import jpsgcs.alun.animate.Loop;
import java.awt.Panel;
import java.awt.Frame;
import java.awt.Color;
import java.util.ConcurrentModificationException;

public class GraphAnimator<V,E> implements SafeRunnable
{
	public GraphAnimator(PaintableGraph<V,E> g, GraphLocator<V,E> m)
	{
		loop = new Loop(this);
		graph = g;
		mover = m;
		delay = 40;
		
		canv = new ActiveCanvas(graph);
		canv.setBackground(new Color(255,255,210));
		canv.setSize(1000,1000);

		canv.addMouseKeyListener(new GraphListener<V,E>(this));
	}

	public void loop()
	{
		try
		{
			mover.move(graph);
			canv.repaint();
			Thread.sleep(delay);
		}
		catch (ConcurrentModificationException e)
		{
	//		System.err.println("Caught in GraphAnimator.loop()");
	//		e.printStackTrace();
		}
		catch (Exception e)
		{
			System.err.println("Caught in GraphAnimator.loop()");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public ActiveCanvas getCanvas()
	{
		return canv;
	}

	public Loop getLoop()
	{
		return loop;
	}

	public PaintableGraph<V,E> getGraph()
	{
		return graph;
	}

	public void setGraph(PaintableGraph<V,E> g)
	{
		graph = g;
		canv.setPainter(graph);
	}

	public GraphLocator<V,E> getLocator()
	{
		return mover;
	}

	public void setLocator(GraphLocator<V,E> m)
	{
		mover = m;
	}

	public int getPause()
	{
		return delay;
	}

	public void setPause(int p)
	{
		delay = p;
	}

// Private data.

	private PaintableGraph<V,E> graph = null;
	private GraphLocator<V,E> mover = null;
	private ActiveCanvas canv = null;
	private Loop loop = null;
	private int delay = 40;
}
