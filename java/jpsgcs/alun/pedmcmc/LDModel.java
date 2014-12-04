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

import jpsgcs.alun.util.InputFormatter;
import jpsgcs.alun.genio.ParameterData;
import jpsgcs.alun.markov.MarkovRandomField;
import jpsgcs.alun.markov.Variable;
import jpsgcs.alun.markov.MultiVariable;
import jpsgcs.alun.markov.Function;
import jpsgcs.alun.markov.DenseTable;
import jpsgcs.alun.markov.Table;

import java.util.Vector;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.LinkedHashSet;
import java.io.PrintStream;
import java.io.IOException;

public class LDModel extends MarkovRandomField
{
	public LDModel(Variable[] l)
	{
		setLoci(l);
	}

	public LDModel(ParameterData d)
	{
		loc = new Variable[d.nLoci()];
		for (int i=0; i<d.nLoci(); i++)
			loc[i] = new Variable(d.nAlleles(i));
		setLoci(loc);

		for (int i=0; i<d.nLoci(); i++)
		{
			Variable[] xx = {loc[i]};
			DenseTable dt = new DenseTable(xx);
			dt.setTable(d.alleleFreqs(i));
			add(dt);
		}

		par = d;
	}

	public void initialize(int low, int wid)
	{
		clear();

		for (int i=low; i<low+wid; i++)
		{
			Variable[] xx = {loc[i]};
			DenseTable dt = new DenseTable(xx);
			dt.setTable(par.alleleFreqs(i));
			add(dt);
		}
	}

	public LDModel(InputFormatter f) throws IOException
	{
		this(f,false);
	}

	public LDModel(InputFormatter f, boolean longform) throws IOException
	{
		boolean got = false;

		if (longform)
			got = readLoci(f);
		else
			got = readStateSizes(f);

		if (got)
			readFunctions(f);
	}

	public boolean readLoci(InputFormatter f) throws IOException
	{
		if (!f.newLine())
			return false;

		loc = new Variable[f.nextInt()];
		
		for (int i=0; i<loc.length; i++)
		{
			f.newLine();
			int[] s = new int[f.nextInt()];
			for (int j=0; j<s.length; j++)
				s[j] = f.nextInt();

			loc[i] = new Variable(s);
		}

		setLoci(loc);
		return true;
	}

	public boolean readStateSizes(InputFormatter f) throws IOException
	{
		if (!f.newLine())
			return false;

		Vector<Variable> vv = new Vector<Variable>();
		for (int i=0; f.newToken(); i++)
			vv.add(new Variable(f.getInt()));

		loc = new Variable[vv.size()];
		for (int i=0; i<loc.length; i++)
			loc[i] = vv.get(i);

		setLoci(loc);
		return true;
	}

	public void readFunctions(InputFormatter f) throws IOException
	{
		while (f.newLine())
		{
			Vector<Variable> vl = new Vector<Variable>();
			
			while (f.newToken())
				vl.add(loc[f.getInt()]);
			f.newLine();
		
			Variable[] u = (Variable[]) vl.toArray(new Variable[0]);

			MultiVariable m = new MultiVariable(u);
			Table t = new DenseTable(m);
			t.allocate();

			for (m.init(); m.next(); )
				t.setValue(f.nextDouble());

			add(t);
		}
	}
/*
		Vector<Variable> vv = new Vector<Variable>();
		f.newLine();
		for (int i=0; f.newToken(); i++)
			vv.add(new Variable(f.getInt()));

		loc = new Variable[vv.size()];
		for (int i=0; i<loc.length; i++)
			loc[i] = vv.get(i);
		setLoci(loc);

		while (f.newLine())
		{
			Vector<Variable> vl = new Vector<Variable>();
			
			while (f.newToken())
				vl.add(loc[f.getInt()]);
			f.newLine();
		
			Variable[] u = (Variable[]) vl.toArray(new Variable[0]);

			MultiVariable m = new MultiVariable(u);
			Table t = new DenseTable(m);
			t.allocate();

			for (m.init(); m.next(); )
				t.setValue(f.nextDouble());

			add(t);
		}
*/

	public void writeTo(PrintStream p)
	{
		writeTo(p,false);
	}

	public void writeTo(PrintStream p, boolean longform)
	{
		if (longform)
			writeVariables(p);
		else
			writeStateSizes(p);

		writeFunctions(p);
	}

	public void writeStateSizes(PrintStream p)
	{
		for (Variable l : loc)
			p.print(" "+l.getNStates());
		p.println();
		p.flush();
	}

	public void writeVariables(PrintStream p)
	{
		p.println(loc.length);
		for (Variable l : loc)
		{
			p.print(l.getNStates());
			for (l.init(); l.next(); )
				p.print(" "+l.getState());
			p.println();
		}
		p.flush();
	}

	public void writeFunctions(PrintStream p)
	{
		for (Function t : getFunctions())
			writeFunction(t,p);
		p.flush();
	}

	public void writeFunction(Function t, PrintStream p)
	{
		Variable[] u = t.getVariables();

		for (int i=0; i<u.length; i++)
			p.print(" "+index(u[i]));
		p.print("\n");

		MultiVariable m = new MultiVariable(u);
		for (m.init(); m.next(); )
			p.print(" "+t.getValue());

		p.print("\n");
	}

/*
	public void writeTo(PrintWriter p)
	{
		for (Variable l : loc)
			p.print(" "+l.getNStates());
		p.println();

		for (Function t : getFunctions())
		{
			Variable[] u = t.getVariables();

			for (int i=0; i<u.length; i++)
				p.print(" "+index(u[i]));
			p.print("\n");

			MultiVariable m = new MultiVariable(u);
			for (m.init(); m.next(); )
				p.print(" "+t.getValue());

			p.print("\n");
		}

		p.flush();
	}
*/

/*
	public void writeTo(PrintStream s)
	{
		writeTo(new PrintWriter(s));
	}
*/

	public MarkovRandomField duplicate(Genotype[] g)
	{
		if (loc.length != g.length)
			throw new RuntimeException("Variable array length missmatch: LDModel.duplicate()");

		Map<Variable,Genotype> map = new LinkedHashMap<Variable,Genotype>();

		for (int i=0; i<loc.length; i++)
		{
			if (g[i].na != loc[i].getNStates())
				throw new RuntimeException("State space size missmatch: LDModel.duplicate()");
			map.put(loc[i],g[i]);
		}

		return duplicate(map);
	}

	public MarkovRandomField duplicate(Map<Variable,Genotype> map)
	{
		MarkovRandomField dup = new MarkovRandomField();
		for (Function f : getFunctions())
			dup.add(duplicate(map,f));

		Set<Variable> rem = new LinkedHashSet<Variable>();
		
		for (Object x : dup.bipartiteGraph().getVertices())
			if (x instanceof Variable)
				rem.add((Variable)x);

		for (Object x : g.getVertices())
			if (x instanceof Variable)
				rem.remove(map.get(x));

		for (Object x : rem)
			dup.remove(x);

		return dup;
	}

	private Function duplicate(Map<Variable,Genotype> map, Function f)
	{
		Set<Variable> v = new LinkedHashSet<Variable>();
		for (Variable x : f.getVariables())
			v.add(map.get(x));

		MultiVariable w = new MultiVariable(v);
		DenseTable t = new DenseTable(w);
		t.allocate();

		for (w.init(); w.next(); )
		{
			double z = 1;
	
			for (Variable x : f.getVariables())
				x.setState(map.get(x).pat());
			z *= f.getValue();

			for (Variable x : f.getVariables())
				x.setState(map.get(x).mat());
			z *= f.getValue();

			t.setValue(z);
		}

		return t;
	}

	public Variable[] getLocusVariables()
	{
		return loc;
	}

	public int[] usedLoci()
	{
		Set<Integer> x = new LinkedHashSet<Integer>();
		for (Object v : g.getVertices())
			if (v instanceof Variable)
				x.add(map.get(v));

		int[] s = new int[x.size()];
		int i = 0;
		for (Integer ii : x)
			s[i++] = ii.intValue();

		return s;
	}

	public void report()
	{
		System.err.println(usedLoci().length);
//		for (int i : usedLoci())
//			System.err.print(" "+i);
//		System.err.println();
	}

// Private data.

	private Variable[] loc = null;
	private Map<Variable,Integer> map = null;
	private ParameterData par = null;

	private void setLoci(Variable[] l)
	{
		loc = l;
		map = new LinkedHashMap<Variable,Integer>();
		for (int i=0; i<loc.length; i++)
			map.put(loc[i],i);
	}

	private int index(Variable v)
	{
		return map.get(v).intValue();
	}
}
