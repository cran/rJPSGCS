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


import jpsgcs.alun.pedmcmc.LDModel;
import jpsgcs.alun.linkage.LinkageFormatter;
import jpsgcs.alun.linkage.LinkageParameterData;
import jpsgcs.alun.linkage.LinkageInterface;
import jpsgcs.alun.linkage.LinkageDataSet;

public class LDModelToGraph
{
	public static void main(String[] args)
	{
		try
		{
			LinkageFormatter parin = new LinkageFormatter();
			LinkageParameterData par = new LinkageParameterData(parin);
			LDModel ld = new LDModel(parin);
			if (ld.getLocusVariables() == null)
				ld = new LDModel(new LinkageInterface(new LinkageDataSet(par,null)));

			System.out.println(ld.markovGraph());
		}
		catch (Exception e)
		{
			System.err.println("Caught in LDModelToGraph.main()");
			e.printStackTrace();
		}
	}
}
