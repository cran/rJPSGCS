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


import jpsgcs.alun.util.InputFormatter;
import jpsgcs.alun.graph.Network;
import java.util.Vector;

public class CliquesToAdj
{
	public static void main(String[] args)
	{
		try
		{
			Network<String,Object> g = new Network<String,Object>();
			InputFormatter f = new InputFormatter();

			while (f.newLine())
			{
				Vector<String> v = new Vector<String>();
				while (f.newToken())
					v.add(f.getString());

				for (int i=0; i<v.size(); i++)
				{
					g.add(v.get(i));
					for (int j=i+1; j<v.size(); j++)
						g.connect(v.get(i),v.get(j));
				}
			}

			System.out.println(g);
		}
		catch (Exception e)
		{
			System.err.println("Caught in CliquesToAdj()");
			e.printStackTrace();
		}
	}
}
