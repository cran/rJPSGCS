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

public class GoldenSection
{
	public static double gold = 0.61803390;

	public static CartesianPoint maximum(Curve p, double lo, double hi, double tol)
	{
		return maximum(p,lo,hi,tol,false);
	}

	public static CartesianPoint maximum(Curve p, double lo, double hi, double tol, boolean verb)
	{
		//return golden(p,lo,lo+(1-gold)*(hi-lo),hi,tol);

		double g = 1 - (Math.sqrt(5)-1) / 2.0;

		double h = hi;
		double fh = p.f(h);

		double l = lo;
		double fl = p.f(l);

		double a = l + g*(h-l);
		double fa = p.f(a);

		double b = a + g*(h-a);
		double fb = p.f(b);
		
		while (h-l > tol)
		{
			if (fa > fb)
			{
				h = b;
				fh = fb;
				b = a;
				fb = fa;
				a = b - g*(b-l);
				fa = p.f(a);
			}
			else
			{
				l = a;
				fl = fa;
				a = b;
				fa = fb;
				b = a + g*(h-a);
				fb = p.f(b);
			}
			
			if (verb)
			{
				System.out.print(l+"\t"+a+"\t"+b+"\t"+h+"\t\t");
				System.out.println(fl+"\t"+fa+"\t"+fb+"\t"+fh);
			}
		}

		double x = l;
		double y = fl;
		if (fa > y) { x = a; y = fa; }
		if (fb > y) { x = b; y = fb; }
		if (fh > y) { x = h; y = fh; }

		return new CartesianPoint(x,y);
	}

	public static CartesianPoint golden(Curve p, double lo, double mid, double hi, double tol)
	{	
		CartesianPoint r = new CartesianPoint(lo,p.f(lo));
		CartesianPoint s = null;
		CartesianPoint t = null;
		CartesianPoint u = new CartesianPoint(hi,p.f(hi));
		
		if (Math.abs(mid-lo) < Math.abs(hi-mid))
		{
			s = new CartesianPoint(mid,p.f(mid));
			double x = mid + (hi-mid)*(1-gold);
			t = new CartesianPoint(x,p.f(x));
		}
		else
		{
			double x = lo + (mid-lo)*(1-gold);
			s = new CartesianPoint(x,p.f(x));
			t = new CartesianPoint(mid,p.f(mid));
			
		}

		return golden(p,r,s,t,u,tol);
	}

	public static CartesianPoint golden(Curve p, CartesianPoint r, CartesianPoint s, CartesianPoint t, CartesianPoint u, double tol)
	{
		while (Math.abs(r.x-u.x) > tol)
		{
			if (t.y > s.y)
			{
				r = s;
				s = t;
				double x = gold*s.x + (1-gold)*u.x;
				t = new CartesianPoint(x,p.f(x));
			}
			else
			{
				u = t;
				t = s;
				double x = (1-gold)*r.x + gold*t.x;
				s = new CartesianPoint(x,p.f(x));
			}
		}

		CartesianPoint z = r;
		if (s.y > z.y)
			z = s;
		if (t.y > z.y)
			z = t;
		if (u.y > z.y)
			z = u;
		return z;
	}
}
