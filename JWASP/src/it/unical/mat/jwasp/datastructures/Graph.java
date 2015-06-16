package it.unical.mat.jwasp.datastructures;

import org.sat4j.core.Vec;
import org.sat4j.core.VecInt;

public class Graph {
	private Vec<VecInt> edges;
	private VecInt vertices;
	private VecInt indices;
	private VecInt minDistances;
	private int index;
	private VecInt stack;
	private VecInt added;
	private Vec<VecInt> components;

	public Graph() {
		edges = new Vec<VecInt>();
		vertices = new VecInt();
		indices = new VecInt();
		minDistances = new VecInt();
		added = new VecInt();
		components = new Vec<VecInt>();
	}

	public void addVertex(int vertex) {
		while (vertex >= vertices.size()) {
			vertices.push(vertex);
			indices.push(-1);
			minDistances.push(-1);
			added.push(-1);
			edges.push(new VecInt());
		}
	}

	public void addEdge(int n1, int n2) {
		edges.get(n1).push(n2);
	}

	public boolean containsVertex(int v) {
		return v < vertices.size();
	}

	public void removeAllEdges(int v) {
		edges.get(v).clear();
	}

	public Vec<VecInt> computeSCComponents() {
		index = 0;
		stack = new VecInt();
		for (int i = 0; i < vertices.size(); i++) {
			int node = vertices.get(i);
			if (isUndefined(node))
				tarjan(node);
		}
		return components;
	}

	private boolean isUndefined(int node) {
		return indices.get(node) == -1;
	}

	private void tarjan(int node) {
		indices.set(node, index);
		minDistances.set(node, index);
		index++;
		stack.push(node);
		added.set(node, 0);
		VecInt edges_ = edges.get(node);
		for (int i = 0; i < edges_.size(); i++) {
			int v1 = edges_.get(i);
			if (isUndefined(v1)) {
				tarjan(v1);
				int min = Math
						.min(minDistances.get(node), minDistances.get(v1));
				minDistances.set(node, min);
			} else if (added.get(v1) == 0) {
				int min = Math.min(minDistances.get(node), indices.get(v1));
				minDistances.set(node, min);
			}
		}

		if (minDistances.get(node) == indices.get(node)) {
			VecInt component = new VecInt();
			int v1;
			do {
				v1 = stack.last();
				stack.pop();
				added.set(v1, -1);
				component.push(v1);
			} while (v1 != node);
			components.push(component);
		}
	}
}
