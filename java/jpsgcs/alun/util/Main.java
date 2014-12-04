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


package jpsgcs.alun.util;

public class Main
{
	public static String[] strip(String[] a, String s)
	{
		if (a != null)
		{
			for (int i=0; i<a.length; i++)
			{
				if (a[i].equals(s))
				{
					String[] b = new String[a.length-1];
					for (int j=0; j<i; j++)
						b[j] = a[j];
					for (int j=i+1; j<a.length; j++)
						b[j-1] = a[j];
						return b;
				}
			}
		}
		return a;
	}
}
