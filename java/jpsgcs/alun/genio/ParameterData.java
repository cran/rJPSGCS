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

public interface ParameterData
{
	public String locusName(int i);
	
/**
 The number of genetic loci for which data is available.
 The loci will be accessed by index from 0 to nLoci()-1
 and are assumed to be in corresponding physical order.
*/
	public int nLoci();

/**
 The number of alleles at the ith genetic locus.
*/
	public int nAlleles(int i);

/**
 The frequencies of the alleles at the ith genetic locus.
*/
	public double[] alleleFreqs(int i);

/**
 Returns the probability of a recombination between loci i and j in
 a single meiosis in females.
*/
	public double getFemaleRecomFrac(int i, int j);

/**
 Returns the probability of a recombination between loci i and j in
 a single meiosis in males.
*/
	public double getMaleRecomFrac(int i, int j);
}
