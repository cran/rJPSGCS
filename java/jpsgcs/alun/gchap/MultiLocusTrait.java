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

import java.util.Vector;

/**
 This represents a multi locus trait. It links the loci to the phenotypes.
*/
public class MultiLocusTrait extends GeneticTrait
{
/**
 Creates a new multi locus trait from two given genetic traits,
 each of which may itself be a multi locus trait.
*/
	public MultiLocusTrait(GeneticTrait a, GeneticTrait b)
	{
		super(new MultiLocus(a.getLocus(),b.getLocus()));
	
		Phenotype[] pa = a.getPhenotypes();
		Phenotype[] pb = b.getPhenotypes();
		hh = new HashOfHash();

		for (int i=0; i<pa.length; i++)
			for (int j=0; j<pb.length; j++)
			{
				Phenotype p = new MultiLocusPhenotype((MultiLocus)getLocus(),pa[i],pb[j]);
				hh.put(pa[i],pb[j],p);
				putPhenotype(p);				
			}
	}

/**
 Returns the phenotype of the multilocus trait that represents
 the same information as those specified for the two constituent traits.
*/
	public Phenotype findPhenotype(Phenotype a, Phenotype b) 
	{
		return (Phenotype) hh.get(a,b);
	}

// Private data.

	private HashOfHash hh = null;

/**
 Test main.
*/
	public static void main(String[] args)
	{
		GeneticTrait a = new SNP();
		GeneticTrait b = new Marker(2);
		GeneticTrait c = new MultiLocusTrait(a,b);
		System.out.println(c);
	}
}
