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

public class QuantitativePhenotype extends LinkagePhenotype
{
	public QuantitativePhenotype(QuantitativeLocus l, double[] vars)
	{
		setLocus(l);
		v = new double[vars.length];
		for (int i=0; i<v.length; i++)
			v[i] = vars[i];
	}

	public LinkagePhenotype nullCopy()
	{
		return new QuantitativePhenotype((QuantitativeLocus)getLocus(),new double[v.length]);
	}

	public String toString()
	{
		StringBuffer b = new StringBuffer();
		for (int i=0; i<v.length; i++)
			b.append(v[i]+" ");
		if (v.length > 0)
			b.deleteCharAt(b.length()-1);
		return b.toString();
	}

// Private data.

	public double[] v = null;
}
