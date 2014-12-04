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


package jpsgcs.alun.graph;

public class Parameter
{
	public Parameter(String n, double mn, double mx, double df)
	{
		name = n;
		min = mn;
		max = mx;
		def = df;
		value = df;
	}

	public String name() { return name; }
	public double def() { return def; }
	public double max() { return max; }
	public double min() { return min; }

	public void setValue(double v)
	{
		value = v;
	}

	public double getValue()
	{
		return value;
	}

	private String name = null;
	private double max = 0;
	private double min = 0;
	private double def = 0;
	private double value = 0;
}
