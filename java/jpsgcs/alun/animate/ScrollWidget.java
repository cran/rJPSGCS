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

import java.awt.Scrollbar;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.TextField;
import java.awt.Panel;
import java.awt.BorderLayout;
import jpsgcs.alun.util.StringFormatter;

public class ScrollWidget extends Scrollbar implements AdjustmentListener
{
	public ScrollWidget(String label, double scl, double init)
	{
		super(Scrollbar.HORIZONTAL);
		setValues(500,100,0,1100);
		setBlockIncrement(100);
		setUnitIncrement(1);
		addAdjustmentListener(this);

		lab = new TextField("",20);
		lab.setEditable(false);
		setLabel(label);

		dp = 3;
		box = new TextField("",5);
		box.setEditable(false);
		scaleBy(scl);
		setValue((int)(init/scale));
		adjustmentValueChanged(null);

		pan = new Panel();
		pan.setLayout(new BorderLayout());
		pan.add(lab,BorderLayout.WEST);
		pan.add(this,BorderLayout.CENTER);
		pan.add(box,BorderLayout.EAST);
	}

	public void adjustmentValueChanged(AdjustmentEvent e)
	{
		box.setText(StringFormatter.format(getRealValue(),dp));
	}

	public void scaleBy(double s)
	{
		scale *= s;
		adjustmentValueChanged(null);
	}

	public void setLabel(String s)
	{
		lab.setText(s);
	}

	public Panel getPanel()
	{
		return pan;
	}

	public double getRealValue()
	{
		return getValue()*scale;
	}

	private double scale = 0.001;
	private TextField lab = null;
	private Panel pan = null;
	private TextField box = null;
	private int dp = 3;
}
