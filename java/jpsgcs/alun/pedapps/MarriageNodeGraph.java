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


package jpsgcs.alun.pedapps;

import jpsgcs.alun.genio.PedigreeData;
import jpsgcs.alun.graph.Network;
import jpsgcs.alun.viewgraph.StringNode;
import jpsgcs.alun.viewgraph.Blob;
import jpsgcs.alun.viewgraph.VertexRepresentation;

import java.util.Map;
import java.awt.Color;

public class MarriageNodeGraph extends Network<Integer,Object>
{
	public MarriageNodeGraph(PedigreeData ped, Map<Integer,VertexRepresentation> map)
	{
		this(ped,map,false);
	}

	public MarriageNodeGraph(PedigreeData ped, Map<Integer,VertexRepresentation> map, boolean usepedid)
	{
		super(true,false);
		int[][] fam = ped.nuclearFamilies();

		for (int i=0; i<fam.length; i++)
		{
			connect(fam[i][0],mar(i));
			connect(fam[i][1],mar(i));
			for (int k=2; k<fam[i].length; k++)
				connect(mar(i),fam[i][k]);
		}

		for (int i=0; i<fam.length; i++)
		{
			Blob mar = new Blob();
			mar.setColor(Color.white);
			mar.setSize(3,3);
			map.put(mar(i),mar);
			
			VertexRepresentation pa = map.get(fam[i][0]);
			if (pa == null)
			{
				pa = new StringNode(ind(ped,fam[i][0],usepedid),Color.cyan);
				map.put(fam[i][0],pa);
			}

			VertexRepresentation ma = map.get(fam[i][1]);
			if (ma == null)
			{
				ma = new StringNode(ind(ped,fam[i][1],usepedid),Color.yellow);
				map.put(fam[i][1],ma);
			}
		}

		for (int i=0; i<fam.length; i++)
		{
			for (int k=2; k<fam[i].length; k++)
			{
				VertexRepresentation kid = map.get(fam[i][k]);
				if (kid == null)
				{
					kid = new StringNode(ind(ped,fam[i][k],usepedid),Color.white);
					map.put(fam[i][k],kid);
				}
			}
		}
	}

	private String ind(PedigreeData ped, int i, boolean usepedid)
	{
		if (usepedid)
			return ped.pedigreeName(i)+" "+ped.individualName(i);
		else
			return ped.individualName(i);
	}

	private int mar(int i)
	{
		return -(i+1);
	}
}
