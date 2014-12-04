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


package jpsgcs.alun.graph;

import java.util.Map;
import java.util.IdentityHashMap;

/** 
 This implementation of a Network uses an IdentityHashMap and 
 a Set backed by IdentityHashMap to get a graph implementation
 that uses identity rather than equality to manage its elements.
 This is useful, for example, when the vertices of a graph are
 themselves Sets. Using this implemntation lets you change the 
 contents of the Sets without disturbing their relationship
 to eachother in the graph.
*/
public class IdentityNetwork<V,E> extends Network<V,E>
{
/**
 Creates a new, empty undirected IdentityNetwork.
*/
	public IdentityNetwork()
	{
		this(false);
	}

/**
 Creates an empty directed or undirected IdentityNetwork as specified
 by the given boolean.
*/
	public IdentityNetwork(boolean directed)
	{
		super(directed,true);
	}
}
