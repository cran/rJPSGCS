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

import jpsgcs.alun.util.InputFormatter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.RuntimeException;

public class LinkageFormatter extends InputFormatter
{
	public LinkageFormatter() throws IOException
	{
		this(new BufferedReader(new InputStreamReader(System.in)),"System.in");
	}

	public LinkageFormatter(String name) throws IOException
	{
		this (new BufferedReader(new FileReader(name)),name);
	}

	public LinkageFormatter(BufferedReader br, String f) throws IOException
	{
		super(br);
		file = f;
	}

	public void readLine() throws IOException
	{
		if (!newLine())
			crash("is missing");
	}

	public int readInt(String name, int def, boolean used, boolean crash)
	{
		if (newToken() && nextIsInt())
			return getInt();
		
		if (crash)
		{
			crash("Can't read integer "+name+" from string \""+getString()+"\".");
		}
		else
		{
			warn("Can't read integer "+name+
				" from string \""+getString()+"\"."+
				"\n\tAssumed to be "+def+"."
				+ (!used ? "\n\tThis parameter is not used by these programs" : "")
				);
		}
		return def;
	}

	public double readDouble(String name, double def, boolean used, boolean crash)
	{
		if (newToken() && nextIsDouble())
			return getDouble();
		
		if (crash)
		{
			crash("Can't read double "+name+" from string \""+getString()+"\".");
		}
		else
		{
			warn("Can't read double "+name+
				" from string \""+getString()+"\"."+
				"\n\tAssumed to be "+def+"."
				+ (!used ? "\n\tThis parameter is not used by these programs" : "")
				);
		}
		return def;
	}

	public void crash(String s) throws RuntimeException
	{
		System.err.println("\n"+file+", line "+lastLine()+", item "+lastToken()+":\n\t"+s);
		System.err.println("\tCan't continue");
		//System.exit(1);
		throw new RuntimeException();
	}

	public void warn(String s)
	{
		System.err.println("\n"+file+", line "+lastLine()+", item "+lastToken()+":\n\t"+s);
	}

	public String fileName()
	{
		return file;
	}

	private String file = null;
}
