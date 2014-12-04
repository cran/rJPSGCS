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


package jpsgcs.alun.zeroloop;

import jpsgcs.alun.graph.Network;
import jpsgcs.alun.markov.Variable;
import jpsgcs.alun.markov.MarkovRandomField;
import jpsgcs.alun.markov.GraphicalModel;
import java.util.Set;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class ZeroLoopPedGenerator
{
	public ZeroLoopPedGenerator(int nmar, int nind)
	{
		this(nmar,nind,false,true);
	}

	public ZeroLoopPedGenerator(int nmar, int nind, boolean monog, boolean relab)
	{
		ind = new LinkedHashSet<Individual>();
		for (int i=0; i<nind; i++)
			ind.add(new Individual(i+1));

		mar = new LinkedHashSet<Marriage>();
		for (int i=0; i<nmar; i++)
			mar.add(new Marriage());

		mono = monog;
		relabel = relab;

		pb = new RandomPedTree<PedNode,Edge>(new LinkedHashSet<PedNode>(mar),new LinkedHashSet<PedNode>(ind),mono);
	}

	public List<Individual> next()
	{
		// Initialise marriages and individuals
		for (Individual i : ind)
			i.par = null;
		for (Marriage m : mar)
			m.pa = m.ma = null;

		// Link the marriages and individuals.
		for (Edge e : nextEdges())
		{
			Individual ii = (Individual) e.to();
			Marriage mm = (Marriage) e.from();

			if (e.getState() == 0)
			{
				ii.par = mm;
			}
			else
			{
				if (mm.pa == null)
					mm.pa = ii;
				else
					mm.ma = ii;
			}
		}
			
		// Choose sexes of individuals to be compatible.
		Network<Individual,Object> g = new Network<Individual,Object>();
		for (Individual i : ind)
			g.add(i);
		for (Marriage m : mar)
			g.connect(m.pa,m.ma);

		Set<Individual> all = new LinkedHashSet<Individual>(g.getVertices());

		while (!all.isEmpty())
		{
			Individual x = all.iterator().next();
			x.sex = Math.random() < 0.5 ? 0 : 1;

			LinkedList<Individual> toDo = new LinkedList<Individual>();
			toDo.add(x);;
			all.remove(x);
			
			while (!toDo.isEmpty())
			{
				x = toDo.remove();
				for (Individual n : g.getNeighbours(x))
				{
					if (all.contains(n))
					{
						n.sex = 1-x.sex;
						all.remove(n);
						toDo.add(n);
					}
				}
			}
		}

		for (Marriage m : mar)
			if (m.pa.sex == 0)
			{
				Individual x = m.pa;
				m.pa = m.ma;
				m.ma = x;
			}


		// Sort individuals in pedigree order.

		Set<Individual> done = new LinkedHashSet<Individual>();
		for (Individual i : ind)
			add(done,i);

		if (relabel)
		{
			int id = 1;
			for (Individual i : done)
				i.nm = id++;
		}

		return new ArrayList<Individual>(done);
	}

// Private data and methods.

	private Set<Individual> ind = null;
	private Set<Marriage> mar = null;
	private RandomPedTree<PedNode,Edge> pb = null;
	private boolean mono = false;
	private boolean relabel = true;

	private Collection<Edge> nextEdges()
	{
		Network<PedNode,Edge> t = pb.next();
		for (Marriage m : mar)
			for (PedNode i : t.getNeighbours(m))
				t.connect(m,i,new Edge(m,i));

		MarkovRandomField p = new MarkovRandomField();
		for (Individual i : ind)
		{
			p.add(new OneParentalMarriageConstraint(t.connections(i)));
			if (mono)
				p.add(new MonogamyConstraint(t.connections(i)));
		
		}

		for (Marriage m : mar)
			p.add(new TwoParentConstraint(t.connections(m)));

		GraphicalModel m = new GraphicalModel(p,false);
		m.simulate();

		Set<Edge> out = new LinkedHashSet<Edge>();
		for (PedNode q : t.getVertices())
			out.addAll(t.connections(q));
		return out;
	}

	private void add(Set<Individual> d, Individual i)
	{
		if (d.contains(i))
			return;
	
		if (i.par != null)
		{
			add(d,i.par.pa);
			add(d,i.par.ma);
		}

		d.add(i);
	}

/**
	Test main.
*/
	public static void main(String[] args)
	{
		try
		{
			int m = 0;
			int n = 0;

			switch(args.length)
			{
			case 2:
				m = new Integer(args[0]).intValue();
				n = new Integer(args[1]).intValue();
				break;
			default:
				System.out.println("Usage: ZeroLoopPedGenerator nmars ninds");
				System.exit(0);
			}

			if (n < 2*m+1)
			{
				System.out.println("Error: ninds must be >= 2*nmars+1");
				System.exit(0);
			}

			ZeroLoopPedGenerator z = new ZeroLoopPedGenerator(m,n);
			List<Individual> ped = z.next();
			for (Individual t : z.next())
				System.out.println(t);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
