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
 This class provides a link between this package and the
 standard java random number generator. Just a wrapper.
*/
public class BuiltInGenerator extends PseudoRandomEngine
{
	static { className = "BuiltInGenerator"; }

/**
 Constructs a new generator that delegates to the java.util.Random class.
*/
 
	public BuiltInGenerator()
	{
		e = new java.util.Random();
	}

/**
 Sets the seed for the generator.
*/
	public final synchronized void seed(long s)
	{
		e.setSeed(s);
	}

/**
 Returns the next pseudo random variate.
*/
	public final synchronized double next()
	{
		return e.nextDouble();
	}

	private java.util.Random e = null;
}
