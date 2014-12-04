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


import jpsgcs.alun.util.InputFormatter;
import jpsgcs.alun.genio.GeneticDataSource;
import jpsgcs.alun.linkage.LinkageParameterData;
import jpsgcs.alun.linkage.LinkagePedigreeData;
import jpsgcs.alun.linkage.LinkageInterface;
import jpsgcs.alun.linkage.LinkageDataSet;
import jpsgcs.alun.linkage.LinkageFormatter;
import jpsgcs.alun.linkage.Linkage;
import jpsgcs.alun.pedmcmc.LDModel;
import jpsgcs.alun.pedapps.CompleteHaplotypes;
import jpsgcs.alun.markov.Function;
import jpsgcs.alun.markov.Variable;
import jpsgcs.alun.jtree.DiscreteData;
import jpsgcs.alun.jtree.JTree;
import jpsgcs.alun.jtree.JTreeSampler;
import jpsgcs.alun.jtree.UniformJTree;
import jpsgcs.alun.jtree.UniformDecomposable;
import jpsgcs.alun.graph.Network;
import jpsgcs.alun.viewgraph.GraphFrame;
import jpsgcs.alun.util.Monitor;
import jpsgcs.alun.util.Main;

import java.io.PrintStream;
import java.util.ConcurrentModificationException;

public class FitGMLDNoWind
{
	public static void main(String[] args)
	{
		try
		{
			Monitor.quiet(false);

			boolean visualize = false;
			double eprob = 0.001;
			double penalty = 1;

			int totalits = 1000000;
			int metropergibbs = 10000;
			int randomits = 1000;

			String[] bargs = Main.strip(args,"-v");
			if (bargs != args)
			{
				visualize = true;
				args = bargs;
			}

			GeneticDataSource x = null;
			LinkageParameterData par = null;
			LinkagePedigreeData ped = null;

			switch (args.length)
			{
			case 6: eprob = new Double(args[5]).doubleValue();

			case 5: penalty = new Double(args[4]).doubleValue();
		
			case 4: metropergibbs = new Integer(args[3]).intValue();

			case 3: totalits = new Integer(args[2]).intValue();

			case 2: par = new LinkageParameterData(new LinkageFormatter(args[0]));
                                ped = new LinkagePedigreeData(new LinkageFormatter(args[1]),par);
                                x = new LinkageInterface(new LinkageDataSet(par,ped));
				break;

			default:
				System.err.println("Usage: java FitGMLDNoWind in.par in.ped [s] [m] [-v]");
				System.exit(1);
			}

			Monitor.show("Read");

			// Initalize the LD model and get the variables.

			LDModel ld = new LDModel(x);
			Variable[] loci = ld.getLocusVariables();

			Monitor.show("LDModel");

			// Set up a view of the input genotypes as a completely imputed haplotypes set.

			CompleteHaplotypes haps = new CompleteHaplotypes(x,ld,eprob);
			haps.simulate();

			// Give the data to a log-likelihood/df calculator.

			penalty *= 0.5 * Math.log(haps.getHaplotypes().length);
			DiscreteData scorer= new DiscreteData(loci,haps,penalty,1);

			Monitor.show("Scorer");

			// Set the inital graphical model to be the trivial graph, and find the junction tree.
			// Make a graph viewer if required.

			Network<Variable,Object> g = new Network<Variable,Object>();
			for (Variable v : loci)
				g.add(v);

			for (int i=1; i<loci.length; i++)
				g.connect(loci[i],loci[0]);

			GraphFrame frame = ( visualize ? new GraphFrame<Variable,Object>(g) : null );


			// Set up the sampler, and randomize the intitial configuration.

			JTree<Variable> jt = new JTree<Variable>(g);
			JTreeSampler<Variable> jts = new UniformDecomposable<Variable>(jt,scorer);
			jts.randomize();

			Monitor.show("Randomized");

			// Go into the simulation loop.

			for (int i=1; i<=totalits; i++)
			{
				if (i % metropergibbs == 0)
				{
					ld.clear();
					for (Function f : scorer.fitModel(jt,true).getFunctions())
						ld.add(f);

					haps.setModel(ld);
					haps.simulate();
					scorer.clear();

					System.err.print("-");
				}

				if (i % randomits == 0)
					jts.randomize();
					
				if (Math.random() < 0.5)
					jts.randomConnection();
				else
					jts.randomDisconnection();
			}

			System.err.println();

			// Go into the maximization loop.

			scorer.setTemperature(0);
			scorer.clear();

			for (int i=1; i<=totalits; i++)
			{
				if (i % metropergibbs == 0)
				{
					ld.clear();
					for (Function f : scorer.fitModel(jt,false).getFunctions())
						ld.add(f);

					haps.setModel(ld);
					haps.maximize();
					scorer.clear();

					System.err.print("+");
				}

				if (i % randomits == 0)
					jts.randomize();
					
				if (Math.random() < 0.5)
					jts.randomConnection();
				else
					jts.randomDisconnection();
			}

			System.err.println();

			// Output model.

			par.writeTo(System.out);
			System.out.println();
			ld.writeStateSizes(System.out);
			ld.writeFunctions(System.out);

			Monitor.show("Done");
		}
		catch (ConcurrentModificationException cme)
		{
		}
		catch (Exception e)
		{
			System.err.println("Caught in FitGMLDNoWind.main()");
			e.printStackTrace();
		}
	}
}
