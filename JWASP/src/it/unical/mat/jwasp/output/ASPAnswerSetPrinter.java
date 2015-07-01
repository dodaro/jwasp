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
package it.unical.mat.jwasp.output;

import java.math.BigInteger;

import org.sat4j.core.Vec;
import org.sat4j.core.VecInt;

import it.unical.mat.jwasp.solver.ASPSolver;
import it.unical.mat.jwasp.utils.Constants;
import it.unical.mat.jwasp.utils.Util;

public class ASPAnswerSetPrinter {

    public ASPAnswerSetPrinter() {
    }
    
    public void greetings() {
    	System.out.println(Constants.JWASP);
    }

    public void foundAnswerSet(int[] model) {
        StringBuilder modelString = new StringBuilder();
        for (int i = 0; i < model.length; i++) {
            if (model[i] > 0 && !Util.isHidden(model[i])) {
                modelString.append(Util.getName(model[i]));
                modelString.append(", ");
            }
        }

        if (modelString.length() > 2) {
            modelString.replace(modelString.length() - 2, modelString.length(), "");
        }

        System.out.println("{" + modelString.toString() + "}");
    }
    
    public void printCautiousConsequences(VecInt cautiousConsequences) {
    	StringBuilder modelString = new StringBuilder();
        for (int i = 0; i < cautiousConsequences.size(); i++) {
            modelString.append(Util.getName(cautiousConsequences.get(i)));
            modelString.append(", ");            
        }

        if (modelString.length() > 2) {
            modelString.replace(modelString.length() - 2, modelString.length(), "");
        }

        System.out.println("{" + modelString.toString() + "}");
	}    
    
    public void foundIncoherence() {
        System.out.println(Constants.INCOHERENT);
    }
    
    public void printNumberOfAnswerSets(int numberOfAnswerSets) {
    	System.out.println("Models: " + numberOfAnswerSets);
    }
    
    public void printCosts(Vec<BigInteger> costs) {
    	StringBuilder optimumCost = new StringBuilder();
    	int level = 1;
    	optimumCost.append(Constants.COST);
    	for(int i = costs.size() - 1; i >= 0; i--) {
    		optimumCost.append(" ");
    		optimumCost.append(costs.get(i));
    		optimumCost.append("@");
    		optimumCost.append(level);
    		level++;
    	}
    	System.out.println(optimumCost.toString());
    }
    
    public void foundOptimum() {
    	ASPSolver.EXIT_CODE = 30;
    	System.out.println(Constants.OPTIMUM);
    }
}
