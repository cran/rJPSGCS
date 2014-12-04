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

abstract public class AuxVariable extends Variable
{
	public AuxVariable(int n)
	{
		super(n);
	}

	public void prepare()
	{
		for (Inheritance i : p)
			i.remember();
		for (Inheritance i : m)
			i.remember();
	}

	public String toString()
	{
		StringBuffer s = new StringBuffer();
		s.append(super.toString()+":");
		s.append("AV(");
		for (Inheritance i : p)
			s.append(i);
		s.append("+");
		for (Inheritance i : m)
			s.append(i);
		s.append(")");
		return s.toString();
	}

// Private data and methods.

	protected Inheritance[] p = null;
	protected Inheritance[] m = null;

	protected static Inheritance[] array(Inheritance i)
	{
		Inheritance[] j = new Inheritance[1];
		j[0]= i;
		return j;
	}
}
