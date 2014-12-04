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

import java.io.PrintStream;
import java.util.zip.*;

public interface GeneticDataSource extends BasicGeneticData
{
/**
 Returns the proband status of the jth individual in the list.
*/
	public int proband(int j);

/**
 Returns the number of individuals whose proband status > 0.
*/
	public int nProbands();

/**
 This returns the first or second allele at the ith locus for the jth individual,
 depending on whether k is 0 or 1.
 This is only really possible when the locus is codominant. 
 In other cases this should return a negative value.
*/
	public int getAllele(int i, int j, int k);

/**
 This sets the alleles at the ith locus for the jth individual to a0 and a1.
 This is only really possible when the locus is codominant, in other cases
 no action should be taken. If the setting was possible true should be 
 returned, otherwise false.
*/
	public boolean setAlleles(int i, int j, int a0, int a1);

	public void writeIndividual(int i, PrintStream p);

	public void writePedigree(PrintStream p);
	public void writePedigree(GZIPOutputStream z);

	public void writeParameters(PrintStream p);

	public void downcodeAlleles();
}
