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


package jpsgcs.alun.genio;

public interface BasicGeneticData extends ParameterData, PedigreeData
{
/**
 Returns the penetrance function at the ith locus for the jth person.
 This is a matrix of dimension nAlleles(j) x nAlleles(j) where the
 entry at row a, column b is the probability of the individual's 
 observed phenotype given that they have allele number a on the
 paternal chromosome and allele number b on the maternal chromosome.
 Again, phenotype is used in a very general way so this function is
 also used to express genotypic information. If an individual j
 is observed with genotype (x,y) at locus i then 
	penetrance(i,j)[x,y] = penetrance(i,j)[y,x] = 1
 with zeros elsewhere.
 If the locus is not perfectly co-dominant, or there is error
 in the observation, this should be expressed appropriately in this
 matrix.
 If no information is present for the jth individual at the ith locus
 this method can return null.
*/
	public double[][] penetrance(int i, int j);
}
