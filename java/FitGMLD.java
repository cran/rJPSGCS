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


import jpsgcs.alun.genio.GeneticDataSource;
import jpsgcs.alun.jtree.MultinomialMLEScorer;
import jpsgcs.alun.pedapps.PhaseKnownHaplotypes;
import jpsgcs.alun.jtree.ProperIntervalJTree;

import jpsgcs.alun.genio.BasicGeneticData;
import jpsgcs.alun.linkage.Linkage;
import jpsgcs.alun.linkage.LinkageInterface;
import jpsgcs.alun.linkage.LinkageParameterData;
import jpsgcs.alun.linkage.LinkageFormatter;
import jpsgcs.alun.linkage.LinkageDataSet;
import jpsgcs.alun.linkage.TransposedPedigreeData;
import jpsgcs.alun.pedmcmc.LDModel;
import jpsgcs.alun.pedapps.CompleteHaplotypes;
import jpsgcs.alun.markov.Function;
import jpsgcs.alun.markov.GraphicalModel;
import jpsgcs.alun.markov.Table;
import jpsgcs.alun.markov.Variable;
import jpsgcs.alun.jtree.DiscreteData;
import jpsgcs.alun.jtree.JTree;
import jpsgcs.alun.jtree.JTreeSampler;
import jpsgcs.alun.jtree.OrderedJTree;
//import jpsgcs.alun.jtree.OrderedDecomposable;
//import jpsgcs.alun.jtree.OrderedGiudiciGreen;
import jpsgcs.alun.graph.Network;
import jpsgcs.alun.graph.NetworkMask;
import jpsgcs.alun.viewgraph.GraphFrame;
import jpsgcs.alun.util.InputFormatter;
import jpsgcs.alun.util.Monitor;
import jpsgcs.alun.util.Main;

import java.io.PrintStream;
import java.util.ConcurrentModificationException;
import java.util.Set;
import java.util.LinkedHashSet;

public class FitGMLD
{
	public static void main(String[] args)
	{
		try
		{
		// Set paramters to default vaules.

			Monitor.quiet(false);

			boolean[] both = {true, false};
			boolean visualize = false;
                        boolean proper = false;

			int maxlink = 35;
			int windowwidth = 250;
			int gibbsperwindow = 50;
			int optgibbsperwindow = 5;
			int metropergibbsperlocus = 200;
			double penalty = 0.25;
			double eprob = 0.001;

			LinkageParameterData par = null;
			TransposedPedigreeData ped = null;
			GeneticDataSource x = null;
                        boolean phaseknown = false;
			PrintStream out_stream = System.out;


		// Read options and data.

			String[] bargs = Main.strip(args,"-v");
			if (bargs != args)
			{
				visualize = true;
				args = bargs;
			}

                        bargs = Main.strip(args,"-phaseknown");
                        if (bargs != args)
                        {
                                phaseknown = true;
                                args = bargs;
                        }

                        bargs = Main.strip(args,"-proper");
                        if (bargs != args)
                        {
                                proper = true;
                                args = bargs;
                        }

			switch (args.length)
			{

			case 9: eprob = new Double(args[8]).doubleValue();

			case 8: penalty = new Double(args[7]).doubleValue();
			
			case 7: metropergibbsperlocus = new Integer(args[6]).intValue();
	
			case 6: gibbsperwindow = new Integer(args[5]).intValue();
		
			case 5: windowwidth = new Integer(args[4]).intValue();

			case 4: maxlink = new Integer(args[3]).intValue();

			case 3: out_stream = new PrintStream(args[2]);

			case 2: par = new LinkageParameterData(new LinkageFormatter(args[0]));
				ped = new TransposedPedigreeData(new LinkageFormatter(args[1]),par);
				x = new LinkageInterface(new LinkageDataSet(par,ped));
				break;

			default:
				System.err.println("Usage: java FitGMLD input.par input.tped out.ld.par [maxlink] [windowwidth] [gibbsperwind] [metropergibbslocus] [penalty] [err prob] [-v]");
				System.exit(1);
			}

		// Write the parameter data to the output file.

			par.writeTo(out_stream);
			out_stream.println();

		// Initalize the LD model and get the variables.

			LDModel ld = new LDModel(x);
			ld.clear();
			ld.writeStateSizes(out_stream);
			Variable[] loci = ld.getLocusVariables();

		// Set the inital graphical model to be have Markov structure with specified lag.
		// Make a graph viewer if required.

			Network<Variable,Object> gg = new Network<Variable,Object>();
                        for (int i=0; i<loci.length; i++)
                                gg.add(loci[i]);

			int lag = 2;
			for (int i=0; i<loci.length; i++)
			{
				int low = i-lag;
				if (low < 0)
					low = 0;
				for (int j=low; j<i; j++)
					gg.connect(loci[j],loci[i]);
			}

			NetworkMask<Variable,Object> g = new NetworkMask<Variable,Object>(gg);
			g.hideAll();

			GraphFrame frame = ( visualize ? new GraphFrame<Variable,Object>(g.completeGraph()) : null );

		// Find and read in initial window of data.

			int held = 3 * windowwidth / 2 + 2*2*maxlink;
			if (held > x.nLoci())
				held = x.nLoci();

			ped.readGeneticData(held);

		// Make and initialize haplotypes in the window.

			CompleteHaplotypes haps = null;
                       if (phaseknown)
                       {
                               haps = new PhaseKnownHaplotypes(x,held);
                               System.err.println("Warning: you have chosen the -phaseknown option.");
                               System.err.println("This assumes that the the order of the alleles in the input pedigree file specifies");
                               System.err.println("the phase. It also requries the data to have no missing values.");
                               System.err.println("It also proabably means that your data is imputed or simulated rather than real.");
                       }
                       else
                               haps = new CompleteHaplotypes(x,eprob,held);

			ld.initialize(0,held);
			haps.setModel(ld);
			haps.update(true);
			MultinomialMLEScorer scorer= new MultinomialMLEScorer(loci,haps,penalty*0.5*Math.log(haps.nRows()),1);

		// Go into main MCMC loop.

			Monitor.show("Setup time = ");

			for (int low = -windowwidth; ; low += windowwidth/2)
			{
			// If done, output remaining model.

				if (low >= loci.length)
				{
					appendToModel(ld,scorer,g,ped.firstLocus(),loci.length,maxlink, out_stream);
					break;
				}

			// Find the loci needed for this window and get more if needed.
			// Output current model elements and move haplotype window also.

				int get = low + 3*windowwidth/2 + 2*maxlink - held;
				if (get + held > loci.length)
					get = loci.length - held;

				if (get > 0)
				{
					appendToModel(ld,scorer,g,ped.firstLocus(),ped.firstLocus()+get,maxlink, out_stream);
					ped.readNextData(get);
					ld.initialize(held,get);
					haps.shift(get);
					haps.setModel(ld);
					haps.update(true);
					scorer.clear();
					held += get;
				}

			// Now do the sampling.

			 	for (boolean random : both)
				{
					int ll = random ? low + windowwidth/2: low;
                                        Set<Variable> order = new LinkedHashSet<Variable>();
                                        Set<Variable> wind = showWindow(g,loci,ll,windowwidth,maxlink,order);
					if (wind.isEmpty())
						continue;

					JTree<Variable> jt = new JTree<Variable>(g,order);
					JTreeSampler<Variable> jts = null;
                                        if (proper)
                                               jts = new ProperIntervalJTree<Variable>(jt,loci,maxlink,wind,scorer);
                                        else
                                               jts = new OrderedJTree<Variable>(jt,loci,maxlink,wind,scorer);
					scorer.setTemperature(random ? 1 : 0);

					int nits = random ? gibbsperwindow : optgibbsperwindow;

					for (int i=0; i<nits; i++)
					{
						ld.clear();
						for (Function f : scorer.fitModel(jt).getFunctions())
							ld.add(f);
						haps.setModel(ld);
						haps.update(random);
						scorer.clear();

						for (int j=0; j<metropergibbsperlocus*windowwidth; j++)
						{
							if (Math.random() < 0.5)
								jts.randomConnection();
							else
								jts.randomDisconnection();
						}
						jts.randomize();
						System.err.print(random ? "-" : "+");
					}

					hideWindow(g,loci,ll,windowwidth,maxlink);
				}

				System.err.println();
			}
	
			Monitor.show("Done  =   ");
			out_stream.close();
		}
		catch (ConcurrentModificationException cme)
		{
		}
		catch (Exception e)
		{
			System.err.println("Caught in FitGMLD.main()");
			e.printStackTrace();
		}
	}

	public static void appendToModel(LDModel ldmod, MultinomialMLEScorer scorer, NetworkMask<Variable,Object> g,int from, int to, int maxlink, PrintStream out_stream)
	{
		Variable[] loci = ldmod.getLocusVariables();

		for (int i=from; i<to; i++)
		{
			g.show(loci[i]);
			for (Variable n : g.completeGraph().getNeighbours(loci[i]))
				g.show(n);
		}

		for (int i=from-1; i>=from-maxlink && i>=0; i--)
			g.hide(loci[i]);

		JTree<Variable> jtree = new JTree<Variable>(g);
		for (Function f : scorer.fitModel(jtree).getFunctions())
			ldmod.writeFunction(f,out_stream);

		for (int i=from; i<to; i++)
			g.hide(loci[i]);

		if (g.getVertices().size() > 0)
		{
			jtree = new JTree<Variable>(g);
			for (Function f : scorer.fitModel(jtree).getFunctions())
			{
				((Table)f).invert();
				ldmod.writeFunction(f,out_stream);
			}

			for (int i=to; i<to+maxlink && i<loci.length; i++)
				g.hide(loci[i]);
		}
	}

	public static void hideWindow(NetworkMask<Variable,Object> g, Variable[] loci, int low, int windowwidth, int maxlink)
	{
		int high = low + windowwidth;
		if (high > loci.length)
			high = loci.length;

		int ll = low - 2*maxlink;
		if (ll < 0)
			ll = 0;
	
		int hh = high + 2*maxlink;
		if (hh > loci.length)
			hh = loci.length;

		for (int i=ll; i<hh; i++)
			g.hide(loci[i]);
	}

	public static Set<Variable> showWindow(NetworkMask<Variable,Object> g, Variable[] loci, int low, int windowwidth, int maxlink, Set<Variable> order)
	{
		int high = low + windowwidth;
		if (high > loci.length)
			high = loci.length;

		Set<Variable> wind = new LinkedHashSet<Variable>();
		for (int i= (low < 0 ? 0 : low) ; i<high; i++)
			wind.add(loci[i]);
	
		int ll = low - 2*maxlink;
		if (ll < 0)
			ll = 0;
	
		int hh = high + 2*maxlink;
		if (hh > loci.length)
			hh = loci.length;

		for (int i=ll; i<hh; i++)
		{
			g.show(loci[i]);
			order.add(loci[i]);
		}
		return wind;
	}
}
