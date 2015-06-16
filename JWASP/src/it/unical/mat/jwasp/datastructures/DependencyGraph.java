/**
 * *****************************************************************************
 * ASP4J: an ASP library for Java Copyright (C) 2015, Carmine Dodaro
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU Lesser General Public License Version 2.1 or later (the
 * "LGPL"), in which case the provisions of the LGPL are applicable instead of
 * those above. If you wish to allow use of your version of this file only under
 * the terms of the LGPL, and not to allow others to use your version of this
 * file under the terms of the EPL, indicate your decision by deleting the
 * provisions above and replace them with the notice and other provisions
 * required by the LGPL. If you do not delete the provisions above, a recipient
 * may use your version of this file under the terms of the EPL or the LGPL.
 *
 * Based on SAT4J (http://www.sat4j.org/).
 *
 ******************************************************************************
 */
package it.unical.mat.jwasp.datastructures;

import org.sat4j.core.Vec;
import org.sat4j.core.VecInt;

public class DependencyGraph {

	private static DependencyGraph instance = null;
	private Vec<VecInt> scc;
	private Graph graph;

	private DependencyGraph() {
		graph = new Graph();
	}

	public static DependencyGraph getInstance() {
		if (instance == null) {
			instance = new DependencyGraph();
		}
		return instance;
	}

	public void addEdge(Integer v1, Integer v2) {
		if (!graph.containsVertex(v1)) {
			graph.addVertex(v1);
		}
		if (!graph.containsVertex(v2)) {
			graph.addVertex(v2);
		}
		graph.addEdge(v1, v2);
	}
	
	public void removeEdgesOfVertex(Integer v1){
		if(graph.containsVertex(v1))
			graph.removeAllEdges(v1);
	}

	public Vec<VecInt> computeSCC() {
		scc = graph.computeSCComponents();
		Vec<VecInt> components = new Vec<VecInt>(scc.size());
		for(int i = 0; i < scc.size(); i++) {
			VecInt component = scc.get(i);
			if (component.size() > 1) {
				components.push(component);
			}
		}
		return components;
	}
}
