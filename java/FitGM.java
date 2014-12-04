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


import jpsgcs.alun.markov.Function;
import jpsgcs.alun.markov.Variable;
import jpsgcs.alun.jtree.DiscreteData;
import jpsgcs.alun.jtree.JTree;
import jpsgcs.alun.jtree.JTreeSampler;
import jpsgcs.alun.jtree.UniformDecomposable;
import jpsgcs.alun.graph.Network;
import jpsgcs.alun.viewgraph.GraphFrame;
import jpsgcs.alun.util.Monitor;
import jpsgcs.alun.util.Main;
import jpsgcs.alun.jtree.DataMatrix;
import java.util.ConcurrentModificationException;

/**
	This program estimates a graphical model for discrete multivariate data.
<ul>
	Usage : <b> java FitGM < data [s] [-v] </b>
</ul>
	where
<ul>
	<li> <b> data </b> is the input data file. </li>
	<li> <b> s </b> is an optional parameter specifying the total number of iterations to be carried out.
	The default is 1000000. </li>
	<li> <b> -v </b> is a visualization option. If specified, a GUI showing the 
	conditional indpendence graph for the model is displayed. </li>
	
</ul>
<p>
	The input is assumed to have a line for each multivariate observation.
	Each line consists of a white space separated list of integer values.
	The multivariate observations are assumed to be independent and complete.

<p>
	The model is estimated by two rounds of Markov chain Monte Carlos searching.
	The first round of iterations is done with Metropolis sampling. The second
	is a random uphill search.
*/

public class FitGM
{
	public static void main(String[] args)
	{
		try
		{
			Monitor.quiet(true);

			boolean visualize = false;
			double penalty = 1;
			int totalits = 1000000;
			int randomits = 1000;

			String[] bargs = Main.strip(args,"-v");
			if (bargs != args)
			{
				visualize = true;
				args = bargs;
			}
			
			switch (args.length)
			{
			case 1: totalits = new Integer(args[0]).intValue();

			case 0: break;

			default:
				System.err.println("Usage: java FitGMLD [s] [-v]");
				System.exit(1);
			}

			DataMatrix data = new DataMatrix();
			penalty *= 0.5 * Math.log(data.nRows());
			DiscreteData scorer= new DiscreteData(data.getVariables(),data,penalty,1);

			Network<Variable,Object> g = new Network<Variable,Object>();
			for (Variable v : data.getVariables())
				g.add(v);
			GraphFrame frame = ( visualize ? new GraphFrame<Variable,Object>(g) : null );

			JTree<Variable> jt = new JTree<Variable>(g);
			JTreeSampler<Variable> jts = new UniformDecomposable<Variable>(jt,scorer);
			jts.randomize();

			// Go into the simulation loop.

			boolean[] both = {true, false};
			
			for (boolean random : both)
			{
				scorer.setTemperature(random ? 1 : 0);
				scorer.clear();

				for (int i=1; i<=totalits; i++)
				{
					if (i % randomits == 0)
					{
						jts.randomize();
						System.err.print(".");
					}
						
					if (Math.random() < 0.5)
						jts.randomConnection();
					else
						jts.randomDisconnection();
				}

				System.err.println();
			}

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
}
