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

public class StringNode implements VertexRepresentation
{
	public StringNode(String s)
	{
		this(s,Color.yellow);
	}

	public StringNode(String s, Color c)
	{
		this(s,c, Color.black, Color.black, Color.red, 0);
	}

	public StringNode(String s, Color cbackground, Color ctext, Color cborder0, Color cborder1, int h)
	{
		name = s;

		background = cbackground;
		text = ctext;
		border0 = cborder0;
		border1 = cborder1;
		
		shape = h;
	}
	
	public void paint(Graphics g, double dx, double dy, boolean b)
	{
		if (!sizeset)
			setSize(g);

		int x = (int)(dx-w);
		int y = (int)(dy-h);
		
		g.setColor(background);

		switch(shape)
		{
		case 0: g.fillRect(x,y,2*w,2*h); 
			break;
		case 1: g.fillOval(x,y,2*w,2*h); 
			break;
		}

		g.setColor( b ? border0 : border1);

		switch(shape)
		{
		case 0: g.drawRect(x,y,2*w,2*h); 
			break;
		case 1: g.drawOval(x,y,2*w,2*h); 
			break;
		}

		g.setColor(text);
		g.drawString(name,x+xfill,y+2*(h-yfill));
	}
	
	public boolean contains(double a, double b)
	{
		if (a < -w || a > w)
			return false;
		if (b < -h || b > h)
			return false;
		return true;
	}

// Private data.

	private String name = null;
	private Color background = null;
	private Color text = null;
	private Color border0 = null;
	private Color border1 = null;

	private int shape = 0;
	private int w = 8;
	private int h = 8;
	private boolean sizeset = false;
	private int xfill = 4;
	private int yfill = 2;

	private void setSize(Graphics g)
	{
		w = xfill + g.getFontMetrics().stringWidth(name) / 2;
		h = yfill + g.getFontMetrics().getHeight() / 2;
		sizeset = true;
	}
}
