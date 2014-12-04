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


import jpsgcs.alun.linkage.Linkage;
import jpsgcs.alun.genio.PedigreeData;
import jpsgcs.alun.pedapps.MarriageNodeGraph;
import jpsgcs.alun.viewgraph.VertexRepresentation;
import jpsgcs.alun.viewgraph.PaintableGraph;
import jpsgcs.alun.viewgraph.GraphPanel;
import jpsgcs.alun.graph.GraphLocator;
import jpsgcs.alun.graph.DAGLocator;
import jpsgcs.alun.animate.FrameQuitter;

import java.util.Map;
import java.util.LinkedHashMap;
import java.awt.Panel;
import java.awt.Frame;

/**
 	This is an interactive graphical program for viewing pedigree data.

<ul>
        Usage : <b> java ViewLinkPed < input.ped </b> </li>
</ul>
        where
<ul>
<li> <b> input.ped </b> is a LINKAGE pedigree file. </li>
</ul>
<p>
	The controls are similar to those for 
	<a href="ViewGraph.html"> ViewGraph. </a>
*/

public class ViewLinkPed
{
	public static void main(String[] args)
	{
		try
		{
			PedigreeData data = Linkage.readPedigreeData();
			Map<Integer,VertexRepresentation> map = new LinkedHashMap<Integer,VertexRepresentation>();
			MarriageNodeGraph g = new MarriageNodeGraph(data,map,false);
			
			PaintableGraph<Integer,Object> p = new PaintableGraph<Integer,Object>(g,map);
			GraphLocator<Integer,Object> l = new DAGLocator<Integer,Object>();

			Panel pan = new GraphPanel<Integer,Object>(p,l);
			Frame f = new Frame();
			f.addWindowListener(new FrameQuitter());
			f.add(pan);
			f.pack();
			f.setVisible(true);
		}
		catch (Exception e)
		{
			System.err.println("Caught in ViewLinkPed:main()");
			e.printStackTrace();
		}
	}
}
