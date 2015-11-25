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
package it.unical.mat.jwasp.solver;

import it.unical.mat.jwasp.datastructures.Aggregate;
import it.unical.mat.jwasp.datastructures.DependencyGraph;
import it.unical.mat.jwasp.datastructures.Rule;
import it.unical.mat.jwasp.datastructures.SCCStructure;
import it.unical.mat.jwasp.datastructures.SCComponent;
import it.unical.mat.jwasp.utils.Options;
import it.unical.mat.jwasp.utils.Util;

import org.sat4j.core.Vec;
import org.sat4j.core.VecInt;
import org.sat4j.specs.ContradictionException;

public class ProgramBuilder {

	private Vec<Rule> rules;
	private Vec<Aggregate> aggregates;
	private Vec<VecInt> supportingRules;
	private int numberOfVars;
	private int numberOfVarsNoAux;
	private ASPSolver solver;
	private DependencyGraph dependencyGraph;
	private int numberOfLevels;

	public ProgramBuilder(ASPSolver solver) {
		rules = new Vec<Rule>();
		aggregates = new Vec<Aggregate>();
		supportingRules = new Vec<VecInt>(numberOfVars + 1);
		numberOfVars = 0;
		this.solver = solver;
		this.dependencyGraph = DependencyGraph.getInstance();
		this.numberOfLevels = 0;
	}

	public void addVariable(int var) {
		if (var > numberOfVars) {
			numberOfVars = var;
			while (supportingRules.size() <= numberOfVars) {
				supportingRules.push(new VecInt());
			}
			solver.newVar(numberOfVars);
		}
	}

	// Be careful: expensive method
	public void addRuleAndVariables(Rule r) throws ContradictionException {
		int max = 0;
		VecInt body = r.getBody();
		for (int i = 0; i < body.size(); i++) {
			int var = Math.abs(body.get(i));
			if (var > max) {
				max = var;
			}
		}
		VecInt head = r.getHead();
		for (int i = 0; i < head.size(); i++) {
			int var = head.get(i);
			if (var > max) {
				max = var;
			}
		}
		addVariable(max);
		addRule(r);
	}

	public void addRule(Rule r) throws ContradictionException {
		if (r.isConstraint()) {
			solver.addClause(r.toClause());
			return;
		}

		rules.push(r);
		addedRule(r);
	}

	public void addAggregate(Aggregate a) {
		aggregates.push(a);
		setNoNeedOfSupport(a.getId());
	}

	public void addTrueLiteral(int lit) throws ContradictionException {
		solver.addUnaryClause(lit);
	}

	public void finalizeProgram() throws ContradictionException {
		solver.newVar(numberOfVars);
		numberOfVarsNoAux = numberOfVars;
		shiftRules();
		computeClausesAndAddAuxAtoms();
		computeCompletion();
		addAggregates();
		computeSCCs();
	}

	private void addEdgeInDependencyGraph(int v1, int v2) {
		VecInt supp = supportingRules.get(v1);
		if (!supp.isEmpty() && (supp.get(0) == Integer.MAX_VALUE || supp.get(0) == 0))
			return;
		dependencyGraph.addEdge(v1, v2);
	}

	private void addedRule(Rule r) {
		if (r.isBodyEmpty() || r.isConstraint())
			return;

		VecInt head = r.getHead();
		VecInt body = r.getBody();

		for (int i = 0; i < head.size(); i++) {
			int headAtom = head.get(i);
			for (int j = 0; j < body.size(); j++) {
				int bodyLiteral = body.get(j);
				if (bodyLiteral < 0)
					continue;
				addEdgeInDependencyGraph(bodyLiteral, headAtom);
			}
		}
	}

	private void shiftRules() throws ContradictionException {
		int originalSize = rules.size();
		for (int i = 0; i < originalSize; i++) {
			Rule r = rules.get(i);
			if (r.isChoiceRule()) {
				VecInt head = r.getHead();
				if (r.isBodyEmpty()) {
					setNoNeedOfSupport(head);
				} else {
					int newId = addAuxForBody(r.getBody());
					for (int j = 0; j < head.size(); j++) {
						VecInt body = new VecInt(1);
						body.push(newId);
						addSupportingLiteralForHeadAtom(head.get(j), newId);
					}
				}
				r.remove();
				continue;
			}

			if (!r.isDisjunctive())
				continue;

			VecInt head = r.getHead();
			for (int j = 0; j < head.size(); j++) {
				VecInt body = new VecInt();
				r.getBody().copyTo(body);
				for (int k = 0; k < head.size(); k++) {
					if (j == k)
						continue;
					body.push(-head.get(k));
				}
				createAndAddRule(head.get(j), body);
			}
			r.remove();
		}
	}

	private void addSupportingLiteralForHeadAtom(int headAtom, int lit) {
		if (lit == 0) {
			supportingRules.get(headAtom).clear();
		}
		supportingRules.get(headAtom).push(lit);
	}

	private void setNoNeedOfSupport(int id) {
		supportingRules.get(id).clear();
		supportingRules.get(id).push(Integer.MAX_VALUE);
	}

	private void setNoNeedOfSupport(VecInt atoms) {
		for (int i = 0; i < atoms.size(); i++)
			setNoNeedOfSupport(atoms.get(i));
	}

	private void computeClausesAndAddAuxAtoms() throws ContradictionException {
		for (int i = 0; i < rules.size(); i++) {
			Rule r = rules.get(i);
			if (r.isRemoved())
				continue;
			assert (!r.isConstraint());
			assert (!r.isDisjunctive());
			assert (!r.isChoiceRule());
			int headAtom = r.getHead().get(0);
			if (r.isBodyEmpty()) {
				solver.addClause(r.toClause());
				addSupportingLiteralForHeadAtom(headAtom, 0);
				continue;
			}

			int body = addAuxForBody(r.getBody());
			if (body > 0)
				addEdgeInDependencyGraph(body, headAtom);
			addSupportingLiteralForHeadAtom(headAtom, body);
			r.getBody().clear();
			r.getBody().push(body);
			solver.addClause(r.toClause());
		}
	}

	private int addAuxForBody(VecInt body) throws ContradictionException {
		assert (!body.isEmpty());

		if (body.size() == 1)
			return body.get(0);

		int id = solver.nextFreeVarId(true);
		addVariable(id);
		VecInt clause = new VecInt(body.size() + 1);
		clause.push(id);
		for (int i = 0; i < body.size(); i++) {
			int l = body.get(i);
			clause.push(-l);
			VecInt binary = new VecInt(2);
			binary.push(-id);
			binary.push(l);
			solver.addClause(binary);
			if (l > 0) {
				addEdgeInDependencyGraph(l, id);
				addSupportingLiteralForHeadAtom(id, l);
			}
		}
		solver.addClause(clause);
		return id;
	}

	private void computeCompletion() throws ContradictionException {
		for (int i = 2; i <= numberOfVarsNoAux; i++) {
			VecInt supp = supportingRules.get(i);
			if (supp == null || supp.isEmpty()) {
				solver.addUnaryClause(-i);
				continue;
			}

			if (supp.get(0) == Integer.MAX_VALUE)
				continue;

			if (supp.get(0) == 0) {
				solver.addUnaryClause(i);
				continue;
			}

			supp.push(-i);
			solver.addClause(supp);
		}
	}

	private void computeSCCs() {
		Vec<VecInt> comps = dependencyGraph.computeSCC();
		for (int i = 0; i < comps.size(); i++) {
			SCComponent component = new SCComponent(i + 1, solver, comps.get(i));
			VecInt internalAtoms = component.getInternalAtoms();
			int t = 0;
			for (int j = 0; j < internalAtoms.size(); j++) {
				int atom = internalAtoms.get(j);
				internalAtoms.set(t, atom);
				SCCStructure atomStructure = solver.getStructureForAtom(atom);
				VecInt suppRules = supportingRules.get(atom);
				if (!suppRules.isEmpty() && (suppRules.get(0) == Integer.MAX_VALUE || suppRules.get(0) == 0))
					continue;
				else
					t++;
				int size = suppRules.size();
				boolean isAux = atom > this.numberOfVarsNoAux;
				if (isAux) {
					atomStructure.setAux(true);
					for (int k = 0; k < size; k++) {
						int lit = suppRules.get(k);
						if (lit < 0)
							continue;
						SCCStructure literalStructure = solver.getStructureForAtom(lit);

						if (literalStructure.getComponent() != component)
							continue;
						literalStructure.addCanBeSupportedByThis(lit, atom);
						literalStructure.addSupportedByThis(lit, atom);
						literalStructure.addComponentToNotify(-lit, component);
						atomStructure.addSupporting(lit);
					}
				} else {
					// Last position is the same atom
					size--;
					for (int k = 0; k < size; k++) {
						int lit = suppRules.get(k);
						SCCStructure literalStructure = solver.getStructureForAtom(lit);
						literalStructure.addCanBeSupportedByThis(lit, atom);
						literalStructure.addComponentToNotify(-lit, component);
						atomStructure.addSupporting(lit);
					}
				}
			}
			internalAtoms.shrinkTo(t);
			solver.addComponent(component);
		}
	}

	private void addAggregates() throws ContradictionException {
		for (int i = 0; i < aggregates.size(); i++) {
			Aggregate aggr = aggregates.get(i);
			solver.addAggregate(aggr);
			solver.addAggregate(aggr.getOpposite());
		}
	}

	private void createAndAddRule(int headAtom, VecInt body) {
		Rule newRule = new Rule();
		newRule.addAtomInHead(headAtom);
		newRule.setBody(body);
		rules.push(newRule);
	}

	public void addOptimizationRule(VecInt literals, Vec<Long> weights) {
		solver.addOptimizationLevel();
		for (int i = 0; i < literals.size(); i++) {
			solver.addOptimizationLiteral(literals.get(i), weights.get(i), numberOfLevels);
		}
		numberOfLevels++;
	}

	public void addCautiousCandidate(int var) {
		solver.addCautiousCandidate(var);
	}

	public void setVarName(int id, String name) {
		if (Options.cautiousAlgorithm != null)
			this.addCautiousCandidate(id);
		Util.setName(id, name);
	}
}
