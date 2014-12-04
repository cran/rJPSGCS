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
 This general method of generating replicates from a density function 
 uses  different Generalised Exponential envelopes through the range.
*/
public class ExponentialSwitching extends Rejection
{
/**
 Constructs a new Exponential switching scheme for the given random variable.
 The random variable must have a density function which is concave throughout
 its range.
 The points given in array are used to construct the envelope.
*/
	public ExponentialSwitching(LogConcave X, double[] a)
	{
		this(X,makePoints(X,a));
	}

	public ExponentialSwitching(LogConcave X, Point[] p)
	{
		super(X,makeEnvelope(X,p),1);
		v = new Vector<Point>();
		for (int i=0; i<p.length; i++)
			v.addElement(p[i]);
	}

/**
 Returns an exponential mixture envelope for the given random variable,
 using the values in the given array to generate the switch points.
*/
	protected static Point[] makePoints(LogConcave R, double[] a)
	{
		double lo = R.lowerBound();
		double hi = R.upperBound();
		for (int i=0; i<a.length; i++)
			if (a[i] < lo || a[i] > hi)
				throw new ParameterException("Exponential Switching switch point out of range.");
		
		if (a.length < 3)
			throw new ParameterException("Exponential Switching: must have at least 3 point to construct an envelope");

		Point[] p = new Point[a.length];
		for (int i=0; i<a.length; i++)
			p[i] = new Point(a[i],R.logDensityFunction(a[i]));

		return p;
	}

/**
 Returns an exponential mixture envelope for the given random variable,
 using the points in the given array as the switch points.
*/
	protected static ExponentialMixture makeEnvelope(LogConcave R, Point[] p)
	{
		GeneralExponentialRV[] e = null;
		if (R instanceof DifferentiableLogConcave)
			e = tangentEnvelope((DifferentiableLogConcave)R,p);
		else
			e = chordEnvelope(R,p);
		return new ExponentialMixture(e);
	}

/**
 Returns an array of GeneralExponential random variables constructed from tangents
 to the log density.
*/
	private static GeneralExponentialRV[] tangentEnvelope(DifferentiableLogConcave R, Point[] p)
	{
		int n = p.length;
		Line[] l = new Line[n];
		for (int i=0; i<n; i++)
			l[i] = new Line(p[i],R.derivativeLogDensity(p[i].x));

		GeneralExponentialRV[] X = new GeneralExponentialRV[n];

		Point pp = l[0].intersection(l[1]);
		X[0] = new GeneralExponentialRV(l[0].a,l[0].b,R.lowerBound(),pp.x);
		for (int i=1; i<n-1; i++)
		{
			Point qq = pp;
			pp = l[i].intersection(l[i+1]);
			X[i] = new GeneralExponentialRV(l[i].a,l[i].b,qq.x,pp.x);
		}
		X[n-1] = new GeneralExponentialRV(l[n-1].a,l[n-1].b,pp.x,R.upperBound());

		return X;
	}

/**
 Returns an array of General Exponential random variables constructed from chords
 of the log density.
*/
	private static GeneralExponentialRV[] chordEnvelope(LogConcave R, Point[] p) 
	{
		int n = p.length;
		int m = n-1;
		Line[] l = new Line[m];
		for (int i=1; i<n; i++)
			l[i-1] = new Line(p[i-1],p[i]);

		Point[] q = new Point[2*n-3];
		int k = 0;
		q[k++] = p[0];
		for (int i=1; i<n-2; i++)
		{
			q[k++] = p[i];
			q[k++] = l[i-1].intersection(l[i+1]);
		}
		q[k++] = p[n-2];
		q[k++] = p[n-1];

		GeneralExponentialRV[] X = new GeneralExponentialRV[k+1];
		X[0] = new GeneralExponentialRV(l[0].a,l[0].b,R.lowerBound(),q[0].x);
		X[1] = new GeneralExponentialRV(l[1].a,l[1].b,q[0].x,q[1].x);
		for (int i=2; i<k-1; i+=2)
		{
			int j = i/2-1;
			X[i] = new GeneralExponentialRV(l[j].a,l[j].b,q[i-1].x,q[i].x);
		}
		for (int i=3; i<k; i+=2)
		{
			int j = (i+1)/2;
			X[i] = new GeneralExponentialRV(l[j].a,l[j].b,q[i-1].x,q[i].x);
		}
		X[k-1] = new GeneralExponentialRV(l[m-2].a,l[m-2].b,q[k-2].x,q[k-1].x);
		X[k] = new GeneralExponentialRV(l[m-1].a,l[m-1].b,q[k-1].x,R.upperBound());

		return X;
	}

	protected Vector<Point> v = null;
}
