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


package jpsgcs.alun.gchap;

/**
 This class represents an observation at a list of loci.
*/
public class MultiLocusObservation extends Observation
{
/**
 Create a new multi locus observation from the
 observations on the constituent two loci, each of which may
 itself be a multi locus observation.
*/
	public MultiLocusObservation(Observation a, Observation b)
	{
		if (a.d.length != b.d.length)
			throw new RuntimeException("Observation missmatch");

		m = new MultiLocusTrait(a.m,b.m);

		d = new Phenotype[a.d.length];
		for (int j=0; j<d.length; j++)
		{
			d[j] = ((MultiLocusTrait)m).findPhenotype(a.d[j],b.d[j]);
			if (d[j] == null)
				continue;
			d[j].incFrequency(1);
		}

		m.downCode();
	}
}
