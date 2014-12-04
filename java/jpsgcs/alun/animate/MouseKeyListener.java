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

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 A template class to handle mouse events, a combination of 
 a mouse adapter, a mouse motion adapter, and a KeyListener.
*/

public class MouseKeyListener implements MouseListener, MouseMotionListener, KeyListener
{
	public void mouseEntered(MouseEvent e) { }

	public void mouseExited(MouseEvent e) { }

	public void mouseClicked(MouseEvent e) { }

	public void mousePressed(MouseEvent e) { }

	public void mouseReleased(MouseEvent e) { }

	public void mouseMoved(MouseEvent e) { }

	public void mouseDragged(MouseEvent e) { }

	public void keyReleased(KeyEvent e) { }

	public void keyTyped(KeyEvent e) { }

	public void keyPressed(KeyEvent e) { }

	protected int button(MouseEvent e)
	{
                if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) >0)
		{
/*
			if (e.isControlDown())
			{
				if (e.isAltDown())
					return 3;
				else
					return 2;
			}
			else
*/
                        	return 1;
		}
                if ((e.getModifiers() & MouseEvent.BUTTON2_MASK) >0)
                        return 2;
                if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) >0)
                        return 3;
                return 0;
        }
}
