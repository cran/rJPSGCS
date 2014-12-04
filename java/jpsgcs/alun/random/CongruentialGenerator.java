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
 This class implements the simple congruential method of 
 generating pseudo random uniforms. 
*/
public class CongruentialGenerator extends PseudoRandomEngine
{
	static { className = "CongruentialGenerator"; }

/**
 The default constructor sets the parameters to the so called minimum
 standard of Park and Miller.
*/
	public CongruentialGenerator()
	{
		this(16807,0,0x7FFFFFFF);
	}

/**
 General constructor that allows the programmer to set the multiplier,
 shift and modulus parameters.
 Choose your own values at your own risk.
 The seed is set arbitrarily.
*/
	public CongruentialGenerator(long mult, long shift, long mod)
	{
		seed(arbitrarySeed());
		m = mult;
		h = shift;
		d = mod;
	}

/**
 This sets the seed for the generator.
*/
	public final synchronized void seed(long i)
	{
		s = i;
	}

/**
 Returns the next pseudo random Uniform.
*/
	public final synchronized double next()
	{
		s = ( s * m + h ) % d;
		return s / (double) d;
	}

	private long s, h, m, d;
}
