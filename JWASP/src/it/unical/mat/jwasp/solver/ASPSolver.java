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
import it.unical.mat.jwasp.datastructures.SCCStructure;
import it.unical.mat.jwasp.datastructures.SCComponent;
import it.unical.mat.jwasp.output.ASPAnswerSetPrinter;
import it.unical.mat.jwasp.parser.ASPGringoParser;
import it.unical.mat.jwasp.parser.BadInputException;
import it.unical.mat.jwasp.utils.Constants;
import it.unical.mat.jwasp.utils.Options;
import it.unical.mat.jwasp.utils.Util;

import java.io.File;
import java.io.IOException;

import org.sat4j.core.Vec;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.core.Constr;
import org.sat4j.minisat.core.IOrder;
import org.sat4j.minisat.core.LearningStrategy;
import org.sat4j.minisat.core.RestartStrategy;
import org.sat4j.pb.core.PBDataStructureFactory;
import org.sat4j.pb.core.PBSolver;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.Lbool;
import org.sat4j.specs.TimeoutException;

public class ASPSolver extends PBSolver {
	public final static int TIMEOUT = 0;
	public final static int COHERENT = 1;
	public final static int INCOHERENT = 2;
	public static int EXIT_CODE = 0;

	private static final long serialVersionUID = 779695783693900331L;
	private ASPAnswerSetPrinter printer;
	private Vec<SCComponent> components;
	private Vec<SCCStructure> structures;

	public ASPSolver(LearningStrategy<PBDataStructureFactory> learner,
			PBDataStructureFactory dsf, IOrder order, RestartStrategy restarter) {
		super(learner, dsf, order, restarter);
		printer = new ASPAnswerSetPrinter();
		components = new Vec<SCComponent>();
		structures = new Vec<SCCStructure>();
	}

	public int solve() {
		try {
			System.out.println(Constants.ASP4J);
			return solve_() ? COHERENT : INCOHERENT;
		} catch (TimeoutException e) {
			System.err.println("Killed: Bye!");
			return TIMEOUT;
		}
	}

	private void initSCCStructures() throws ContradictionException {
		for (int i = 0; i < components.size(); i++) {
			components.get(i).init();
			VecInt clauseToPropagate = null;
			do {
				clauseToPropagate = components.get(i).computeUnfoundedSet();
				if (clauseToPropagate != null) {
					if (handleUnfoundedSet(clauseToPropagate) != null)
						throw new ContradictionException();
				}
			} while (clauseToPropagate != null);
		}
	}

	private boolean solve_() throws TimeoutException {
		if (!parse())
			return false;
		try {
			initSCCStructures();
		} catch (ContradictionException e) {
			printer.foundIncoherence();
			return false;
		}
		return this.enumerateAnswerSets(Options.models) > 0;
	}

	private boolean parse() {
		ProgramBuilder builder = new ProgramBuilder(this);
		ASPGringoParser parser = new ASPGringoParser(builder);
		try {
			if (Options.file == null)
				parser.parseFromStandardInput();
			else
				parser.parseFrom(new File(Options.file));
		} catch (ContradictionException e) {
			printer.foundIncoherence();
			return false;
		} catch (NumberFormatException e) {
			System.err.println("Error while reading the input.");
			return false;
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return false;
		} catch (BadInputException e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}

	private int enumerateAnswerSets(int maxNum) throws TimeoutException {
		int numberOfAnswerSets = 0;
		if (maxNum <= 0)
			maxNum = Integer.MAX_VALUE;

		while (numberOfAnswerSets < maxNum && isSatisfiable()) {
			numberOfAnswerSets++;
			int[] model = this.model();
			printer.foundAnswerSet(model);
			VecInt clause = Util.flipClause(model);
			try {
				this.addClause(clause);
			} catch (ContradictionException e) {
				break;
			}			
		}

		if (numberOfAnswerSets == 0)
			printer.foundIncoherence();

		printer.printNumberOfAnswerSets(numberOfAnswerSets);

		return numberOfAnswerSets;
	}

	public void addAggregate(Aggregate aggr) throws ContradictionException {
		addPseudoBoolean(aggr.getLits(), aggr.getWeights(), true,
				aggr.getBound());
	}

	public void addUnaryClause(int lit) throws ContradictionException {
		addClause(new VecInt().push(lit));
	}

	@Override
	public int newVar(int var) {
		while (var >= structures.size()) {
			structures.push(new SCCStructure());
		}

		return super.newVar(var);
	}

	public void addComponent(SCComponent component) {
		this.components.push(component);
	}

	public SCCStructure getStructureForAtom(int atom) {
		return this.structures.get(Math.abs(atom));
	}

	public void setComponentOfVariable(int var, SCComponent component) {
		this.structures.get(var).setComponent(component);
	}

	public boolean sameComponent(int var1, int var2) {
		return this.structures.get(var1).getComponent() == this.structures.get(
				var2).getComponent();
	}

	public boolean isVariableInComponent(int var, SCComponent component) {
		return this.structures.get(var).getComponent() == component;
	}

	public boolean isTrue(int lit) {
		return truthValue(lit) == Lbool.TRUE;
	}

	public boolean isFalse(int lit) {
		return truthValue(lit) == Lbool.FALSE;
	}

	public boolean isUndefined(int lit) {
		return truthValue(lit) == Lbool.UNDEFINED;
	}

	@Override
	public void foundConflict() {
		for (int i = 0; i < propagatorsToVisit.size(); i++)
			propagatorsToVisit.get(i).clear();
		propagatorsToVisit.clear();
	}

	@Override
	protected void morePropagation(int p) {
		Vec<SCComponent> components = getStructureForAtom(p)
				.getComponentsToNotify(p);
		for (int i = 0; i < components.size(); i++) {
			SCComponent s = components.get(i);
			if (s.literalIsFalse(-p)) {
				propagatorsToVisit.push(s);
				addedPropagators.add(s);
			}
		}
	}

	@Override
	protected Constr postPropagation() {
		while (!propagatorsToVisit.isEmpty()) {
			SCComponent component = (SCComponent) propagatorsToVisit.last();
			VecInt clauseToPropagate = component.computeUnfoundedSet();			
			if (clauseToPropagate == null) {
				addedPropagators.remove(component);
				propagatorsToVisit.pop();
			} else {
				return handleUnfoundedSet(clauseToPropagate);
			}
		}
		return null;
	}

	private Constr handleUnfoundedSet(VecInt clauseToPropagate) {
		Constr constraint = this.dsfactory
				.createUnregisteredClause(dimacs2internal(clauseToPropagate));		
		int lit = clauseToPropagate.get(0);
		if (isFalse(lit))
			return constraint;

		if (clauseToPropagate.size() > 1)
			dsfactory.learnConstraint(constraint);
		constraint.assertConstraint(this);
		return null;		 
	}

	public void setPrinter(ASPAnswerSetPrinter printer) {
		this.printer = printer;
	}
}