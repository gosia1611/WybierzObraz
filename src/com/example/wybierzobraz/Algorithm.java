package com.example.wybierzobraz;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Algorithm {
	enum Phase {ZERO_PHASE, TRIAL_PHASE, LEARNING_PHASE, TEST_PHASE, END_PHASE};
	private Phase phase = Phase.TRIAL_PHASE;
	enum Choise {LEFT, RIGHT};
	int pairNo;
	int cycleNo;
	Integer[] lastLearnPair;
	
	double p1 = 0.8;
	double p2 = 0.2;
	double p3 = 0.7;
	double p4 = 0.3;
	double p5 = 0.6;
	double p6 = 0.4;
	double p7 = 1;
	double p8 = 0;
	
	Integer[] images = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e, R.drawable.f};
	Integer[] trialPair = {R.drawable.g, R.drawable.h};
	Integer[][] learnPairs;
	Map<Integer, Double> learningPhaseProbabilities = new TreeMap<Integer, Double>();
	Integer[][] testPairs;
	
	Algorithm() {
		init();
	}
	
	void init() {
		initLearningPhasePairs();
		initTestPhasePairs();
	}
	
	void initLearningPhasePairs() {
		Integer[] randomizedImages = Utils.permutation(images);
		learnPairs = new Integer[][]{{randomizedImages[0], randomizedImages[1]}, {randomizedImages[2], randomizedImages[3]}, {randomizedImages[4], randomizedImages[5]}};
		learningPhaseProbabilities.put(learnPairs[0][0], 0.8);
		learningPhaseProbabilities.put(learnPairs[0][1], 0.2);
		learningPhaseProbabilities.put(learnPairs[1][0], 0.7);
		learningPhaseProbabilities.put(learnPairs[1][1], 0.3);
		learningPhaseProbabilities.put(learnPairs[2][0], 0.6);
		learningPhaseProbabilities.put(learnPairs[0][1], 0.4);
	}

	
	void initTestPhasePairs() {
		ArrayList<Integer[]> allPairs = new ArrayList<Integer[]>(75);
		for (int i=0; i<images.length; i++) {
			for (int j=i+1; j<images.length; j++){
				Integer[] pair = new Integer[]{images[i], images[j]};
				if (isLearningPhasePair(pair)) {
					allPairs.add(pair);
				} else {
					for(int k=0; k<6; k++){
						allPairs.add(pair);
					}
				}
			}
		}
		testPairs = allPairs.toArray(new Integer[][]{});
	}
	
	private boolean isLearningPhasePair(Integer[] pair) {
		for (int i=0; i<learnPairs.length; i++) {
			if(pairsEqual(pair, learnPairs[i])) {
				return true;
			}
		}
		return false;
	}
	
	// p is pair1, r is pair2
	private boolean pairsEqual(Integer[] p, Integer[] r) {
		return (p[0] == r[0] || p[0] == r[1]) && (p[1] == r[0] || p[1] == r[1]);
	}
	
	void initCycle() {
		if(phase == Phase.LEARNING_PHASE) {
			initLearnigPhaseOrder();
		}
	}
	

	void initLearnigPhaseOrder() {
		Integer[][] randomizedPairs = Utils.permutation(learnPairs);
		do {
			learnPairs = new Integer[][]{Utils.permutation(randomizedPairs[0]),Utils.permutation(randomizedPairs[1]), Utils.permutation(randomizedPairs[2])};
		} while (lastLearnPair != null && !pairsEqual(lastLearnPair,learnPairs[0]));
		pairNo = 0;
		lastLearnPair = learnPairs[learnPairs.length-1];
	}
	

	Integer[] getImages(){
		switch(phase) {
		case ZERO_PHASE:
			return getTrialPhaseImages();
		case TRIAL_PHASE:
			return getTrialPhaseImages();
		case LEARNING_PHASE: 
			return getLearnPhaseImages();
		case TEST_PHASE:
			return getTestPhaseImages();
		case END_PHASE:
			return null;
		}
		return null;  //wrong phase number
	}
	
	Integer[] getTrialPhaseImages() {
		return Utils.permutation(trialPair);
	}
	
	Integer[] getLearnPhaseImages() {
		return learnPairs[pairNo];
	}
	
	Integer[] getTestPhaseImages() {
		return testPairs[pairNo];
	}
	
	//number of pairs in this cycle
	private int pairsCount() {
		switch(phase){
		case ZERO_PHASE:
			return 1;
		case TRIAL_PHASE:
			return 1;
		case LEARNING_PHASE:
			return learnPairs.length;
		case TEST_PHASE:
			return testPairs.length;
		default:
			return 0;  //error
		}
	}
	
	private int cyclesCount() {
		switch(phase){
		case ZERO_PHASE:
			return 1;
		case TRIAL_PHASE:
			return 6;
		case LEARNING_PHASE:
			return 20;
		case TEST_PHASE:
			return 1;
		default:
			return 0;  //error
		}
	}
	
	Answer getResult(Choise choise, int chosenImage) {
		updateState();
		return new Answer(phase != Phase.END_PHASE,false,false); //perNo==0 && phase!=phaseZero && phase!=trialPhase

	}
	static Random rand = new Random();
	boolean getAnswer(Choise choise, int chosenImage) {
		switch(phase) {
		case ZERO_PHASE:
			return true;
		case TRIAL_PHASE:
			return chosenImage == trialPair[0];
		case LEARNING_PHASE:
			double probability = learningPhaseProbabilities.get(chosenImage);
			return rand.nextDouble()<probability;
			//TEST_PHASE
		default:
			return false;  //error
		}
	}
	
	void updateState() {
		pairNo++;
		if(pairNo >= pairsCount()){
			cycleNo++;
			initCycle();
			pairNo = 0;
		}
		if(cycleNo >= cyclesCount()){
			switch(phase) {
			case ZERO_PHASE:
				phase = Phase.TRIAL_PHASE;
				break;
			case TRIAL_PHASE:
				phase = Phase.LEARNING_PHASE;
				break;
			case LEARNING_PHASE:
				phase = Phase.TEST_PHASE;
				break;
			case TEST_PHASE:
				phase = Phase.END_PHASE;
				break;
			default:  //error
			}
			cycleNo = 0;
		}
	}
	
	static class ResultRow {
		int leftImage;
		int rightImage;
		enum Result {PRICE, PENALTY};
		boolean betterChoise;
	}
	
	static class Answer {
		boolean doContinue; 
		boolean goodAnswer; 
		boolean phaseChanged;
		
		Answer(boolean doContinue, boolean goodAnswer, boolean phaseChanged) {
			this.doContinue = doContinue;
			this.goodAnswer = goodAnswer;
			this.phaseChanged = phaseChanged;
		}
		
	}
}
