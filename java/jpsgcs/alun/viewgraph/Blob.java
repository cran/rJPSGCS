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

import java.awt.Color;
import java.awt.Graphics;

public class Blob implements VertexRepresentation
{
	public boolean contains(double a, double b)
	{
		if (a < -h || a > h)
			return false;
		if (b < -h || b > h)
			return false;
		return true;
	}
	
	public void paint(Graphics g, double dx, double dy, boolean b)
	{
		int x = (int)(dx-w);
		int y = (int)(dy-h);
		g.setColor(col);
		g.fillOval( x-2, y-2, 2*w+4, 2*h+4 );
		g.setColor(b? Color.black: Color.red);
		g.drawOval( x-2, y-2, 2*w+4, 2*h+4 );
	}

	public void setColor(Color c)
	{
		col = c;
	}

	public void setSize(int i, int j)
	{
		w = i;
		h = j;
	}

// Private data.

	private Color col = Color.yellow;
	private int w = 8;
	private int h = 8;
}
