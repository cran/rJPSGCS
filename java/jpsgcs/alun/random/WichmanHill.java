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

/**
 This is an implementation of the simple generator published by
 Wichman and Hill in RSS series C. It is supposed to have reasonable
 statistical properties and long period.
*/
public final class WichmanHill extends PseudoRandomEngine
{
	static { className = "WichmanHill"; }

/**
 Default constructor. Sets the seeds arbitrarily.
*/
	public WichmanHill()
	{
		seed(arbitrarySeed(),arbitrarySeed(),arbitrarySeed());
	}

/**
 Set the three seeds of the generator.
*/
	public final synchronized void seed(long a, long b, long c)
	{
		s1 = a;
		s2 = b;
		s3 = c;
	}

/**
 Set the three seeds of the generator to the same value.
 It would be better to use the three value version, but this is 
 included for compatibility with the superclass requirement.
*/
	public final synchronized void seed(long s)
	{
		seed(s,s,s);
	}

/**
 Returns the next value from the generator.
*/
	public final synchronized double next()
	{
		s1 = (m1*s1) % d1;
		s2 = (m2*s2) % d2;
		s3 = (m3*s3) % d3;
		double u = s1/(double)d1 + s2/(double)d2 + s3/(double)d3;
		return u - (int) u;
	}

	private long s1, s2, s3;
	private static final long m1 = 171;
	private static final long m2 = 172;
	private static final long m3 = 170;
	private static final long d1 = 30269;
	private static final long d2 = 30307;
	private static final long d3 = 30323;
}
