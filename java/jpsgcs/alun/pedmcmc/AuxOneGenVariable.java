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

public class AuxOneGenVariable extends AuxVariable
{
	public AuxOneGenVariable(Inheritance pat, Inheritance mat)
	{
		this(array(pat),array(mat));
	}

	public AuxOneGenVariable(Inheritance[] pat, Inheritance[] mat)
	{
		super(4);
		p = pat;
		m = mat;
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

		case 1:	for (Inheritance i : p)
				i.flip();
			break;

		case 2:	for (Inheritance i : m)
				i.flip();
			break;

		case 3:	for (Inheritance i : p)
				i.flip();
			for (Inheritance i : m)
				i.flip();
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
}
