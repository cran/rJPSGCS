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


package jpsgcs.alun.pedapps;

import jpsgcs.alun.genio.BasicGeneticData;
import jpsgcs.alun.pedmcmc.Inheritance;
import java.util.Vector;

public class Kinship
{
	public Kinship(BasicGeneticData d, Inheritance[][][] vv)
	{
		this (d,vv,GeneticMap.locusCentiMorgans(d,true));
	}

	public Kinship(BasicGeneticData d, Inheritance[][][] hh, double[] pos)
	{
		h = hh;
		pa = Pedigrees.fathers(d);
		ma = Pedigrees.mothers(d);
		order = Pedigrees.canonicalOrder(d);
		nups = 0;
		positions = LocInfo.mapPositions(d,pos,true);
		kin = new double[positions.length][pa.length][pa.length];
	}

	public void update()
	{
		int[][] allele = new int[pa.length][2];

		for (int i=0; i<kin.length; i++)
		{
			LocInfo l = positions[i];

			int al = 0;

			for (int jj=0; jj<allele.length; jj++)
			{
				int j = order[jj];

				if (pa[j] < 0)
					allele[j][0] = al++;
				else
					allele[j][0] = allele[pa[j]] [ inherit(h[l.ileft][j][0].getState(),h[l.iright][j][0].getState(),l.tleft,l.tright) ];

				if (ma[j] < 0)
					allele[j][1] = al++;
				else
					allele[j][1] = allele[ma[j]] [ inherit(h[l.ileft][j][1].getState(),h[l.iright][j][1].getState(),l.tleft,l.tright) ];
			}
			
			countKins(kin[i],allele);
		}

		nups++;
	}

	public int nPositions()
	{
		return kin.length;
	}

	public double[][] kinships(int i)
	{
		double[][] kn = new double[kin[i].length][kin[i][0].length];
		for (int j=0; j<kin[i].length; j++)
			for (int k=0; k<kin[i][j].length; k++)
				kn[j][k] = kin[i][k][j] / nups;
		return kn;
	}

// Private data and methods.

	private Inheritance[][][] h = null;
	private int[] pa = null;
	private int[] ma = null;
	private int[] order = null;
	private LocInfo[] positions = null;
	private double[][][] kin = null;
	private double nups = 0;

	private int inherit(int s0, int s1, double t0, double t1)
	{
		double p0 = ( s0 == 1 ? t0 : (1-t0) ) * ( s1 == 1 ? t1 : (1-t1) );
		double p1 = ( s0 == 0 ? t0 : (1-t0) ) * ( s1 == 0 ? t1 : (1-t1) );
		int res = Math.random() <= p0 / (p0+p1) ? 0 : 1;
		return  res;
	}

	private void countKins(double[][] kn, int[][] al)
	{
		for (int j=0; j<kn.length; j++)
			for (int k=0; k<kn[j].length; k++)
			{
				if (j == k)
				{
					kn[j][k] += al[j][0] == al[j][1] ? 1 : 0.5;
				}
				else
				{
					for (int a=0; a<2; a++)
						for (int b=0; b<2; b++)
							if (al[j][a] == al[k][b])
								kn[j][k] += 0.25;
				}
			}
	}
}
