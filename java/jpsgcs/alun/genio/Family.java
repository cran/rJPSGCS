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


package jpsgcs.alun.genio;

import java.util.LinkedHashSet;
import java.util.Set;

public class Family
{
	public Family()
	{
		k = new LinkedHashSet<Object>();
	}

	public Object getPa()
	{
		return p;
	}

	public Object getMa()
	{
		return m;
	}

	public Object[] getKids()
	{
		return k.toArray();	
	}

	public int nKids()
	{
		return k.size();
	}

	public void setPa(Object a)
	{
		p = a;
	}

	public void setMa(Object a)
	{
		m = a;
	}

	public void addKid(Object a)
	{
		k.add(a);
	}

	public void removeKid(Object a)
	{
		k.remove(a);
	}

	private Object p = null;
	private Object m = null;
	private Set<Object> k = null;
}
