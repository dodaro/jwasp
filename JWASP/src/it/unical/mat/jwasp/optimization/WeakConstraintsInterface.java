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
package it.unical.mat.jwasp.optimization;

import it.unical.mat.jwasp.solver.ASPSolver;

import java.math.BigInteger;

import org.sat4j.core.Vec;
import org.sat4j.core.VecInt;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IVecInt;
import org.sat4j.specs.TimeoutException;

public class WeakConstraintsInterface {

	private BigInteger lb;
	private ASPSolver solver;
	private Vec<BigInteger> costs;
	public WeakConstraintsInterface(ASPSolver solver) {
		this.solver=solver;
		lb = BigInteger.valueOf(0);
		costs = new Vec<BigInteger>();
		for(int i = 0; i < solver.getNumberOfLevels(); i++)
			costs.push(BigInteger.ZERO);
	}
	
	public int solve() throws TimeoutException {
		if(!solver.isSatisfiable(true)) {
			solver.foundIncoherence();
			return ASPSolver.INCOHERENT;
		}
		
		int level = solver.getNumberOfLevels() - 1;
		while(level >= 0) {
			lb = BigInteger.ZERO;								
			boolean res;
			while(true) {
				VecInt assumptions = computeAssumptions(level);
				res = solver.isSatisfiable(assumptions, true);
				if(res) {
					costs.set(level, lb);					
					foundSat(level);
					break;
				}
				foundUnsat(level);
			}
			if(!hardening(level))
				return ASPSolver.OPTIMUM_FOUND;
			
			level--;
		}
		return ASPSolver.OPTIMUM_FOUND;
	}
	
	private void foundSat(int level) {
		solver.printAnswerSet(solver.model());
		printCosts(level);
	}
	
	private void foundUnsat(int level) {
		IVecInt unsatCore = solver.unsatExplanation();
		BigInteger minWeight = BigInteger.valueOf(Long.MAX_VALUE);
		for(int i = 0; i < unsatCore.size(); i++) {
			int literal = -unsatCore.get(i);
			BigInteger weight = solver.getOptimizationOfLiteral(literal,level).getWeight();
			minWeight = minWeight.min(weight);
		}
		lb = lb.add(minWeight);
		
		VecInt literals = new VecInt(unsatCore.size());		
		for(int i = 0; i < unsatCore.size(); i++) {
			int literal = -unsatCore.get(i);
			literals.push(-literal);
			BigInteger weight = solver.getOptimizationOfLiteral(literal,level).getWeight();
			if(weight.equals(minWeight))
				solver.removeOptimizationLiteral(literal, level);
			else
				solver.getOptimizationOfLiteral(literal,level).setWeight(weight.subtract(minWeight));			
		}
		
		int n = literals.size(); 
		int nVars = solver.nVars();
		solver.newVar(nVars + n);
		
		for(int i = 0; i < n-1; i++) {
			int id = nVars+i+1;
			literals.push(-id);
			solver.addOptimizationLiteral(-id, minWeight.longValue(), level);			
		}
		
		for( int i = 0; i < n-2; i++ )
	    {
			int id = nVars+i+1;
	        VecInt clause = new VecInt();
	        clause.push(-id);
	        clause.push(id+1);
	        try {
				solver.addClause(clause);
			} catch (ContradictionException e) {
				e.printStackTrace();
			}        
	    }
		
		try {
			solver.addAtLeast(literals, n-1);
		} catch (ContradictionException e) {
			e.printStackTrace();
		}
	}
	
	private void printCosts(int level) {
		Vec<BigInteger> costsToPrint = new Vec<BigInteger>();
		costs.copyTo(costsToPrint);
		for(int i = level - 1; i >= 0; i--) {
			costsToPrint.set(i, computeCostOfLevel(i));
		}
		solver.printCosts(costsToPrint);
	}
	
	private BigInteger computeCostOfLevel(int level) {
		Vec<OptimizationLiteral> optLiterals = solver.getOptimizationLiterals(level);
		BigInteger value = BigInteger.valueOf(0);
		for(int i = 0; i < optLiterals.size(); i++) {			
			int lit = optLiterals.get(i).getLiteral();
			if(solver.isTrueInModel(lit))
				value.add(solver.getOptimizationOfLiteral(lit,level).getWeight());
		}		
		return value;
	}
	
	private boolean hardening(int level) {
		Vec<OptimizationLiteral> optLiterals = solver.getOptimizationLiterals(level);
		for(int i = 0; i < optLiterals.size(); i++) {
			int lit = optLiterals.get(i).getLiteral();
			try {
				solver.addUnaryClause(-lit);
			} catch (ContradictionException e) {
				return false;
			}
		}
		return true;
	}
	
	private VecInt computeAssumptions(int level) {
		Vec<OptimizationLiteral> optLiterals = solver.getOptimizationLiterals(level);
		VecInt assumptions = new VecInt(optLiterals.size());
		for(int i = 0; i < optLiterals.size(); i++) {
			assumptions.push(-optLiterals.get(i).getLiteral());
		}
		return assumptions;
	}
}
