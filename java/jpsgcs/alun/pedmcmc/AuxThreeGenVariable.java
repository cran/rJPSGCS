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

public class AuxThreeGenVariable extends AuxVariable
{
	public AuxThreeGenVariable(Inheritance[] hp, Inheritance[] hm, Inheritance[] lp, Inheritance[] lm)
	{
		super(2);

		hip = hp;
		him = hm;
		lop = lp;
		lom = lm;

		p = new Inheritance[hip.length + lop.length];
		m = new Inheritance[him.length + lom.length];

		for (int i=0, j=0; i<hip.length; i++, j++)
		{
			p[j] = hip[i];
			m[j] = him[i];
		}

		for (int i=0, j=hip.length; i < lop.length; i++, j++)
			p[j] = lop[i];

		for (int i=0, j=him.length; i < lom.length; i++, j++)
			m[j] = lom[i];
	}

	public void setState(int s)
	{
		super.setState(s);

		for (Inheritance i : p)
			i.recall();
		for (Inheritance i : m)
			i.recall();

		switch(s)
		{
		case 0:	break;

		case 1:	for (Inheritance i : lop)
				i.flip();
			for (Inheritance i : lom)
				i.flip();

			for (int i=0; i<hip.length; i++)
			{
				int t = hip[i].getState();
				hip[i].setState(him[i].getState());
				him[i].setState(t);
			}

			break;
		}
	}

	public boolean next()
	{
		if (!super.next())
			return false;
		setState(getState());
		return true;
	}

// Private data.

	private Inheritance[] hip = null;
	private Inheritance[] him = null;
	private Inheritance[] lop = null;
	private Inheritance[] lom = null;
}
