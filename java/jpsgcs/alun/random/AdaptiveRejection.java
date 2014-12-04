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


package jpsgcs.alun.random;

import java.util.Vector;

/**
 This class implements exponential switching but with the envelope
 changing and getting tighter as new values of the target density
 give more information about its shape.
*/
public class AdaptiveRejection extends ExponentialSwitching
{
/**
 Creates a new adaptive rejection scheme.
*/
	public AdaptiveRejection(LogConcave R, double[] a)
	{
		super(R,a);
	}

/**
 Applies the rejection method and returns the result.
*/
	public double apply()
	{
		Point p = null;
		do 
		{
			p = ((ExponentialMixture)getEnvelope()).nextPoint();
			
			if (stillAdapting)
			{
				v.addElement(new Point(p.x,((LogConcave)getTarget()).logDensityFunction(p.x)));
				if (++increase >= jump)
				{
					updateEnvelope();
					increase = 0;
				}
			}

			ntried++;
		}
		while (U() > getTarget().densityFunction(p.x)/p.y);

		naccepted++;
		return p.x;
	}

/**
 Uses the new points collected to update the exponential mixture
 envelope.
*/
	private void updateEnvelope()
	{
		Point[] p = new Point[v.size()];
		p = (Point[])v.toArray(p);

		for (int i=1; i<p.length; i++)
			for (int j=i; j>0; j--)
				if (p[j].x < p[j-1].x)
				{
					Point t = p[j];
					p[j] = p[j-1];
					p[j-1] = t;
				}

		setEnvelope(makeEnvelope((LogConcave)getTarget(),p));
		stillAdapting = ntried < 20 ||  naccepted <= thresh * ntried;
	}

	private boolean stillAdapting = true;
	private double thresh = 0.95;
	private int jump = 10;
	private int increase = 0;
	private int naccepted = 0;
	private int ntried = 0;
}

