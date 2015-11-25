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

public interface Constants {

    public static final int GRINGO_NORMAL_RULE_ID = 1;
    public static final int GRINGO_COUNT_CONSTRAINT_RULE_ID = 2;
    public static final int GRINGO_CHOICE_RULE_ID = 3;
    public static final int GRINGO_SUM_CONSTRAINT_RULE_ID = 5;
    public static final int GRINGO_OPTIMIZATION_RULE_ID = 6;
    public static final int GRINGO_DISJUNCTIVE_RULE_ID = 8;
    public static final int GRINGO_LINE_SEPARATOR = 0;
    public static final String GRINGO_BPLUS = "B+";
    public static final String GRINGO_BMINUS = "B-";
    public static final String JWASP = "JWASP 1.2.4";
    public static final String OPTIMUM = "OPTIMUM";
    public static final String ANSWER = "ANSWER";
    public static final String INCONSISTENT = "INCONSISTENT";
    public static final String INCOHERENT = "INCOHERENT";
    public static final String COST = "COST";
}
