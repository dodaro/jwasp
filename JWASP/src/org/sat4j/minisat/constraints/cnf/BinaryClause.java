/*******************************************************************************
 * SAT4J: a SATisfiability library for Java Copyright (C) 2004, 2012 Artois University and CNRS
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU Lesser General Public License Version 2.1 or later (the
 * "LGPL"), in which case the provisions of the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of the LGPL, and not to allow others to use your version of
 * this file under the terms of the EPL, indicate your decision by deleting
 * the provisions above and replace them with the notice and other provisions
 * required by the LGPL. If you do not delete the provisions above, a recipient
 * may use your version of this file under the terms of the EPL or the LGPL.
 *
 * Based on the original MiniSat specification from:
 *
 * An extensible SAT solver. Niklas Een and Niklas Sorensson. Proceedings of the
 * Sixth International Conference on Theory and Applications of Satisfiability
 * Testing, LNCS 2919, pp 502-518, 2003.
 *
 * See www.minisat.se for the original solver in C++.
 *
 * Contributors:
 *   CRIL - initial API and implementation
 *******************************************************************************/
package org.sat4j.minisat.constraints.cnf;

import static org.sat4j.core.LiteralsUtils.neg;

import java.io.Serializable;

import org.sat4j.minisat.core.Constr;
import org.sat4j.minisat.core.ILits;
import org.sat4j.minisat.core.Propagatable;
import org.sat4j.specs.IVecInt;
import org.sat4j.specs.UnitPropagationListener;

/**
 * Data structure for binary clause.
 * 
 * @author leberre
 * @since 2.1
 */
public abstract class BinaryClause implements Propagatable, Constr,
        Serializable {

    private static final long serialVersionUID = 1L;

    protected double activity;

    private final ILits voc;

    protected int head;

    protected int tail;

    /**
     * Creates a new basic clause
     * 
     * @param voc
     *            the vocabulary of the formula
     * @param ps
     *            A VecInt that WILL BE EMPTY after calling that method.
     */
    public BinaryClause(IVecInt ps, ILits voc) {
        assert ps.size() == 2;
        this.head = ps.get(0);
        this.tail = ps.get(1);
        this.voc = voc;
        this.activity = 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Constr#calcReason(Solver, Lit, Vec)
     */
    public void calcReason(int p, IVecInt outReason) {
        if (this.voc.isFalsified(this.head)) {
            outReason.push(neg(this.head));
        }
        if (this.voc.isFalsified(this.tail)) {
            outReason.push(neg(this.tail));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see Constr#remove(Solver)
     */
    public void remove(UnitPropagationListener upl) {
        this.voc.watches(neg(this.head)).remove(this);
        this.voc.watches(neg(this.tail)).remove(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see Constr#simplify(Solver)
     */
    public boolean simplify() {
        if (this.voc.isSatisfied(this.head) || this.voc.isSatisfied(this.tail)) {
            return true;
        }
        return false;
    }

    public boolean propagate(UnitPropagationListener s, int p) {
        this.voc.watch(p, this);
        if (this.head == neg(p)) {
            return s.enqueue(this.tail, this);
        }
        assert this.tail == neg(p);
        return s.enqueue(this.head, this);
    }

    /*
     * For learnt clauses only @author leberre
     */
    public boolean locked() {
        return this.voc.getReason(this.head) == this
                || this.voc.getReason(this.tail) == this;
    }

    /**
     * @return the activity of the clause
     */
    public double getActivity() {
        return this.activity;
    }

    @Override
    public String toString() {
        StringBuffer stb = new StringBuffer();
        stb.append(Lits.toString(this.head));
        stb.append("["); //$NON-NLS-1$
        stb.append(this.voc.valueToString(this.head));
        stb.append("]"); //$NON-NLS-1$
        stb.append(" "); //$NON-NLS-1$
        stb.append(Lits.toString(this.tail));
        stb.append("["); //$NON-NLS-1$
        stb.append(this.voc.valueToString(this.tail));
        stb.append("]"); //$NON-NLS-1$
        return stb.toString();
    }

    /**
     * Retourne le ieme literal de la clause. Attention, cet ordre change durant
     * la recherche.
     * 
     * @param i
     *            the index of the literal
     * @return the literal
     */
    public int get(int i) {
        if (i == 0) {
            return this.head;
        }
        assert i == 1;
        return this.tail;
    }

    /**
     * @param d
     */
    public void rescaleBy(double d) {
        this.activity *= d;
    }

    public int size() {
        return 2;
    }

    public void assertConstraint(UnitPropagationListener s) {
        // assert this.voc.isUnassigned(this.head);
        boolean ret = s.enqueue(this.head, this);
        assert ret;
    }

    public void assertConstraintIfNeeded(UnitPropagationListener s) {
        if (voc.isFalsified(this.tail)) {
            boolean ret = s.enqueue(this.head, this);
            assert ret;
        }
    }

    public ILits getVocabulary() {
        return this.voc;
    }

    public int[] getLits() {
        int[] tmp = new int[2];
        tmp[0] = this.head;
        tmp[1] = this.tail;
        return tmp;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            BinaryClause wcl = (BinaryClause) obj;
            if (wcl.head != this.head || wcl.tail != this.tail) {
                return false;
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        long sum = this.head + this.tail;
        return (int) sum / 2;
    }

    public void register() {
        this.voc.watch(neg(this.head), this);
        this.voc.watch(neg(this.tail), this);
    }

    public boolean canBePropagatedMultipleTimes() {
        return false;
    }

    public Constr toConstraint() {
        return this;
    }

    public void calcReasonOnTheFly(int p, IVecInt trail, IVecInt outReason) {
        calcReason(p, outReason);
    }
}
