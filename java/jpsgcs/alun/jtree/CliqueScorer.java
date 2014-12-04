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


package jpsgcs.alun.jtree;

import java.util.Set;

public abstract class CliqueScorer<V>
{
	abstract public double score (Set<V> c);
	abstract public double temperature();
	
	public double score(JTree<V> j)
	{
		double x = 0;

		for (Set<V> c : j.cliques)
			x += score(c);
		for (Set<V> s : j.separators)
			x -= score(s);

		return x;
	}
}
