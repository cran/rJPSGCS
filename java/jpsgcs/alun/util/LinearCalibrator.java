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


package jpsgcs.alun.util;

/**
 This class represents a piecewise monotonic increasing
 function that calibrates from one axis to another.
*/
public class LinearCalibrator
{
/**
 Creates a new callibration by interpolating linearly between the 
 points specified by the xx and yy arrays.
 Points outside of the range determined by these arrays are calibrated
 by a straightline determined by the overall slope.
*/
	public LinearCalibrator(double[] xx, double[] yy)
	{
		if (xx.length != yy.length)
			throw new RuntimeException("Interpolation error: x, y length miss match");
		if (xx.length < 2)
			throw new RuntimeException("Interpolation error: not enough x, y points");
		for (int i=1; i<xx.length; i++)
			if (xx[i] < xx[i-1])
				throw new RuntimeException("Interpolation error: x array is decreasing");
		for (int i=1; i<yy.length; i++)
			if (yy[i] < yy[i-1])
				throw new RuntimeException("Interpolation error: y array is decreasing");

		x = new double[xx.length];
		y = new double[x.length];
		for (int i=0; i<x.length; i++)
		{
			x[i] = xx[i];
			y[i] = yy[i];
		}
	}

/**
 Returns the point in y space corresponding to a point given in x space.
*/
	public double y(double xx)
	{
		int m = y.length-1;
		int d = 10;
		if (d > m)
			d = m;

		if (xx < x[0])
			//return y[0]+(y[1]-y[0])/(x[1]-x[0])*(xx-x[0]);
			//return y[0]+(y[m]-y[0])/(x[m]-x[0])*(xx-x[0]);
			return y[0]+(y[d]-y[0])/(x[d]-x[0])*(xx-x[0]);

		else if (xx > x[m])
			//return y[m]+(y[m]-y[m-1])/(x[m]-x[m-1]) * (xx-x[m]);
			//return y[m]+(y[m]-y[0])/(x[m]-x[0]) * (xx-x[m]);
			return y[m]+(y[m]-y[m-d])/(x[m]-x[m-d]) * (xx-x[m]);
		
		int k = 0;
		int klo = 0;
		int khi = x.length-1;
		while (khi-klo > 1)
		{
			k = (khi+klo)/2;
			if (x[k] > xx)
				khi = k;
			else
				klo = k;
		}

      		double h = x[khi] - x[klo];
                if (h == 0.0)
                        return (x[khi]+x[klo])/2.0;

                double a = (x[khi] - xx)/h;
                double b = (xx - x[klo])/h;

		return a*y[klo] + b*y[khi];
	}

/**
 Returns the point in x space corresponding to a given point in y space.
*/
	public double x(double yy)
	{
		int m = y.length-1;
		int d = 10;
		if (d > m)
			d = m;

		if (yy < y[0])
			//return x[0]+(x[1]-x[0])/(y[1]-y[0])*(yy-y[0]);
			//return x[0]+(x[m]-x[0])/(y[m]-y[0])*(yy-y[0]);
			return x[0]+(x[d]-x[0])/(y[d]-y[0])*(yy-y[0]);
		else if (yy > y[m])
			//return x[m]+(x[m]-x[m-1])/(y[m]-y[m-1]) * (yy-y[m]);
			//return x[m]+(x[m]-x[0])/(y[m]-y[0]) * (yy-y[m]);
			return x[m]+(x[m]-x[m-d])/(y[m]-y[m-d]) * (yy-y[m]);
		
		int k = 0;
		int klo = 0;
		int khi = y.length-1;
		while (khi-klo > 1)
		{
			k = (khi+klo)/2;
			if (y[k] > yy)
				khi = k;
			else
				klo = k;
		}

      		double h = y[khi] - y[klo];
                if (h == 0.0)
                        return (y[khi]+y[klo])/2.0;

                double a = (y[khi] - yy)/h;
                double b = (yy - y[klo])/h;

		return a*x[klo] + b*x[khi];
	}

	
// Private data.

	private double[] x = null;
	private double[] y = null;

/**
 Test main.
*/
	public static void main(String[] args)
	{
		double[] x = new double[100];
		double[] y = new double[100];
		
		for (int i=0; i<x.length; i++)
		{
			x[i] = i/100.0;
			y[i] = 15+Math.sin(x[i]);
		}

		LinearCalibrator s = new LinearCalibrator(x,y);

/*
		for (double z = -0.5; z<1.5; z+=0.01)
			System.out.println(z+" "+s.y(z));
*/
		for (double z = 14.5; z<16.5; z+=0.01)
			System.out.println(s.x(z)+" "+z);
	}
}
