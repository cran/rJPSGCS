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


package jpsgcs.alun.pedmcmc;

import jpsgcs.alun.genio.GeneticDataSource;
import jpsgcs.alun.markov.GraphicalModel;
import jpsgcs.alun.markov.Variable;
import jpsgcs.alun.markov.MarkovRandomField;
import jpsgcs.alun.markov.Function;
import jpsgcs.alun.util.Monitor;

import java.util.Set;
import java.util.LinkedHashSet;

public class LocusMeiosisSampler extends LocusSampler
{
	protected LocusMeiosisSampler()
	{
	}

	public LocusMeiosisSampler(GeneticDataSource d, boolean linkfirst)
	{
		this(d,linkfirst,false,3,1.00);
	}

	public LocusMeiosisSampler(GeneticDataSource d, boolean linkfirst, boolean usepotentials, int opt, double rate)
	{
		LocusProduct[] p = makeLocusSamplers(d,linkfirst,usepotentials);
		if (opt > 0)
			makeMeiosisSamplers(p,d,linkfirst,usepotentials,opt);

		samplerate = rate;
	}

	public LocusMeiosisSampler(GeneticDataSource d, boolean linkfirst, double error, int maxerral)
	{
		this(d,linkfirst,false,3,1.00,error,maxerral);
	}

	public LocusMeiosisSampler(GeneticDataSource d, boolean linkfirst, boolean usepotentials, int opt, double rate, double error, int maxerral)
	{
		LocusProduct[] p = makeErrorLocusSamplers(d,linkfirst,usepotentials,error,maxerral);
		if (opt > 0)
			 makeMeiosisSamplers(p,d,linkfirst,usepotentials,opt);

		samplerate = rate;
	}

	public void sample(boolean report)
	{
		if (onegens != null)
			for (int i=0; i<onegens.length; i++)
				if (Math.random() < samplerate)
				{
					onegens[i].simulate();
					if (report)
						System.err.print("1");
				}

		if (fulltwogens != null)
			for (int i=0; i<fulltwogens.length; i++)
				if (Math.random() < samplerate)
				{
					fulltwogens[i].simulate();
					if (report)
						System.err.print("2");
				}

		if (halftwogens != null)
			for (int i=0; i<halftwogens.length; i++)
				if (Math.random() < samplerate)
				{
					halftwogens[i].simulate();
					if (report)
						System.err.print("h");
				}

		if (threegens != null)
			for (int i=0; i<threegens.length; i++)
				if (Math.random() < samplerate)
				{
					threegens[i].simulate();
					if (report)
						System.err.print("3");
				}

		for (int i=0; i<g.length; i++)
			if (g[i] != null)
			{
				g[i].simulate();
				if (report)
					System.err.print("L");
			}
	}

// Private data.

	protected double samplerate = 1.0;
	protected AuxGraphicalModel[] onegens = null;
	protected AuxGraphicalModel[] fulltwogens = null;
	protected AuxGraphicalModel[] halftwogens = null;
	protected AuxGraphicalModel[] threegens = null;

	protected GraphicalModel[] makeMeiosisSamplers(LocusProduct[] p, GeneticDataSource d, boolean linkfirst, boolean usepotentials, int opt)
	{
		GraphicalModel[] gg = new GraphicalModel[p.length];

		int first = linkfirst ? 0 : 1;

		for (int i=first; i<gg.length; i++)
		{
			for (int j=0; j<h[i].length; j++)
				for (int k=0; k<h[i][j].length; k++)
					if (h[i][j][k] != null)
						p[i].remove(h[i][j][k]);

			Set<AlleleSegregation> f = new LinkedHashSet<AlleleSegregation>();
			for (Function x : p[i].getFunctions())
			{
				if (x instanceof AlleleTransmission)
				{
					Variable[] v = x.getVariables();
					AlleleSegregation z = new AlleleSegregation((Allele)v[0],(Allele)v[1],(Allele)v[3]);
					f.add(z);
				}
			}

			for (AlleleSegregation z: f)
				p[i].add(z);

			gg[i] = new GraphicalModel(p[i],usepotentials);
		}

		switch(opt)
		{
		case 3: threegens = makeThreeGens(d,linkfirst,gg);
		case 2: fulltwogens = makeFullTwoGens(d,linkfirst,gg);
			halftwogens = makeHalfTwoGens(d,linkfirst,gg);
		case 1: onegens = makeOneGens(d,linkfirst,gg);
		}

		return gg;
	}

	protected AuxGraphicalModel[] makeOneGens(GeneticDataSource d, boolean linkfirst, GraphicalModel[] gg)
	{
		Set<AuxGraphicalModel> s = new LinkedHashSet<AuxGraphicalModel>();

		int first = linkfirst ? 0 : 1;

		for (int j=0; j<h[first].length; j++)
		{
			if (h[first][j][0] == null || h[first][j][1] == null)
				 continue;

			MarkovRandomField p = new MarkovRandomField();
			AuxOneGenVariable prev = null;

			for (int i= (linkfirst ? 0 : 1) ; i<h.length; i++)
			{
				AuxOneGenVariable cur = new AuxOneGenVariable(h[i][j][0],h[i][j][1]);
				p.add(new AuxLocusFunction(cur,gg[i]));
				if (prev != null)
					p.add(new AuxLinkFunction(prev,cur,d.getMaleRecomFrac(i-1,i),d.getFemaleRecomFrac(i-1,i)));
				prev = cur;
			}

			s.add(new AuxGraphicalModel(p));
		}

		return (AuxGraphicalModel[]) s.toArray(new AuxGraphicalModel[0]);
	} 

	protected AuxGraphicalModel[] makeFullTwoGens(GeneticDataSource d, boolean linkfirst, GraphicalModel[] gg)
	{
		Set<AuxGraphicalModel> s = new LinkedHashSet<AuxGraphicalModel>();
		int[][] fam = d.nuclearFamilies();

		for (int k=0; k<fam.length; k++)
		{
			if (fam[k].length < 4)
				continue;

			MarkovRandomField p = new MarkovRandomField();
			AuxOneGenVariable prev = null;
			
			for (int i= (linkfirst ? 0 : 1) ; i<h.length; i++)
			{
				Inheritance[] pats = new Inheritance[fam[k].length-2];
				Inheritance[] mats = new Inheritance[fam[k].length-2];

				for (int j=2; j<fam[k].length; j++)
				{
					pats[j-2] = h[i][fam[k][j]][0];
					mats[j-2] = h[i][fam[k][j]][1];
				}

				AuxOneGenVariable cur = new AuxOneGenVariable(pats,mats);
				p.add(new AuxLocusFunction(cur,gg[i]));

				if (prev != null)
					p.add(new AuxLinkFunction(prev,cur,d.getMaleRecomFrac(i-1,i),d.getFemaleRecomFrac(i-1,i)));
				prev = cur;
			}

			s.add(new AuxGraphicalModel(p));
		}

		return (AuxGraphicalModel[]) s.toArray(new AuxGraphicalModel[0]);
	}

	protected AuxGraphicalModel[] makeHalfTwoGens(GeneticDataSource d, boolean linkfirst, GraphicalModel[] gg)
	{
		Set<AuxGraphicalModel> s = new LinkedHashSet<AuxGraphicalModel>();
		int[][] fam = d.nuclearFamilies();

		int[] mars = new int[d.nIndividuals()];
		boolean[] use = new boolean[mars.length];
		for (int i=0; i<use.length; i++)
			use[i] = false;
		
		for (int k=0; k<fam.length; k++)
		{
			mars[fam[k][0]] += 1;
			if (mars[fam[k][0]] > 1)
				use[fam[k][0]] = true;
			mars[fam[k][1]] += 1;
			if (mars[fam[k][1]] > 1)
				use[fam[k][1]] = true;
		}

		for (int j=0; j<use.length; j++)
		{
			if (!use[j])
				continue;

			MarkovRandomField p = new MarkovRandomField();
			AuxOneGenVariable prev = null;

			int[] kids = d.kids(j);

			for (int i= (linkfirst ? 0 : 1) ; i<h.length; i++)
			{
				Inheritance[] pats = new Inheritance[kids.length];
				Inheritance[] mats = new Inheritance[kids.length];

				for (int k=0; k<kids.length; k++)
				{
					pats[k] = h[i][kids[k]][0];
					mats[k] = h[i][kids[k]][1];
				}

				AuxOneGenVariable cur = new AuxOneGenVariable(pats,mats);
				p.add(new AuxLocusFunction(cur,gg[i]));

				if (prev != null)
					p.add(new AuxLinkFunction(prev,cur,d.getMaleRecomFrac(i-1,i),d.getFemaleRecomFrac(i-1,i)));
				prev = cur;
				
			}

			s.add(new AuxGraphicalModel(p));
		}

		return (AuxGraphicalModel[]) s.toArray(new AuxGraphicalModel[0]);
	}

	protected AuxGraphicalModel[] makeThreeGens(GeneticDataSource d, boolean linkfirst, GraphicalModel[] gg)
	{
		Set<AuxGraphicalModel> s = new LinkedHashSet<AuxGraphicalModel>();
		int[][] fam = d.nuclearFamilies();

		for (int k=0; k<fam.length; k++)
		{
			int nlp = 0;
			int nlm = 0;
			for (int j=2; j<fam[k].length; j++)
			{
				int[] kids = d.kids(fam[k][j]);
				if (kids.length == 0)
					continue;

				if (fam[k][j] == d.pa(kids[0]))
					nlp += kids.length;
				else
					nlm += kids.length;
			}

			if (nlp == 0 && nlm == 0)
				continue;

			int[] lp = new int[nlp];
			nlp = 0;
			int[] lm = new int[nlm];
			nlm = 0;

			for (int j=2; j<fam[k].length; j++)
			{
				int[] kids = d.kids(fam[k][j]);
				if (kids.length == 0)
					continue;

				if (fam[k][j] == d.pa(kids[0]))
				{
					for (int l=0; l<kids.length; l++)
						lp[nlp++] = kids[l];
				}
				else
				{
					for (int l=0; l<kids.length; l++)
						lm[nlm++] = kids[l];
				}
			}

			MarkovRandomField p = new MarkovRandomField();
			AuxVariable prev = null;

			for (int i= (linkfirst ? 0 : 1) ; i<h.length; i++)
			{
				Inheritance[] hipats = new Inheritance[fam[k].length-2];
				Inheritance[] himats = new Inheritance[fam[k].length-2];
				
				for (int j=2; j<fam[k].length; j++)
				{
					hipats[j-2] = h[i][fam[k][j]][0];
					himats[j-2] = h[i][fam[k][j]][1];
				}

				Inheritance[] lopats = new Inheritance[lp.length];
				for (int j=0; j<lopats.length; j++)
					lopats[j] = h[i][lp[j]][0];
				
				Inheritance[] lomats = new Inheritance[lm.length];
				for (int j=0; j<lomats.length; j++)
					lomats[j] = h[i][lm[j]][1];

				AuxVariable cur = new AuxThreeGenVariable(hipats,himats,lopats,lomats);
				p.add(new AuxLocusFunction(cur,gg[i]));

				if (prev != null)
					p.add(new AuxLinkFunction(prev,cur,d.getMaleRecomFrac(i-1,i),d.getFemaleRecomFrac(i-1,i)));
				prev = cur;
			}

			s.add(new AuxGraphicalModel(p));
		}

		return (AuxGraphicalModel[]) s.toArray(new AuxGraphicalModel[0]);
	}
}
