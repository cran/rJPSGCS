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


package jpsgcs.alun.markov;

import java.util.Set;

/**
 This is a class to store the information needed in moving from
 a MarkovRandomField representing a product of functions to a 
 GraphicalModel that allows operating on the variables in the product.
*/

public class ModelInfo
{
	public ModelInfo(Set<Variable> c, Set<Variable> n, Set<Function> f, Set<Function> e)
	{
		clique = c;
		next = n;
		func = f;
		evid = e;
	}

	Set<Variable> clique = null;
	Set<Variable> next = null;
	Set<Function> func = null;
	Set<Function> evid = null;
}
