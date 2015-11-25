package it.unical.mat.jwasp.output;

import java.math.BigInteger;
import java.util.List;

import org.sat4j.core.Vec;
import org.sat4j.core.VecInt;

import it.unical.mat.jwasp.utils.Util;

public class CollectionsPrinter extends ASPAnswerSetPrinter{
		
		private List<String> answerSet;
		private List<BigInteger> myCosts;
		
		public CollectionsPrinter(List<String> answerSet) {
			super();
			this.answerSet = answerSet;
		}
		
		public CollectionsPrinter(List<String> answerSet, List<BigInteger> costs) {
			super();
			this.answerSet = answerSet;
			this.myCosts = costs;
		}
		
		@Override
		public void startSearch() {
			answerSet.clear();
		}
		
		@Override
		public void greetings() {
	    }

		@Override
	    public void foundAnswerSet(int[] model) {
	        for (int i = 0; i < model.length; i++) {
	            if (model[i] > 0 && !Util.isHidden(model[i])) {
	            	answerSet.add(Util.getName(model[i]));
	            }
	        }	        
	    }
		
		@Override
		public void printCautiousConsequences(VecInt cautiousConsequences) {
			for (int i = 0; i < cautiousConsequences.size(); i++) {
				answerSet.add(Util.getName(cautiousConsequences.get(i)));	                       
	        }       
		}   

		@Override
	    public void foundIncoherence() {			
	    }
	    
		@Override
	    public void printNumberOfAnswerSets(int numberOfAnswerSets) {
	    }
		
		@Override
		public void printCosts(Vec<BigInteger> costs) {
	    	for(int i = costs.size() - 1; i >= 0; i--) {
	    		myCosts.add(costs.get(i));	    		
	    	}
	    }
	    
		@Override
	    public void foundOptimum() {	    	
	    }
}
