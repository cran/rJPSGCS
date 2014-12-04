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

import jpsgcs.alun.genio.GeneticDataSource;

/**
 Represents an observation for a marker locus.
*/
public class MarkerObservation extends Observation
{
/** 
 Creates a new marker observation by reading the data from the given
 linkage data.
 The data for the ith locus in the list is read.
*/
	public MarkerObservation(GeneticDataSource gds, int i)
	{
		if (gds.nAlleles(i) == 4 && gds.locusName(i).toUpperCase().indexOf("SNP") > -1)
			m = new SNP();
		else
			m = new Marker(gds.nAlleles(i));

		d = new Phenotype[gds.nIndividuals()];
		for (int j=0; j<d.length; j++)
		{
			d[j] = ((Marker)m).findPhenotype(gds.getAllele(i,j,0),gds.getAllele(i,j,1));
			d[j].incFrequency(1);
		}

		m.downCode();
		m.geneCount(1000);
		m.downCode(10E-10);
	}
}
