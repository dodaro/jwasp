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
package it.unical.mat.jwasp;

import com.beust.jcommander.JCommander;

import it.unical.mat.jwasp.solver.ASPSolver;
import it.unical.mat.jwasp.solver.ASPSolverFactory;
import it.unical.mat.jwasp.utils.Options;

public class Main {

    public static void main(String[] args) {    	
        try {
            JCommander jc = new JCommander(new Options(), args);
            if (Options.help) {
                jc.usage();
                return;
            }
            ASPSolver solver = ASPSolverFactory.newSolver();
            solver.solve();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(ASPSolver.EXIT_CODE);
    }
}
