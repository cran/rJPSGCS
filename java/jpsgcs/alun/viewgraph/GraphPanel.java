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

import jpsgcs.alun.graph.GraphLocator;
import jpsgcs.alun.graph.Parameter;
import jpsgcs.alun.animate.ScrollWidget;
import java.awt.Panel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class GraphPanel<V,E> extends Panel
{
	public GraphPanel(PaintableGraph<V,E> g, GraphLocator<V,E> loc)
	{
		loc.set(g);

		GraphAnimator<V,E> ann = new GraphAnimator<V,E>(g,loc);
		setLayout(new BorderLayout());
		add(ann.getCanvas(),BorderLayout.CENTER);

		ann.getLoop().start();

		makeScrollPanel(loc.getParameters());
	}

	public void makeScrollPanel(Parameter[] par)
	{
		Panel p = new Panel();
		p.setLayout(new GridLayout(par.length,1));
		
		for (int i=0; i<par.length; i++)
			p.add(new ParameterScrollWidget(par[i]).getPanel());

		add(p,BorderLayout.SOUTH);
	}
}
