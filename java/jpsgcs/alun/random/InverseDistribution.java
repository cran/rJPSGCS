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
 This interface defines what it is necessary for a random variable
 to provide in order to use the inversion method of transformation.
*/
public interface InverseDistribution
{
/**
 Returns the value in the range for which the distribution function
 has the given value.
 For the inversion method it is ok to permute the values in the range so that the most
 common are searched first which results in faster searching.
 As a consequence this function may or may not be useful for 
 other ways of using the distribution function's inverse.
*/
	public double inverseDistribution(double u);
}
