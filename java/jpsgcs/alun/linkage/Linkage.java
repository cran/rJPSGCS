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


package jpsgcs.alun.linkage;

import jpsgcs.alun.genio.GeneticDataSource;
import jpsgcs.alun.genio.PedigreeData;
import jpsgcs.alun.genio.ParameterData;
import java.io.IOException;

public class Linkage
{
	public static GeneticDataSource read(LinkageFormatter parin, LinkageFormatter pedin) throws IOException
	{
		LinkageDataSet p = new LinkageDataSet(parin,pedin);
		return new LinkageInterface(p);
	}

	public static GeneticDataSource read(String par, String ped) throws IOException
	{
		LinkageDataSet p = new LinkageDataSet(par,ped);
		return new LinkageInterface(p);
	}

	public static GeneticDataSource[] readAndSplit(String par, String ped) throws IOException
	{
		LinkageDataSet p = new LinkageDataSet(par,ped);
		LinkageDataSet[] s = p.splitByPedigree(); 
		GeneticDataSource[] g = new GeneticDataSource[s.length];

		for (int i = 0; i<s.length; i++)
			g[i] = new LinkageInterface(s[i]);

		return g;
	}

	public static PedigreeData readTriplets() throws IOException
	{
		LinkagePedigreeData p = new LinkagePedigreeData(new LinkageFormatter(),null,LinkageIndividual.TRIPLET);
		LinkageDataSet d = new LinkageDataSet(null,p);
		return new LinkageInterface(d);
	}

	public static PedigreeData readTriplets(String ped) throws IOException
	{
		LinkagePedigreeData p = new LinkagePedigreeData(new LinkageFormatter(ped),null,LinkageIndividual.TRIPLET);
		LinkageDataSet d = new LinkageDataSet(null,p);
		return new LinkageInterface(d);
	}

	public static PedigreeData readPedigreeData() throws IOException
	{
		LinkagePedigreeData p = new LinkagePedigreeData(new LinkageFormatter(),null,false);
		LinkageDataSet d = new LinkageDataSet(null,p);
		return new LinkageInterface(d);
	}

	public static PedigreeData readPedigreeData(String ped) throws IOException
	{
		LinkagePedigreeData p = new LinkagePedigreeData(new LinkageFormatter(ped),null,false);
		LinkageDataSet d = new LinkageDataSet(null,p);
		return new LinkageInterface(d);
	}

	public static ParameterData readParameterData() throws IOException
	{
		LinkageParameterData p = new LinkageParameterData(new LinkageFormatter());
		LinkageDataSet d = new LinkageDataSet(p,null);
		return new LinkageInterface(d);
	}

	public static ParameterData readParameterData(String par) throws IOException
	{
		LinkageParameterData p = new LinkageParameterData(new LinkageFormatter(par));
		LinkageDataSet d = new LinkageDataSet(p,null);
		return new LinkageInterface(d);
	}
}
