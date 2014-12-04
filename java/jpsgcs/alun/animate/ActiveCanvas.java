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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ConcurrentModificationException;

public class ActiveCanvas extends BufferedCanvas
{
	public ActiveCanvas(Paintable p)
	{
		super(p);
		setSize(1000,1000);

		trans = new AffineTransform();
		Dimension d = getSize();
		trans.translate(d.width/2,d.height/2);
		addMouseKeyListener(new TransformListener(this,trans));
	}

	public void addMouseKeyListener(MouseKeyListener m)
	{
		addMouseListener(m);
		addMouseMotionListener(m);
		addKeyListener(m);
	}

	public void removeMouseKeyListener(MouseKeyListener m)
	{
		removeMouseListener(m);
		removeMouseMotionListener(m);
		removeKeyListener(m);
	}

	public Graphics getGraphics() 
	{
		Graphics2D g = (Graphics2D) super.getGraphics();
		g.transform(trans);
		return g;
	}

	public void update(Graphics g)
	{
		try
		{
			// Create a buffer image.
			Dimension d = getSize();
			if (ima == null || ima.getHeight(null)!=d.height || ima.getWidth(null)!=d.width)
				ima = createImage(d.width,d.height);
	
			// Draw a background to the image.
			Graphics gg = ima.getGraphics();
			gg.setColor(getBackground());
			gg.fillRect(0,0,d.width,d.height);
	
			// Transform the coordinates.
			((Graphics2D)gg).transform(trans);
	
			// Paint the background image.
			paint(gg);
				
			// Flash the image to the screen.
			super.getGraphics().drawImage(ima,0,0,null);
	
			// Clean up.
			gg.dispose();
		}
		catch (ConcurrentModificationException e)
		{
			//System.err.println("Caught in ActiveCanvas:update()");
			//e.printStackTrace();
		}
	}

	public void paint(Graphics g)
	{
		int bigint = 10000;
		g.setColor(new Color(255,155,20));
		g.drawLine(0,0,0,bigint);
		g.drawLine(0,0,bigint,0);
		g.drawLine(0,0,0,-bigint);
		g.drawLine(0,0,-bigint,0);
		super.paint(g);
	}

// Protected methods.

	protected void processMouseEvent(MouseEvent e)
	{
		super.processMouseEvent(transformed(e));
	}

	protected void processMouseMotionEvent(MouseEvent e)
	{
		super.processMouseMotionEvent(transformed(e));
	}

// Private data, methods and classes.

	private AffineTransform trans = null;
	private Image ima = null;

	private MouseEvent transformed(MouseEvent e)
	{
		try
		{ 
			Point2D p = new Point2D.Double(e.getX(),e.getY());
			trans.inverseTransform(p,p);
			return new MouseEvent
			(
				(Component)e.getSource(),
				e.getID(),
				e.getWhen(),
				e.getModifiers(),
				(int)p.getX(),
				(int)p.getY(),
				e.getClickCount(),
				e.isPopupTrigger()
			);
		}
		catch (NoninvertibleTransformException f)
		{
			System.err.println("Caught in ActiveCanvas:transformed()");
			f.printStackTrace();
		}
		return e;
	}
}
