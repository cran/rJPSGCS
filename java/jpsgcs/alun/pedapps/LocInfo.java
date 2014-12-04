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

public class LocInfo
{
	int ileft = 0;
	int iright = 0;
	double tleft = 0.0;
	double tright = 0.0;
	double pos = 0.0;

	static public double[] locusCentiMorgans(BasicGeneticData d)
	{
		double[] cm = new double[d.nLoci()];
		for (int i=1; i<cm.length; i++)
		{
			double dist = 0;
			dist += GeneticMap.thetaTocMKosambi(d.getMaleRecomFrac(i,i-1));
			dist += GeneticMap.thetaTocMKosambi(d.getFemaleRecomFrac(i,i-1));
			dist /= 2.0;
			cm[i] = cm[i-1] + dist;
		}
		return cm;
	}

	static public LocInfo[] mapPositions(BasicGeneticData d, double[] pos, boolean linkfirst)
	{
		int first = linkfirst ? 0 : 1;

		double[] cm = new double[d.nLoci()-first];

		for (int i=1; i<cm.length; i++)
		{
			double dist = 0;
			dist += GeneticMap.thetaTocMKosambi(d.getMaleRecomFrac(i+first,i-1+first));
			dist += GeneticMap.thetaTocMKosambi(d.getFemaleRecomFrac(i+first,i-1+first));
			dist /= 2.0;
			cm[i] = cm[i-1] + dist;
		}

		LocInfo[] positions = new LocInfo[pos.length];

		for (int i=0, left=-1, right=0; i<pos.length; i++)
		{
			while (right < cm.length && cm[right] <= pos[i])
			{
				left = right;
				right++;
			}

			positions[i] = new LocInfo();
			positions[i].pos = pos[i];

			if (left >= 0)
			{
				positions[i].ileft = left;
				positions[i].tleft = GeneticMap.cMToThetaKosambi(pos[i]-cm[left]);
			}
			else
			{
				positions[i].ileft = 0;
				positions[i].tleft = 0.5;
			}
	
			if (right < cm.length)
			{
				positions[i].iright = right;
				positions[i].tright = GeneticMap.cMToThetaKosambi(cm[right]-pos[i]);
			}
			else
			{
				positions[i].iright = cm.length-1;
				positions[i].tright = 0.5;
			}
		}
		
		return positions;
	}

	public String toString()
	{
		return ileft+" "+tleft+" "+iright+" "+tright;
	}
}
