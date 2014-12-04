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
 This classes extends locus to represent a combination of loci.
 In this model any two loci can be combined to give a new locus.
*/
public class MultiLocus extends Locus
{
/**
 Creates a new multi locus object from the two given loci, each
 of which may itself be a multi-locus locus.
*/
	public MultiLocus(Locus a, Locus b)
	{
		super
		(
			frequencyProduct(a.alleleFrequencies(),b.alleleFrequencies()),
			nameProduct(a.alleleNames(),b.alleleNames())
		);
		nal = b.nAlleles();
	}

// Protected methods.

	protected int nal = 0;

/**
 Returns the index of the alles at the combined locus with
 corresponds to the given pair of alleles at the constituent loci.
*/
	protected final int jointAllele(int i, int j)
	{
		return i*nal + j;
	}

// Private data

	private static String[] nameProduct(String[] a, String[] b)
	{
		String[] x = new String[a.length*b.length];
		for (int i=0, k=0; i<a.length; i++)
			for (int j=0; j<b.length; j++)
				x[k++] = a[i]+" "+b[j];
				//x[k++] = a[i]+"-"+b[j];
				//x[k++] = a[i]+b[j];
		return x;
	}

	private static double[] frequencyProduct(double[] a, double[] b)
	{
		double[] x = new double[a.length*b.length];
		for (int i=0, k=0; i<a.length; i++)
			for (int j=0; j<b.length; j++)
				x[k++] = a[i]*b[j];
		return x;
	}

/**
 Test main.
*/
	public static void main(String[] args)
	{
		Locus a = new Locus(2);
		Locus b = new Locus(2);
		MultiLocus c = new MultiLocus(a,b);
		Locus d = new Locus(3);
		MultiLocus e = new MultiLocus(c,d);
		System.out.println(e+"\n");
	}
}
