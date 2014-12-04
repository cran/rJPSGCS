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
 This is the base class from which random things such as 
 random variables or randomly sampled objects are derived.
 It's main function is to manage the random engine that is
 used by the transformation methods of the random variables etc.
 For almost all applications it is best to use a single 
 generator and queue up calls from other objects to it.
 This makes sure that the generator doesn't give the same
 result to two different calls. It also makes sure that 
 two or more generators don't inadvertently give correlated
 values. 
*/
public class Random
{
/**
 Sets the random engine used by the class and its subclasses.
*/ 
	public final static void setEngine(RandomEngine e)
	{
		engine = e;
	}

/**
 Gets the random number engine used by the class.
*/
	public final static RandomEngine getEngine()
	{
		return engine;
	}

/**
 If the current RandomEngine is a pseudo random engine
 this method sets the seed to the given value.
 Otherwise nothing is done.
*/
	public final static void setSeed(long x)
	{
		if (engine instanceof PseudoRandomEngine)
			((PseudoRandomEngine) engine).seed(x);
	}

/**
 Short hand method to get the next uniform random variate from
 the random engine.
*/
	public final double U()
	{
		return engine.next();
	}

	private static RandomEngine engine = new BuiltInGenerator();
}
