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

public interface PedigreeData
{
	public String pedigreeName(int i);

	public String individualName(int i);
	
/**
 Returns the number of individuals who are being considered.
 Individuals will be accessed by integers between 0 and 
 nIndiviuals()-1 so repeated calls to the methods below must
 give consistent responses.
*/
	public int nIndividuals();

/**
 Returns the position in the list of individuals of the father
 of the individual in the jth position in the list. If father
 is not in the list -1 should be returned.
*/
	public int pa(int j);

/**
 Returns the position in the list of individuals of the mother
 of the individual in the jth position in the list. If mother
 is not in the list -1 should be returned.
*/
	public int ma(int j);

/**
 Returns the positions in the list of individuals of the children
 of the individual in the jth position in the list. If there
 are no offspring an array of length zero is returned.
*/
	public int[] kids(int j);

/**
 Returns a matrix of integers indexing the positions of a
 nuclear family. One row per family. The first element in
 the row is the father, the second the mother and the remainder
 are the indexes of the children.
**/
	public int[][] nuclearFamilies();
}
