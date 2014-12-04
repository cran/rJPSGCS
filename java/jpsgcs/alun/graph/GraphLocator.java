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


package jpsgcs.alun.graph;

import java.util.Collection;

abstract public class GraphLocator<V,E>
{
	abstract public double move(LocatedGraph<V,E> g);

	public Parameter[] getParameters()
	{
		return par;
	}

	public void set(LocatedGraph<V,E> g)
	{
		for (Point p : g.getPoints())
		{
			p.x = Math.random()*1000 - 500 ;
			p.y = Math.random()*1000 - 500 ;
		}
	}

// Private data and methods.

	protected Parameter[] par = null;

	protected double update(Point a, Derivatives D)
	{
		double d2 = Math.abs(D.d2x + D.d2y);
		if (d2 > Double.MIN_VALUE)
		{
			a.x -= D.dx/d2;
			a.y -= D.dy/d2;
			return (D.dx*D.dx + D.dy*D.dy) /d2/d2 ;
		}
		return 0;
	}

	protected void addDerivatives(Derivatives D, Derivatives E, double s)
	{
		D.dx += s*E.dx;
		D.dy += s*E.dy;
		D.d2x += s*E.d2x;
		D.d2y += s*E.d2y;
	}

	protected Derivatives squaredAttractions(Point a, Collection<Point> c)
	{
		Derivatives D = new Derivatives();
		double x = 0;
		double y = 0;
		double r = 0;

		for (Point b : c)
		{
			if (b == a)
				continue;
			x += b.x;
			y += b.y;
			r += 1;
		}

		D.dx += 2*r* a.x - 2*x;
		D.dy += 2*r* a.y - 2*y;
		D.d2x += 2*r;
		D.d2y += 2*r;
		return D;
	}

	protected Derivatives homePointAttraction(Point a, double s)
	{
		Derivatives D = new Derivatives();
			
		D.dx += 2 * (a.x - s*a.x0);
		D.dy += 2 * (a.y - s*a.y0);
		D.d2x += 2;
		D.d2y += 2;
	
		return D;
	}

	protected Derivatives inverseSquareRepulsions(Point a, Collection<Point> c)
	{
		Derivatives D = new Derivatives();
		for (Point b : c)
		{
			if (b == a)
				continue;

			double x = a.x - b.x;
			double xx = x*x;
			double y = a.y - b.y;
			double yy = y*y;

			double r = xx + yy;
			if (r < Double.MIN_VALUE)
				continue;

			r = 1.0/r;
			double s = -2*r*r;
			double t = 4*s*r;

			D.dx += s*x;
			D.dy += s*y;
			D.d2x += s - t*xx;
			D.d2y += s - t*yy;
		}
		return D;
	}

	protected Derivatives localRepulsions(Point a, Collection<Point> c, double gamma)
	{
		Derivatives D = new Derivatives();
		for (Point b : c)
		{
			if (b == a)
				continue;

			double x = a.x - b.x;
			double xx = x*x;
			double y = a.y - b.y;
			double yy = y*y;

			double r = xx + yy;
			if (r < Double.MIN_VALUE)
				continue;

			if (r > gamma)
				continue;

			r = 1/r;

			double s = gamma * r;
			s = s*s;
			double t = 8*s*r;
			s = 2*(1-s);
			
			D.dx += s*x;
			D.dy += s*y;
			D.d2x += s + t*xx;
			D.d2y += s + t*yy;
		}
		return D;
	}

	protected Derivatives rootedLocalRepulsions(Point a, Collection<Point> c, double gamma)
	{
		Derivatives D = new Derivatives();
		for (Point b : c)
		{
			if (b == a)
				continue;

			double x = a.x - b.x;
			double xx = x*x;
			double y = a.y - b.y;
			double yy = y*y;

			double r = xx + yy;
			if (r < Double.MIN_VALUE)
				continue;

			if (r > gamma)
				continue;

			double s = r - gamma;
			double t = 3*gamma -r;
			r = 1/Math.sqrt(r);
			s *= r*r*r;
			t *= r*r*r*r*r;

			D.dx += s*x;
			D.dy += s*y;
			D.d2x += s + t*xx;
			D.d2y += s + t*yy;
		}
		return D;
	}

	protected Derivatives verticalGenerations(Point a, Collection<Point> c, double delta)
	{
		Derivatives D = new Derivatives();

		for (Point b : c)
		{
			if (b == a)
				continue;
			D.dy += a.y - b.y + delta;
			D.d2y += 1;
		}

		return D;
	}
}
