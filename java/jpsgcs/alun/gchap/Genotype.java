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
 Represents a genotype as an un ordered pair of allele indexes.
*/
public class Genotype
{
/** 
 Creates a new genotype from the given allele numbers.
 The alleles are unordered so using (i,j) and (j,i) are
 equivalent.
*/
	public Genotype(int i, int j)
	{
		if (i < j)
		{
			a = i;
			b = j;
		}
		else
		{
			a = j;
			b = i;
		} 
	}

/**
 The first allele index.
*/
	public int a = 0;
/**
 The second allele index.
*/
	public int b = 0;
}
