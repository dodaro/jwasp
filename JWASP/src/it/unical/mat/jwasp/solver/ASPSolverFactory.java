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

import it.unical.mat.jwasp.output.ASPAnswerSetPrinter;
import it.unical.mat.jwasp.output.CompetitionPrinter;
import it.unical.mat.jwasp.utils.Options;

import org.sat4j.minisat.core.SearchParams;
import org.sat4j.minisat.learning.MiniSATLearning;
import org.sat4j.minisat.orders.RSATPhaseSelectionStrategy;
import org.sat4j.minisat.orders.VarOrderHeap;
import org.sat4j.minisat.restarts.ArminRestarts;
import org.sat4j.minisat.restarts.Glucose21Restarts;
import org.sat4j.minisat.restarts.MiniSATRestarts;
import org.sat4j.pb.constraints.CompetResolutionPBLongMixedWLClauseCardConstrDataStructure;
import org.sat4j.pb.core.PBDataStructureFactory;

public class ASPSolverFactory {

	private ASPSolverFactory() {
	}

	private static ASPSolver newDefault() {
		MiniSATLearning<PBDataStructureFactory> learning = new MiniSATLearning<PBDataStructureFactory>();
		ASPSolver solver = new ASPSolver(
				learning,
				new CompetResolutionPBLongMixedWLClauseCardConstrDataStructure(),
				new VarOrderHeap(new RSATPhaseSelectionStrategy()),
				new Glucose21Restarts());
		solver.setSearchParams(new SearchParams(1.1, 100));
		solver.setSimplifier(solver.EXPENSIVE_SIMPLIFICATION);
		solver.setLearnedConstraintsDeletionStrategy(solver.glucose);		
		return solver;
	}

	private static void updateRestartStrategy(ASPSolver solver) {
		if (Options.restarts == null)
			return;
		if (Options.restarts.equalsIgnoreCase("glucose"))
			solver.setRestartStrategy(new Glucose21Restarts());
		else if (Options.restarts.equalsIgnoreCase("armin"))
			solver.setRestartStrategy(new ArminRestarts());
		else if (Options.restarts.equalsIgnoreCase("minisat"))
			solver.setRestartStrategy(new MiniSATRestarts());
	}

	private static void updateSimplification(ASPSolver solver) {
		if (Options.simp == null)
			return;
		if (Options.simp.equalsIgnoreCase("expensive"))
			solver.setSimplifier(solver.EXPENSIVE_SIMPLIFICATION);
		else if (Options.simp.equalsIgnoreCase("no"))
			solver.setSimplifier(ASPSolver.NO_SIMPLIFICATION);
		else if (Options.simp.equalsIgnoreCase("simple"))
			solver.setSimplifier(solver.SIMPLE_SIMPLIFICATION);
	}

	private static void updateDeletionStrategy(ASPSolver solver) {
		if (Options.deletion == null)
			return;		
		if (Options.deletion.equalsIgnoreCase("glucose"))
			solver.setLearnedConstraintsDeletionStrategy(solver.glucose);
		else if (Options.deletion.equalsIgnoreCase("memory"))
			solver.setLearnedConstraintsDeletionStrategy(solver.memory_based);		
	}
	
	private static void updateOutput(ASPSolver solver) {
		if(Options.output == null)
			return;
		if (Options.output.equalsIgnoreCase("competition"))
			solver.setPrinter(new CompetitionPrinter());
		else if (Options.output.equalsIgnoreCase("wasp"))
			solver.setPrinter(new ASPAnswerSetPrinter());
	}

	public static ASPSolver newSolver() {
		ASPSolver solver = newDefault();
		updateRestartStrategy(solver);
		updateDeletionStrategy(solver);
		updateSimplification(solver);
		updateOutput(solver);
		solver.setNumberOfAnswerSets(Options.models);
		return solver;
	}
}
