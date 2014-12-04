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

import jpsgcs.alun.markov.Variable;
import jpsgcs.alun.markov.Function;
import jpsgcs.alun.markov.GraphicalModel;
import jpsgcs.alun.markov.MarkovRandomField;
import java.util.Set;
import java.util.LinkedHashSet;

public class AuxGraphicalModel extends GraphicalModel
{
	public AuxGraphicalModel(MarkovRandomField m)
	{
		super(m,false);

		Set<AuxVariable> s = new LinkedHashSet<AuxVariable>();
		for (Object x : m.getElements())
			if (x instanceof Variable)
				s.add((AuxVariable)x);
		v = (AuxVariable[]) s.toArray(new AuxVariable[0]);

		Set<AuxLocusFunction> t = new LinkedHashSet<AuxLocusFunction>();
		for (Object x : m.getElements())
			if (x instanceof AuxLocusFunction)
				t.add((AuxLocusFunction)x);
		f = (AuxLocusFunction[]) t.toArray(new AuxLocusFunction[0]);
	}

	public void simulate(boolean usepost)
	{
		for (int i=0; i<v.length; i++)
			v[i].prepare();
		for (int i=0; i<f.length; i++)
			f[i].prepare();
		super.simulate(usepost);
	}

// Private data.

	private AuxVariable[] v = null;
	private AuxLocusFunction[] f = null;
}
