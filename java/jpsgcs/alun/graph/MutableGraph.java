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

/**
	This interface describes the operations that can be carried out
	on a graph structure that allows changes.
*/

public interface MutableGraph<V,E> extends Graph<V,E>
{
/**
	Removes all vertices and edges from the graph.
*/
	public void clear();

/**
	Ensures that the given vertex is in the graph. Returns true
	if the graph changed as a result of this call.
*/
	public boolean add(V v);
	
/**
	Removes the given vertex from the graph. Returns true if
	the graph changed as a result of the call.
*/
	public boolean remove(Object v);

/**
	Makes a connection between the given vertices in the graph.
	Returns true if the graph changed as a result of the call.
	Graphs that allow multiple connections between the same vertices
	and those that allow only a single connection, will interpret
	this command differently.
	Directed and undireced graphs will also interpret this command
	differently.
*/
	public boolean connect(V u, V v);
	
/**
	Removes a connection between the given vertices. Returns true
	if the graph changed as a result of this call. 
	For graphs that allow multiple connections between vertices,
	it is up to the implementation to decide whether to remove
	a single connection or all connections.
*/
	public boolean disconnect(Object u, Object v);

/**
	Makes a connection between the given vertices in the graph,
	and associates the given edge with this connection.
	Returns true if the graph changed as a result of the call.
	Graphs that allow multiple connections between the same vertices
	and those that allow only a single connection, will interpret
	this command differently.
*/
	public boolean connect(V u, V v, E e);
}
