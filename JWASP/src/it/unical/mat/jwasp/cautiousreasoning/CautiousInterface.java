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
package it.unical.mat.jwasp.cautiousreasoning;

import it.unical.mat.jwasp.solver.ASPSolver;

import org.sat4j.core.VecInt;
import org.sat4j.specs.TimeoutException;

public class CautiousInterface {
	
	VecInt candidates;
	VecInt answers;
	ASPSolver solver;

	public CautiousInterface(VecInt candidates, ASPSolver solver) {
		this.candidates = candidates;
		this.solver = solver;
		this.answers = new VecInt(candidates.size());
	}
	
	public int computeCautiousConsequences() throws TimeoutException {
		if(!solver.isSatisfiable(true))
			return ASPSolver.INCOHERENT;
		return ict();
	}
	
	private void addAnswer(int v) {
		answers.push(v);
	}
	
	private void reduceCandidates() {
		int j = 0;		
	    for( int i = 0; i < candidates.size(); i++ )
	    {
	    	int v = candidates.get(i);
	    	candidates.set(j, v);
	        if( solver.model( v ) )
		        j++;
	    }
	    candidates.shrinkTo( j );
	}

	private int ict() throws TimeoutException {
		reduceCandidates();
		solver.unrollToZero();
		VecInt assumps = new VecInt(1);
		while( !candidates.isEmpty() )
	    {
	        int v = candidates.last();
	        if( !solver.isUndefined( v ) )
	        {
	            if( solver.model( v ) )
	                addAnswer( v );
	            
	            candidates.pop();
	            continue;
	        }
	        
	        assumps.push(-v);
	        boolean isSat = solver.isSatisfiable(assumps, true);
	        
	        candidates.pop();
	        if( isSat )
	            reduceCandidates();
	        else
	            addAnswer( v );
	        assumps.clear();	        
	    }
		solver.printCautiousConsequences(answers);
		return ASPSolver.COHERENT;
	}
}
