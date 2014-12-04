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


package jpsgcs.alun.animate;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Dimension;

public class BufferedCanvas extends Canvas
{
	public BufferedCanvas(Paintable p)
	{
		painter = p;
	}

	public void paint(Graphics g)
	{
		if (painter != null)
			painter.paint(g);
	}

	public void update(Graphics g)
	{
		// Create a buffer image.
		Dimension d = getSize();
		if (ima == null || ima.getHeight(null)!=d.height || ima.getWidth(null)!=d.width)
			ima = createImage(d.width,d.height);

		// Draw a background to the image.
		Graphics gg = ima.getGraphics();
		gg.setColor(getBackground());
		gg.fillRect(0,0,d.width,d.height);

		// Paint the background image.
		paint(gg);
			
		// Flash the image to the screen.
		g.drawImage(ima,0,0,null);

		// Clean up.
		gg.dispose();
	}

	public Paintable getPainter()
	{
		return painter;
	}

	public void setPainter(Paintable p)
	{
		painter = p;
	}

// Private data.

	private Image ima = null;
	private Paintable painter = null;
}
