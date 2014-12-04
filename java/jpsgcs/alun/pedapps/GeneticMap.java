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

import jpsgcs.alun.genio.BasicGeneticData;
import jpsgcs.alun.util.InputFormatter;
import jpsgcs.alun.util.CartesianPoint;
import jpsgcs.alun.util.LinearCalibrator;
import java.util.Vector;
import java.io.IOException;
import java.util.Vector;

public class GeneticMap extends LinearCalibrator
{
	public GeneticMap(double[] physical, double[] genetic)
	{
		super(physical,genetic);
	}

	public static GeneticMap readMap(String fl) throws IOException
	{
		InputFormatter f = new InputFormatter(fl);
		Vector<CartesianPoint> v = new Vector<CartesianPoint>();

		while (f.newLine())
		{
			f.nextString();
			f.nextString();

			v.add(new CartesianPoint(f.nextDouble(),f.nextDouble()));
		}

		double[] x = new double[v.size()];
		double[] y = new double[v.size()];
		for (int i=0; i<x.length; i++)
		{
			x[i] = v.get(i).y;
			y[i] = v.get(i).x;

			if (i > 0)
			{
				if (x[i] < x[i-1])
					throw new RuntimeException("Physical distance array decreasing at line "+i);
				if (y[i] < y[i-1])
					throw new RuntimeException("Genetic distance array decreasing at line "+i);
			}
		}

		return new GeneticMap(x,y);
	}

	static public double[] locusCentiMorgans(BasicGeneticData d, boolean usefirst)
	{
		int first = usefirst ? 0 : 1;
		double[] cm = new double[d.nLoci()-first];
		for (int i=1; i<cm.length; i++)
		{
			double dist = 0;
			dist += GeneticMap.thetaTocMKosambi(d.getMaleRecomFrac(i+first,i-1+first));
			dist += GeneticMap.thetaTocMKosambi(d.getFemaleRecomFrac(i+first,i-1+first));
			dist /= 2.0;
			cm[i] = cm[i-1] + dist;
		}
		return cm;
	}

	static public double[] locusCentiMorgans(BasicGeneticData d, boolean usefirst, int inter)
	{
		double[] cm = locusCentiMorgans(d,usefirst);
		Vector<Double> v = new Vector<Double>();

/*
		for (int i=inter; i>=0; i--)
			v.add(new Double(cm[0] - thetaTocMKosambi(i * 0.25 / inter)));
*/

		v.add(new Double(cm[0]));

		for (int j=1; j<cm.length; j++)
			for (int i=inter-1; i>=0; i--)
				v.add(new Double(cm[j] - i*(cm[j]-cm[j-1])/inter));

/*
		for (int i=1; i<=inter; i++)
			v.add(new Double(cm[cm.length-1] + thetaTocMKosambi(i*0.25/inter)));
*/
			
		double[] pos = new double[v.size()];
		for (int i=0; i<pos.length; i++)
			pos[i] = v.get(i).doubleValue();
			
		return pos;
	}

	public double centiMorgan(long x)
	{
		return y(x);
	}

	public long basepair(double y)
	{
		return (long) x(y);
	}

	public static double cMToThetaKosambi(double x)
	{
		return 0.5 * Math.tanh(2*x/100.0);
	}

	public static double thetaTocMKosambi(double t)
	{
		return 100 * 0.25 * ( Math.log(1+2*t) - Math.log(1-2*t) );
	}

	public static double sumTheta(double x, double y)
	{
		double z = thetaTocMKosambi(x) + thetaTocMKosambi(y);
		return cMToThetaKosambi(z);
	}

	public static void main(String[] args)
	{
		double[] c = {0.1, 1, 5, 10, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000} ;

		for (int i=0; i<c.length; i++)
		{
			System.err.print(c[i]+"\t");
			double x = cMToThetaKosambi(c[i]);
			System.err.print(x+"\t");
			double y = thetaTocMKosambi(x);
			System.err.println(y);
		}
	}
}
