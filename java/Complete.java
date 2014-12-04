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


import jpsgcs.alun.util.Monitor;
import jpsgcs.alun.util.InputFormatter;
import jpsgcs.alun.genio.GeneticDataSource;
import jpsgcs.alun.linkage.Linkage;
import jpsgcs.alun.linkage.LinkageInterface;
import jpsgcs.alun.linkage.SteppedLinkageData;
import jpsgcs.alun.markov.GraphicalModel;
import jpsgcs.alun.markov.MarkovRandomField;
import jpsgcs.alun.markov.Function;
import jpsgcs.alun.markov.Variable;
import jpsgcs.alun.pedmcmc.Genotype;
import jpsgcs.alun.pedmcmc.GenotypePenetrance;
import jpsgcs.alun.pedmcmc.GenotypeErrorPenetrance;
import jpsgcs.alun.pedmcmc.GenotypePrior;
import jpsgcs.alun.pedmcmc.LDModel;
import jpsgcs.alun.pedmcmc.Error;
import jpsgcs.alun.pedmcmc.ErrorPrior;
import jpsgcs.alun.util.Main;
import jpsgcs.alun.linkage.LinkageFormatter;

/**
	This program takes genotype data, specified as LINKAGE parameter and pedigree files,
	and either imputes or samples complete phase known hapolotype data, using a specified
	graphical model for linkage disequilibrum. 
<ul>
	<li> Usage : <b> java Complete input.parld input.ped > output.ped [-r] </b><li>
</ul>
	where
<ul>
<li> <b> input.parld </b> is a LINKAGE parameter file with a graphical model for LD attached as 
made by the FitGMLD program </li>
<li> <b> input.ped </b> is the input LINKAGE pedigree file </li>
<li> <b> -r </b> is an optional parameter which will make the program sample the haplotype imputation
at random from the posterior distribution of haplotypes given the genotype data. The default action
is to find a haplotype configuration that maximizes the posterior probability. </li>
</ul>
<p>
	The output is a linkage pedigree file with
	the same structure as the input pedigree file but with missing genotypes imputed, and
	the alleles phased so that the first allele at each locus is on one haplotype and the
	second on the other.
<p>
	The individuals are assumed to be unrelated, and any relationships specified in the
	input pedigree file will be ignored in the haplotyping.
	
*/

public class Complete
{
	public static void main(String[] args)
	{
		try
		{
			Monitor.quiet(true);

			boolean sample = false;
			double eprob = 0.001;

			LinkageFormatter parin = null;
			LinkageFormatter pedin = null;

			String[] bargs = Main.strip(args,"-r");
			if (bargs != args)
			{
				sample = true;
				args = bargs;
			}

			switch (args.length)
			{
			case 2: parin = new LinkageFormatter(args[0]);
				pedin = new LinkageFormatter(args[1]);
				break;

			default:
				System.err.println("Usage: java Complete in.parld in.ped [-r] > out.ped");
				System.exit(1);
			}

			SteppedLinkageData sld = new SteppedLinkageData(parin,pedin);
			GeneticDataSource x = new LinkageInterface(sld);

			LDModel ld = new LDModel(parin);
			if (ld.getLocusVariables() == null)
			{
				System.err.println("Warning: parameter file has no LD model appended.");
				System.err.println("Will assume linakge equilibrium and given allele frequencies.");
				ld = new LDModel(x);
			}

			GenotypePenetrance[] p = new GenotypePenetrance[x.nLoci()];
			Genotype[] g = new Genotype[x.nLoci()];
			MarkovRandomField m = new MarkovRandomField();

			Variable dummy = null;

			for (int i=0; i<p.length; i++)
			{
				double[] f = x.alleleFreqs(i);
				g[i] = new Genotype(f.length);
				Error err = new Error();
				p[i] = new jpsgcs.alun.pedapps.CompletingPenetrance(g[i],err,dummy);
				m.add(p[i]);
				m.add(new ErrorPrior(err,eprob));
			}

			Monitor.show("Read     ");

			for (Function f : ld.duplicate(g).getFunctions())
				m.add(f);

			m.remove(dummy);

			Monitor.show("Duplicated ");

			GraphicalModel gm = new GraphicalModel(m,true);

			Monitor.show("Imputing ");

			while (sld.next())
			{
				for (int i=0; i<x.nLoci(); i++)
					p[i].fix(x.penetrance(i,0));
				if (sample)
					gm.simulate();
				else
					gm.maximize();
				
				for (int i=0; i<x.nLoci(); i++)
					x.setAlleles(i,0,g[i].pat(),g[i].mat());
				
				x.writeIndividual(0,System.out);
				System.out.println();
				System.err.print(".");
			}

			System.err.println();

			Monitor.show("Done     ");
		}
		catch (Exception e)
		{
			System.err.println("Caught in Complete.main()");
			e.printStackTrace();
		}
	}
}
