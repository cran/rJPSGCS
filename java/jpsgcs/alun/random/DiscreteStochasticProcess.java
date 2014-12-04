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
 Defines what is necessary for a discrete stochastic process
 to implement.
*/
abstract public class DiscreteStochasticProcess extends StochasticProcess
{
/**
 Advances the process to the occurrence of the next event.
*/
	abstract public void next();
/**
 Advances the process to the occurrence of the nth next event.
*/
	abstract public void next(int n);
}
