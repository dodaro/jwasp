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
package it.unical.mat.jwasp.utils;

import com.beust.jcommander.Parameter;

public class Options {

    @Parameter(names = "-help", description = "Print this guide and exit", help = true)
    public static boolean help = false;

    @Parameter(names = "-n", description = "Number of answer sets to compute (0 for all answer sets)")
    public static Integer models = 1;

    @Parameter(names = "-file", description = "The input file in lparse format")
    public static String file = null;

    @Parameter(names = "-restarts", description = "Restart strategy: [armin, minisat, glucose]")
    public static String restarts = "glucose";

    @Parameter(names = "-deletion", description = "Deletion strategy: [glucose, memory]")
    public static String deletion = "glucose";

    @Parameter(names = "-simplifications", description = "Simplifications: [expensive, no, simple]")
    public static String simp = "expensive";
    
    @Parameter(names = "-output", description = "Output format: [wasp, competition]")
    public static String output = "wasp";
}
