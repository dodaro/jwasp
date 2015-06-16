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

import org.sat4j.core.VecInt;

public class Rule {

    private boolean removed;
    private boolean choiceRule;    
    private VecInt head;
    private VecInt body;

    private void init(boolean choiceRule) {
    	head = new VecInt();
        body = new VecInt();
        removed = false;
        this.choiceRule = choiceRule;
    }    
    
    public Rule() {
        init(false);
    }

    public Rule(boolean choiceRule) {
        init(choiceRule);        
    }

    public void remove() {
        removed = true;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void addAtomInHead(int atom) {
        this.head.push(atom);
    }

    public void addLiteralInBody(int literal) {
        this.body.push(literal);
    }

    public VecInt getHead() {
        return head;
    }

    public void setHead(VecInt head) {
        this.head = head;
    }

    public VecInt getBody() {
        return body;
    }

    public void setBody(VecInt body) {
        this.body = body;
    }

    public boolean isChoiceRule() {
        return choiceRule;
    }

    public void setChoiceRule(boolean choiceRule) {
        this.choiceRule = choiceRule;
    }

    public boolean isConstraint() {
        return head.isEmpty();
    }

    public boolean isFact() {
        return head.size() == 1 && body.isEmpty();
    }

    public boolean isBodyEmpty() {
        return body.isEmpty();
    }

    public boolean isDisjunctive() {
        return head.size() > 1 && !choiceRule;
    }

    public VecInt toClause() {
        VecInt clause = new VecInt(head.size() + body.size());
        for (int i = 0; i < head.size(); i++) {
            clause.push(head.get(i));
        }
        for (int i = 0; i < body.size(); i++) {
            clause.push(-body.get(i));
        }
        return clause;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        StringBuilder strBuilder = new StringBuilder();

        if (!head.isEmpty()) {
            strBuilder.append(head.get(0));
            for (int i = 1; i < head.size(); i++) {
                strBuilder.append(" | ");
                strBuilder.append(head.get(i));
            }
        }

        if (!body.isEmpty()) {
            strBuilder.append(" :- ");
            strBuilder.append(body.get(0));
            for (int i = 1; i < body.size(); i++) {
                strBuilder.append(", ");
                strBuilder.append(body.get(i));
            }
        }
        strBuilder.append(".");
        return strBuilder.toString();
    }
}
