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
 This interface defines what is needed to in order to use
 Exponential Switching as a method of generation.
 Any random variable that implements this inteface will be
 assumed to have a density function the log of which 
 is a concave function throughout its range.
*/
public interface LogConcave extends DensityFunction
{
/**
 Return the log of a function proportional to the density function.
*/
	public double logDensityFunction(double x);
/**
 Return the lower bound of the range of the random variable.
 May be negative infinity.
*/
	public double lowerBound();
/**
 Return the lower bound of the range of the random variable.
 May be positive infinity.
*/
	public double upperBound();
}
