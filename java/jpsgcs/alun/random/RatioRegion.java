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
 This interface defines what a random variable must provide in 
 order that the ratio of uniforms method can be used to 
 generate it.
*/
public interface RatioRegion
{
/**
 Returns true iff the given point is contained in the appropriate region
*/
	public boolean containsInRatioRegion(double x, double y);
}
