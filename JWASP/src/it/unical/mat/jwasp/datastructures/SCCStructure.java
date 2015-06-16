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

import java.util.HashSet;
import java.util.Set;

import org.sat4j.core.Vec;
import org.sat4j.core.VecInt;

public class SCCStructure {
	private SCComponent component;

	private Integer sourcePointer;
	private VecInt supporting;
	private VecInt supportedByThis[];
	private VecInt canBeSupportedByThis[];
	private Vec<Vec<SCComponent>> componentsToNotify;
	private Vec<Set<SCComponent>> addedComponents;

	private boolean founded;
	private boolean inQueue;
	private boolean inUnfoundedSet;
	private boolean aux;

	private void init() {
		founded = true;
		inQueue = false;
		inUnfoundedSet = false;
		aux = false;
		sourcePointer = 0;
		supportedByThis = new VecInt[2];
		supportedByThis[0] = new VecInt();
		supportedByThis[1] = new VecInt();
		canBeSupportedByThis = new VecInt[2];
		canBeSupportedByThis[0] = new VecInt();
		canBeSupportedByThis[1] = new VecInt();
		supporting = new VecInt();
		componentsToNotify = new Vec<Vec<SCComponent>>();
		componentsToNotify.push(new Vec<SCComponent>());
		componentsToNotify.push(new Vec<SCComponent>());
		addedComponents = new Vec<Set<SCComponent>>();
		addedComponents.push(new HashSet<SCComponent>());
		addedComponents.push(new HashSet<SCComponent>());
	}

	public SCCStructure() {
		init();
	}
	
	public boolean foundSourcePointerAuxAtom() {
		assert(sourcePointer > 0);
		sourcePointer--;		
		return sourcePointer == 0;
	}
	
	public void removedSourcePointerAuxAtom() {
		sourcePointer++;
	}
	
	public void clearSourcePointerAuxAtom() {
		sourcePointer = 0;
	}
	
	public SCComponent getComponent() {
		return component;
	}

	public void setComponent(SCComponent component) {
		this.component = component;
	}

	public Integer getSourcePointer() {
		return sourcePointer;
	}
	
	public void setSourcePointer(Integer sourcePointer) {
		this.sourcePointer = sourcePointer;
	}
	
	public void setAux(boolean aux) {
		this.aux = aux;
	}
	
	public boolean isAux() {
		return aux;
	}
	
	public void setFounded(boolean founded) {
		this.founded = founded;
	}
	
	public boolean isFounded() {
		return founded;
	}
	
	public void setInQueue(boolean inQueue) {
		this.inQueue = inQueue;
	}
	
	public boolean isInQueue() {
		return inQueue;
	}
	
	public void setInUnfoundedSet(boolean inUnfoundedSet) {
		this.inUnfoundedSet = inUnfoundedSet;
	}
	
	public boolean isInUnfoundedSet() {
		return inUnfoundedSet;
	}

	public VecInt getSupporting() {
		return supporting;
	}

	public void addSupporting(int lit) {
		supporting.push(lit);
	}
	
	private int getIndex(int lit) {
		return lit < 0 ? 0 : 1;
	}

	public VecInt getSupportedByThis(int lit) {		
		return supportedByThis[getIndex(lit)];
	}

	public void addSupportedByThis(int lit, int atomThatIsSupported) {
		supportedByThis[getIndex(lit)].push(atomThatIsSupported);		
	}

	public VecInt getCanBeSupportedByThis(int lit) {
		return canBeSupportedByThis[getIndex(lit)];
	}

	public void addCanBeSupportedByThis(int lit, int atomThatCanBeSupported) {
		canBeSupportedByThis[getIndex(lit)].push(atomThatCanBeSupported);
	}
	
	public Vec<SCComponent> getComponentsToNotify(int lit) {
		return componentsToNotify.get(getIndex(lit));
	}
	
	public void addComponentToNotify(int lit, SCComponent sc) {
		if(addedComponents.get(getIndex(lit)).add(sc))
			componentsToNotify.get(getIndex(lit)).push(sc);
	}
}
