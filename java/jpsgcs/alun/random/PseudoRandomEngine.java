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
 This subclass of RandomEngine allows a seed to be set and 
 any  sequence of output to be repeated exactly. 
*/
abstract public class PseudoRandomEngine extends RandomEngine
{
/**
 This sets the seed for the pseduo random number generating method.
 The sequence of output should be determined exactly by the seed.
*/
	abstract public void seed(long s);

/**
 This provides an arbitrary seed derived from the time of day to 
 allow generators to startup without explicit definition of different
 seeds each time.
*/
	public synchronized final long arbitrarySeed()
	{
		return System.currentTimeMillis();
	}
}
