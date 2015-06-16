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

import it.unical.mat.jwasp.solver.ASPSolver;

import java.util.HashSet;
import java.util.Set;

import org.sat4j.core.VecInt;

public class SCComponent extends Propagator{

	public int id;
	private VecInt internalAtoms;
	private ASPSolver solver;
	private VecInt unfoundedSet;
	private VecInt atomsWithoutSourcePointer;
	int suca = 0;

	private VecInt falseLiterals;
	private VecInt reason;

	public SCComponent(int id, ASPSolver solver, VecInt internalAtoms) {
		this.id = id;
		this.solver = solver;
		this.unfoundedSet = new VecInt();
		this.atomsWithoutSourcePointer = new VecInt(internalAtoms.size());
		this.falseLiterals = new VecInt();
		this.internalAtoms = internalAtoms;
		for (int i = 0; i < internalAtoms.size(); i++) {			
			this.solver.setComponentOfVariable(internalAtoms.get(i), this);
		}
	}

	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("[");
		for (int i = 0; i < internalAtoms.size(); i++) {
			strBuilder.append(" ");
			strBuilder.append(internalAtoms.get(i));
		}
		strBuilder.append(" ]");
		return strBuilder.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof SCComponent)
			return this.id == ((SCComponent) o).id;
		return false;
	}

	public boolean equals(SCComponent c) {
		return this.id == c.id;
	}

	public VecInt getInternalAtoms() {
		return internalAtoms;
	}

	public void init() {
		for (int i = 0; i < internalAtoms.size(); i++) {
			int atom = internalAtoms.get(i);
			if (!solver.getStructureForAtom(atom).isAux())
				addAtomWithoutSourcePointer(atom);
		}		
	}

	private void addAtomWithoutSourcePointer(int atom) {
		if (!solver.getStructureForAtom(atom).isInQueue()
				&& !solver.isFalse(atom)) {
			solver.getStructureForAtom(atom).setFounded(false);
			atomsWithoutSourcePointer.push(atom);
			solver.getStructureForAtom(atom).setInQueue(true);
		}
	}

	@Override
	public void clear() {
		falseLiterals.clear();		
		unfoundedSet.clear();
		reason = null;
	}

	public boolean literalIsFalse(int literal) {
		boolean res = false;
		VecInt supportedByThis = solver.getStructureForAtom(literal)
				.getSupportedByThis(literal);
		for (int i = 0; i < supportedByThis.size(); i++) {
			int current = supportedByThis.get(i);
			if (isAux(current) || solver.isFalse(current) || !solver.isVariableInComponent(current, this))
				continue;
			res = true;
		}
		if (res)
			falseLiterals.push(literal);		
		return res;
	}

	private void computeAtomsWithoutSourcePointer() {
		for (int i = 0; i < falseLiterals.size(); i++) {
			int literal = falseLiterals.get(i);
			VecInt supportedByThis = solver.getStructureForAtom(literal)
					.getSupportedByThis(literal);
			int k = 0;
			for (int j = 0; j < supportedByThis.size(); j++) {
				int current = supportedByThis.get(j);
				supportedByThis.set(k, current);
				if (isAux(current) || solver.isFalse(current) || !solver.isVariableInComponent(current, this)) {
					k++;
					continue;					
				}
				addAtomWithoutSourcePointer(current);
			}
			supportedByThis.shrinkTo(k);
		}
		falseLiterals.clear();
	}

	private void atomLostSourcePointer(int atom) {
		VecInt supportedByThis = solver.getStructureForAtom(atom)
				.getSupportedByThis(atom);
		int k = 0;
		for (int i = 0; i < supportedByThis.size(); i++) {
			int current = supportedByThis.get(i);
			supportedByThis.set(k, current);
			if(solver.isFalse(current) || !solver.isVariableInComponent(current, this)) {
				k++;
				continue;
			}
			if (isAux(current)) {
				k++;
				removeSourcePointerAuxAtom(current);
			}
			addAtomWithoutSourcePointer(current);
		}
		supportedByThis.shrinkTo(k);
	}

	private boolean isSupporting(int literal) {
		// If literal is false cannot be supporting
		if (solver.isFalse(literal))
			return false;

		// External literal
		if (isExternal(literal))
			return true;

		// Internal literal
		return isFounded(literal);
	}

	private boolean isExternal(int literal) {
		return literal < 0
				|| solver.getStructureForAtom(literal).getComponent() != this;
	}

	private void setSourcePointer(int atom, int literal) {
		if (literal != 0)
			solver.getStructureForAtom(literal).addSupportedByThis(literal,
					atom);
		solver.getStructureForAtom(atom).setSourcePointer(literal);
		solver.getStructureForAtom(atom).setFounded(true);
	}

	private boolean tryToFindASourcePointer(int atom) {
		assert !isFounded(atom);
		SCCStructure structure = solver.getStructureForAtom(atom);
		// aux atoms are ignored
		if (structure.isAux())
			return false;

		VecInt supporting = structure.getSupporting();
		for (int i = 0; i < supporting.size(); i++) {			
			int current = supporting.get(i);
			if (isSupporting(current)) {
				setSourcePointer(atom, current);
				return true;
			}
		}
		return false;
	}

	private boolean isFounded(int atom) {
		return solver.getStructureForAtom(atom).isFounded();
	}

	private boolean isAux(int atom) {
		return solver.getStructureForAtom(atom).isAux();
	}

	private boolean foundSourcePointerAuxAtom(int auxAtom) {
		assert isAux(auxAtom);
		return solver.getStructureForAtom(auxAtom).foundSourcePointerAuxAtom();
	}

	private void removeSourcePointerAuxAtom(int auxAtom) {
		assert isAux(auxAtom);
		solver.getStructureForAtom(auxAtom).removedSourcePointerAuxAtom();
	}

	private void propagateSourcePointer(int atom) {
		VecInt canBeSupp = solver.getStructureForAtom(atom)
				.getCanBeSupportedByThis(atom);
		for (int i = 0; i < canBeSupp.size(); i++) {
			int current = canBeSupp.get(i);
			if (isFounded(current))
				continue;

			int sp = atom;
			if (isAux(current)) {
				if (foundSourcePointerAuxAtom(current))
					sp = 0;
				else
					continue;
			}
			setSourcePointer(current, sp);
			propagateSourcePointer(current);
		}
	}

	private void clearAtomsWithoutSourcePointers() {
		for (int i = 0; i < atomsWithoutSourcePointer.size(); i++) {
			int atom = atomsWithoutSourcePointer.get(i);
			SCCStructure structure = solver.getStructureForAtom(atom);			
			if(structure.isAux()) {				
				setSourcePointer(atom, 0);				
			}
			else if(!structure.isFounded()) {
				setSourcePointer(atom, structure.getSourcePointer());
			}
			structure.setFounded(true);
			structure.setInQueue(false);
		}
		atomsWithoutSourcePointer.clear();
	}
	
	private VecInt createClause(int atom) {
		VecInt clause = new VecInt(reason.size()+1);
		clause.push(-atom);
		for(int i = 0; i < reason.size(); i++)
			clause.push(reason.get(i));		
		return clause;
	}
	
	private VecInt getNextClauseToPropagate() {
		while (!unfoundedSet.isEmpty()) {
			int atom = unfoundedSet.last();
			unfoundedSet.pop();
			if(solver.isFalse(atom))
				continue;			
			return createClause(atom);
		}
		return null;
	}
	
	private void computeReason() {
		reason = new VecInt();
		Set<Integer> added = new HashSet<Integer>();
		for (int i = 0; i < unfoundedSet.size(); i++) {
			int atom = unfoundedSet.get(i);
			SCCStructure structure = solver.getStructureForAtom(atom);
			VecInt supporting = structure.getSupporting();
			for (int j = 0; j < supporting.size(); j++) {
				int literal = supporting.get(j);
				if (solver.isFalse(literal)) {
					if (added.add(literal))
						reason.push(literal);
				}
			}
		}		
	}

	public VecInt computeUnfoundedSet() {		
		VecInt clauseToPropagate = getNextClauseToPropagate();
		if(clauseToPropagate!=null)
			return clauseToPropagate;
		
		computeAtomsWithoutSourcePointer();
		for (int i = 0; i < atomsWithoutSourcePointer.size(); i++)
			atomLostSourcePointer(atomsWithoutSourcePointer.get(i));		

		for (int i = 0; i < atomsWithoutSourcePointer.size(); i++) {
			int atom = atomsWithoutSourcePointer.get(i);
			if (isFounded(atom))
				continue;
			boolean result = tryToFindASourcePointer(atom);
			if (result)
				propagateSourcePointer(atom);
			else
				unfoundedSet.push(atom);
		}

		int j = 0;
		for (int i = 0; i < unfoundedSet.size(); i++) {
			int elem = unfoundedSet.get(i);
			unfoundedSet.set(j, elem);
			if (!isFounded(elem))
				j++;
		}
		unfoundedSet.shrinkTo(j);
		clearAtomsWithoutSourcePointers();
		if (unfoundedSet.isEmpty())
			return null;
		computeReason();
		clauseToPropagate = getNextClauseToPropagate();
		return clauseToPropagate;
	}	
}
