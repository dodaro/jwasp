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

import java.math.BigInteger;

import org.sat4j.core.Vec;
import org.sat4j.core.VecInt;

public class Aggregate {

    private boolean removed;
    private int id;
    private BigInteger bound;
    private VecInt lits;
    private Vec<BigInteger> weights;

    public Aggregate(int id, long bound) {
        this.lits = new VecInt();
        this.weights = new Vec<BigInteger>();
        removed = false;
        this.id = id;
        this.bound = BigInteger.valueOf(bound);

        this.lits.push(-id);
        this.weights.push(this.bound);
    }

    public void remove() {
        removed = true;
    }

    public boolean isRemoved() {
        return removed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigInteger getBound() {
        return bound;
    }

    public int getBoundInt() {
        return bound.intValue();
    }

    public void setBound(BigInteger bound) {
        this.bound = bound;
    }

    public VecInt getLits() {
        return lits;
    }

    public Vec<BigInteger> getWeights() {
        return weights;
    }

    public void addLiteral(int lit, long weight) {
        this.lits.push(lit);
        BigInteger newWeight = BigInteger.valueOf(weight);
        this.weights.push(newWeight);
    }

    public Aggregate getOpposite() {
        Aggregate aggr = new Aggregate(-id, 0);

        BigInteger sumOfWeights = BigInteger.valueOf(0);
        for (int i = 1; i < lits.size(); i++) {
            BigInteger weight = weights.get(i);
            aggr.addLiteral(-lits.get(i), weight.longValue());
            sumOfWeights = sumOfWeights.add(weight);
        }

        aggr.bound = (sumOfWeights.subtract(this.bound)).add(BigInteger.valueOf(1));
        aggr.weights.set(0, aggr.bound);
        return aggr;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(id);
        strBuilder.append(": [");
        if (!lits.isEmpty()) {
            strBuilder.append(lits.get(0));
            strBuilder.append("=");
            strBuilder.append(weights.get(0));
            for (int i = 1; i < lits.size(); i++) {
                strBuilder.append(",");
                strBuilder.append(lits.get(i));
                strBuilder.append("=");
                strBuilder.append(weights.get(i));
            }
        }
        strBuilder.append("] >= ");
        strBuilder.append(bound);
        return strBuilder.toString();
    }

}
