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

import jpsgcs.alun.markov.MarkovRandomField;
import jpsgcs.alun.markov.Function;
import java.util.Set;
import java.util.LinkedHashSet;

public class TwoLocusProduct extends MarkovRandomField
{
	public TwoLocusProduct(LocusProduct a, LocusProduct b, double theta)
	{
		for (Function f : a.getFunctions())
			add(f);
		for (Function f : b.getFunctions())
			add(f);
		
		Inheritance[][] ha = a.getInheritances();
		Inheritance[][] hb = b.getInheritances();

		if (ha.length != hb.length)
			throw new RuntimeException("Locus missmatch in TwoLocusPoduct constructor");

		Set<Recombination> s = new LinkedHashSet<Recombination>();

		for (int j=0; j<ha.length; j++)
		{
			if ((ha[j][0] == null && hb[j][0] != null) || (ha[j][0] != null && hb[j][0] == null)) 
				throw new RuntimeException("Locus missmatch in TwoLocusPoduct constructor");

			if (ha[j][0] != null && hb[j][0] != null)
			{
				Recombination r = new Recombination(ha[j][0],hb[j][0],theta);
				s.add(r);
				add(r);
			}

			if ((ha[j][1] == null && hb[j][1] != null) || (ha[j][1] != null && hb[j][1] == null)) 
				throw new RuntimeException("Locus missmatch in TwoLocusPoduct constructor");

			if (ha[j][1] != null && hb[j][1] != null)
			{
				Recombination r = new Recombination(ha[j][1],hb[j][1],theta);
				s.add(r);
				add(r);
			}
		}

		rec = (Recombination[]) s.toArray(new Recombination[0]);
	}

	public void fix(double theta)
	{
		for (int i=0; i<rec.length; i++)
			rec[i].fix(theta);
	}

// Private data and methods.

	private Recombination[] rec = null;
}
