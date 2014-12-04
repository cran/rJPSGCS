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


package jpsgcs.alun.zeroloop;

import jpsgcs.alun.markov.Variable;
import java.util.Set;
import java.util.Collection;

public class TwoParentConstraint extends EdgeConstraint
{
	public TwoParentConstraint(Collection<? extends Variable> ed)
	{
		super(ed);
	}
	
	public double getValue()
	{
		int tot = 0;
		for (int i=0; i<e.length; i++)
			tot += e[i].getState();
		return ( tot == 2 ? 1 : 0 );
	}
}
