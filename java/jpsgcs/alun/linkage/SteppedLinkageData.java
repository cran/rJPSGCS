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

import java.util.Vector;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SteppedLinkageData extends LinkageDataSet implements LinkConstants
{
	public SteppedLinkageData(LinkageFormatter parin, LinkageFormatter pedin) throws IOException
	{
		pardat = new LinkageParameterData(parin);
		peddat = new LinkagePedigreeData();
		pedinf = pedin;
		set(pardat,peddat);
		v = new Vector<LinkageIndividual>();
	}

	public SteppedLinkageData(String parfile, String pedfile) throws IOException
	{
		this( new LinkageFormatter(new BufferedReader(new FileReader(parfile)),"Par file"), new LinkageFormatter(new BufferedReader(new FileReader(pedfile)),"Ped file"));
/*
                LinkageFormatter f = new LinkageFormatter(new BufferedReader(new FileReader(parfile)),"Par file");
		pardat = new LinkageParameterData(f);

		peddat = new LinkagePedigreeData();
		pedinf = new LinkageFormatter(new BufferedReader(new FileReader(pedfile)),"Ped file");

		set(pardat,peddat);

		v = new Vector<LinkageIndividual>();
*/
        }

	public boolean next() throws IOException
	{
		v.clear();
		if (!pedinf.newLine())
			return false;
		v.addElement(new LinkageIndividual(pedinf,pardat,STANDARD));
		peddat.quickSet(v);
		return true;
	}

	private Vector<LinkageIndividual> v = null;
	private LinkageParameterData pardat = null;
	private LinkagePedigreeData peddat = null;
	private LinkageFormatter pedinf = null;
}
