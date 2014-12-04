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

import java.util.Set;
import java.util.Collection;

/** 
	This interface describes the queries that can reliably be run
	on an object that can be interpreted as a graph. 
	The vertices will be of class V and the 
	edges of class E. Note that, although it is possible to associate
	a particular edge object with a connection, it is not necessary to
	do so. In this case connects(x,y) will return true, but connection(x,y)
	will return null.
*/

public interface Graph<V,E>
{
/**
	Returns true if the given object is a vertex of the graph.
*/
	public boolean contains(Object x);

/**
	Returns true if the two given objects are connected vertices
	of the graph.
*/
	public boolean connects(Object x, Object y);
	
/**
	Returns the edge object that is associated with the connection
	between the given vertex objects. Returns null if there is no
	connection, or if there is no specific object associated with
	the connection.
	Graphs that allow for multiple connections between vertices
	will have to intrepret how to implement this.
*/
	public E connection(Object u, Object v);

/**
	Returns the set of objects associated with the connections from
	the given object.
*/
	public Collection<E> connections(Object u);

/**
	Returns the set of vertices of the graph.
*/
	public Set<V> getVertices();

/**
	Returns the collection of objects adjacent to the given one.
	Returns null if the object is not in the graph.
	If the graph allows parallel edges, the same vertex may
	appear multiple times in the collection.
*/
	public Collection<V> getNeighbours(Object x);

/**
	Returns true if the graph is directed, false if undirected.
*/
	public boolean isDirected();

/**
	In a directed graph this returns the collection of objects connected 
	to the given one by inward pointing connections. In an undirected
	graph it should be the same as getNeighbours().
*/
	public Collection<V> inNeighbours(Object x);
	
/**
	In a directed graph this returns the collection of objects connected 
	to the given one by outward pointing connections. In an undirected
	graph it should be the same as getNeighbours().
*/
	public Collection<V> outNeighbours(Object x);
}
