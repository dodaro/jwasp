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
package it.unical.mat.jwasp.parser;

import it.unical.mat.jwasp.datastructures.Aggregate;
import it.unical.mat.jwasp.datastructures.Rule;
import it.unical.mat.jwasp.solver.ProgramBuilder;
import it.unical.mat.jwasp.utils.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.sat4j.core.Vec;
import org.sat4j.core.VecInt;
import org.sat4j.specs.ContradictionException;

public class ASPGringoParser {

    private BufferedReader br;
    private String currentLine;
    private String[] line;
    private Rule currentRule;
    private Aggregate currentAggregate;
    private ProgramBuilder builder;

    public ASPGringoParser(ProgramBuilder builder) {
        currentRule = null;
        currentAggregate = null;
        this.builder = builder;
    }

    public void parseFrom(File f) throws ContradictionException,
            NumberFormatException, IOException, BadInputException {
    	br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        parseInput();
    }

    public void parseFromStandardInput() throws ContradictionException,
            NumberFormatException, IOException, BadInputException {
        br = new BufferedReader(new InputStreamReader(System.in));
        parseInput();
    }

    private void parseInput() throws ContradictionException,
            NumberFormatException, IOException, BadInputException {
        parse();
    }

    private void parse() throws IOException, BadInputException,
            NumberFormatException, ContradictionException {
        boolean loop = true;
        while (loop) {
            currentLine = br.readLine();
            line = currentLine.split(" ");
            int type = Character.getNumericValue(currentLine.charAt(0));
            switch (type) {
                case Constants.GRINGO_NORMAL_RULE_ID:
                    readNormalRule();
                    break;
                case Constants.GRINGO_CHOICE_RULE_ID:
                    readChoiceRule();
                    break;
                case Constants.GRINGO_COUNT_CONSTRAINT_RULE_ID:
                    readCount();
                    break;
                case Constants.GRINGO_SUM_CONSTRAINT_RULE_ID:
                    readSum();
                    break;
                case Constants.GRINGO_DISJUNCTIVE_RULE_ID:
                    readDisjunctiveRule();
                    break;
                case Constants.GRINGO_OPTIMIZATION_RULE_ID:
                    readOptimizationRule();
                    break;
                case Constants.GRINGO_LINE_SEPARATOR:
                    loop = false;
                    break;
                default:
                    throw new BadInputException();
            }

            addCurrentRule();
        }
        readAtomsTable();
        readOtherInformation();

        builder.finalizeProgram();
    }

    private void updateMax(int var) {
        builder.addVariable(var);
    }

    private void readAtomsTable() throws IOException {
        while (true) {
            String name = br.readLine();
            if (name.equals("0")) {
                return;
            }
            String[] splitName = name.split(" ");
            int id = Integer.parseInt(splitName[0]);
            updateMax(id);
            builder.setVarName(id, splitName[1]);            
        }
    }

    private void readOtherInformation() throws IOException,
            ContradictionException {
        String value = br.readLine();
        assert (value.equals(Constants.GRINGO_BPLUS));
        value = br.readLine();
        while (!value.equals("0")) {
            int lit = Integer.parseInt(value);
            updateMax(lit);
            builder.addTrueLiteral(lit);
            value = br.readLine();
        }

        value = br.readLine();
        assert (value.equals(Constants.GRINGO_BMINUS));
        value = br.readLine();
        while (!value.equals("0")) {
            int lit = Integer.parseInt(value);
            updateMax(lit);
            builder.addTrueLiteral(-lit);
            value = br.readLine();
        }

        value = br.readLine();
        if (!value.equals("1")) {
            throw new ContradictionException();
        }
    }

    private void addCurrentRule() throws ContradictionException {
        if (currentRule == null) {
            return;
        }

        builder.addRule(currentRule);
        currentRule = null;
    }

    private void addCurrentAggregate() {
        if (currentAggregate == null) {
            return;
        }

        builder.addAggregate(currentAggregate);
        currentAggregate = null;
    }

    private void readBody(int next, int size, int negativeSize) {
        int i = 0;
        while (i < negativeSize) {
            int lit = Integer.parseInt(line[next++]);
            updateMax(lit);
            currentRule.addLiteralInBody(-lit);
            i++;
        }

        while (i < size) {
            int lit = Integer.parseInt(line[next++]);
            updateMax(lit);
            currentRule.addLiteralInBody(lit);
            i++;
        }
    }

    private void readNormalRule() {
    	int next = 1;
        currentRule = new Rule();
        int h = Integer.parseInt(line[next++]);
        updateMax(h);        
        if (h != 1) {
            currentRule.addAtomInHead(h);
        }
        int size = Integer.parseInt(line[next++]);
        int negativeSize = Integer.parseInt(line[next++]);
        readBody(next, size, negativeSize);        
    }

    private void readDisjunctiveRule() {
        int next = 1;
        currentRule = new Rule();
        int hSize = Integer.parseInt(line[next++]);
        for (int i = 0; i < hSize; i++) {
            int h = Integer.parseInt(line[next++]);
            updateMax(h);
            currentRule.addAtomInHead(h);
        }
        int size = Integer.parseInt(line[next++]);
        int negativeSize = Integer.parseInt(line[next++]);
        readBody(next, size, negativeSize);
    }

    private void readChoiceRule() {
        int next = 1;
        currentRule = new Rule(true);
        int hSize = Integer.parseInt(line[next++]);
        for (int i = 0; i < hSize; i++) {
            int h = Integer.parseInt(line[next++]);
            updateMax(h);
            currentRule.addAtomInHead(h);
        }
        int size = Integer.parseInt(line[next++]);
        int negativeSize = Integer.parseInt(line[next++]);
        readBody(next, size, negativeSize);
    }

    private void readCount() {
        int next = 1;
        int id, size, negativeSize, tmp;
        long bound;
        id = Integer.parseInt(line[next++]);
        updateMax(id);
        size = Integer.parseInt(line[next++]);
        negativeSize = Integer.parseInt(line[next++]);
        bound = Long.parseLong(line[next++]);
        assert (size >= bound);

        currentAggregate = new Aggregate(id, bound);
        while (negativeSize-- > 0) {
            --size;
            tmp = Integer.parseInt(line[next++]);
            updateMax(tmp);
            currentAggregate.addLiteral(-tmp, 1);
        }
        while (size-- > 0) {
            tmp = Integer.parseInt(line[next++]);
            updateMax(tmp);
            currentAggregate.addLiteral(tmp, 1);
        }

        addCurrentAggregate();
    }

    private void readSum() {
        int next = 1;
        int id, size, negativeSize, tmp;
        long bound, weight;
        id = Integer.parseInt(line[next++]);
        updateMax(id);
        bound = Long.parseLong(line[next++]);
        size = Integer.parseInt(line[next++]);
        negativeSize = Integer.parseInt(line[next++]);
        assert (size >= bound);

        currentAggregate = new Aggregate(id, bound);

        int counter = 0;
        while (counter < negativeSize) {
            tmp = Integer.parseInt(line[next]);
            updateMax(tmp);
            weight = Long.parseLong(line[next + size]);
            next++;
            if (weight > bound) {
                weight = bound;
            }
            currentAggregate.addLiteral(-tmp, weight);
            ++counter;
        }
        while (counter < size) {
            tmp = Integer.parseInt(line[next]);
            updateMax(tmp);
            weight = Long.parseLong(line[next + size]);
            next++;
            if (weight > bound) {
                weight = bound;
            }
            currentAggregate.addLiteral(tmp, weight);
            ++counter;
        }

        addCurrentAggregate();
    }

	 private void readOptimizationRule() {
		 //first value is 0
	 	int next = 2;
        int size, negativeSize, tmp;
        long weight;
        size = Integer.parseInt(line[next++]);
        negativeSize = Integer.parseInt(line[next++]);
        
        int counter = 0;
        
        VecInt literals = new VecInt();        
        while( counter < negativeSize )
		{
	        tmp = Integer.parseInt(line[next++]);
	        updateMax( tmp );
	        literals.push(-tmp);
	        counter++;
	    }
		
        while( counter < size )
	    {
        	tmp = Integer.parseInt(line[next++]);
	        updateMax( tmp );
	        literals.push( tmp );
	        counter++;
	    }

        Vec<Long> weights = new Vec<Long>();
	    counter = 0;
	    while( counter < negativeSize )
	    {
	    	weight = Long.parseLong(line[next++]);	        
	        weights.push(weight);
	        ++counter;
	    }
		    
	    while( counter < size )
	    {
	    	weight = Long.parseLong(line[next++]);
	        weights.push(weight);
	        ++counter;
	    }
		
	    builder.addOptimizationRule( literals, weights );
     }	
}
