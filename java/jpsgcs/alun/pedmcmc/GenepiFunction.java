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


package jpsgcs.alun.pedmcmc;

import jpsgcs.alun.markov.Variable;
import java.util.Set;
import java.util.LinkedHashSet;

abstract public class GenepiFunction
{
	public Variable[] getVariables()
	{
		return v;
	}

	public String toString()
	{
		StringBuffer s = new StringBuffer();
		s.append("(");
		for (Variable u : v)
			s.append(u+",");
		if (v.length > 0)
			s.deleteCharAt(s.length()-1);
		s.append(")");
		return s.toString();
	}

	protected Variable[] v = null;
}
