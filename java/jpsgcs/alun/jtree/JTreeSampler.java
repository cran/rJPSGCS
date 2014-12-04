/*
 Copyright 2011 Alun Thomas.

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


package jpsgcs.alun.jtree;

import jpsgcs.alun.hashing.RandomIdentitySet;
import jpsgcs.alun.util.Pair;

import java.util.Collection;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.IdentityHashMap;

abstract public class JTreeSampler<V>
{
	abstract public int randomConnection();
	abstract public int randomDisconnection();

	protected JTree<V> jt = null;
	protected CliqueScorer<V> cs = null;

	public JTreeSampler(JTree<V> jtree, CliqueScorer<V> scorer)
	{
		jt = jtree;
		cs = scorer;
	}

	protected double deltaScore(Clique<V> C, V x, V y)
	{
		if (cs == null)
			return 0;

		double score = -cs.score(C);
		C.remove(x);
		score += cs.score(C);
		C.remove(y);
		score -= cs.score(C);
		C.add(x);
		score += cs.score(C);
		C.add(y);
		return score;
	}

	protected double deltaScore(Clique<V> C, Set<V> x, Set<V> y)
	{
		if (cs == null)
			return 0;

		double score = -cs.score(C);
		C.removeAll(x);
		score += cs.score(C);
		C.removeAll(y);
		score -= cs.score(C);
		C.addAll(x);
		score += cs.score(C);
		C.addAll(y);
		return score;
	}

	protected double deltaScore(Separator<V> S, V x, V y)
	{
		if (cs == null)
			return 0;
		
		double score = cs.score(S);
		S.add(x);
		score -= cs.score(S);
		S.add(y);
		score += cs.score(S);
		S.remove(x);
		score -= cs.score(S);
		S.remove(y);
		return score;
	}

	protected double deltaScore(Separator<V> S, Set<V> x, Set<V> y)
	{
		if (cs == null)
			return 0;
		
		double score = cs.score(S);
		S.addAll(x);
		score -= cs.score(S);
		S.addAll(y);
		score += cs.score(S);
		S.removeAll(x);
		score -= cs.score(S);
		S.removeAll(y);
		return score;
	}

	protected double temperature()
	{
		return cs == null ? 1 : cs.temperature();
	}

	public void randomize()
	{
		randomize(jt);
	}

	public static <V> void randomize(JTree<V> jt)
	{
		if (jt.separators.size() == 0)
			return;
		for (Separator<V> s : jt.getDistinctSeparators())
			randomize(jt,s);
	}

	public static <V> void randomize(JTree<V> jt, Separator<V> s)
	{
		// Find the sub tree of vertices containing s.
		// and the list of separators with intersection s

		RandomIdentitySet<Clique<V>> Ts = new RandomIdentitySet<Clique<V>>();
		List<Separator<V>> sep = new ArrayList<Separator<V>>();
		jt.collect(Ts,sep,s.getX(),s);

		if (Ts.size() == 2)
			return;

		// Disconnect the equivalent separators.

		for (Separator<V> ss : sep)
			jt.disconnect(ss.getX(),ss.getY());

		// Find the components of the subtree linked by s.

		List<RandomIdentitySet<Clique<V>>> Fs = new ArrayList<RandomIdentitySet<Clique<V>>>();
		Set<Clique<V>> Tscopy = new LinkedHashSet<Clique<V>>(Ts);

		while (!Tscopy.isEmpty())
		{
			RandomIdentitySet<Clique<V>> fsi = new RandomIdentitySet<Clique<V>>();
			jt.collect(fsi,Tscopy.iterator().next(),s);
			Tscopy.removeAll(fsi);
			Fs.add(fsi);
		}

		// Draw, with replacement, n-2 components of Ts with probability proportional to their size.

		Map<Clique<V>,RandomIdentitySet<Clique<V>>> h = new IdentityHashMap<Clique<V>,RandomIdentitySet<Clique<V>>>();
		for (RandomIdentitySet<Clique<V>> f : Fs)
			for (Clique<V> x : f)
				h.put(x,f);

		List<RandomIdentitySet<Clique<V>>> v = new ArrayList<RandomIdentitySet<Clique<V>>>();
		for (int i=0; i<Fs.size() - 2; i++)
			v.add(h.get(Ts.next()));

		// Put these random list and the set of compoenents into a sorter that will 
		// deliver them in the appropriate order for the pruffer code.
		
		PruferSorter<RandomIdentitySet<Clique<V>>> ps = new PruferSorter<RandomIdentitySet<Clique<V>>>(v,Fs);

		while (ps.hasNext())
		{
			Pair<RandomIdentitySet<Clique<V>>,RandomIdentitySet<Clique<V>>> p = ps.next();
			Clique<V> x = p.getX().next();
			Clique<V> y = p.getY().next();

			Separator<V> ss = sep.remove(sep.size()-1);
			ss.setX(x);
			ss.setY(y);
			jt.connect(x,y,ss);
		}
	}
}
