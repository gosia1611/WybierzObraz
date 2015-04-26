package com.example.wybierzobraz;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Algorithm {
	enum Phase {ZERO_PHASE, TRIAL_PHASE, LEARNING_PHASE, TEST_PHASE, END_PHASE};
	private Phase phase = Phase.TRIAL_PHASE;
	enum Choice {LEFT, RIGHT, NOTHING};
	int pairNo;
	int cycleNo;
	Integer[] lastLearnPair;
	boolean phaseChanged = false;
	
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
	Map<Integer, Double> Probabilities = new TreeMap<Integer, Double>();
	Integer[][] testPairs;
	
	Algorithm() {
		init();
	}
	
	void init() {
		initProbabilites();
		initLearningPhasePairs();
		initTestPhasePairs();
	}
	
	void initProbabilites() {
		Probabilities.put(images[0], 0.8);
		Probabilities.put(images[1], 0.2);
		Probabilities.put(images[2], 0.7);
		Probabilities.put(images[3], 0.3);
		Probabilities.put(images[4], 0.6);
		Probabilities.put(images[5], 0.4);
		Probabilities.put(trialPair[0], 1.0);
		Probabilities.put(trialPair[1], 0.0);
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
		testPairs = Utils.permutation(allPairs.toArray(new Integer[][]{}));
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
		//do {
			learnPairs = new Integer[][]{Utils.permutation(randomizedPairs[0]),Utils.permutation(randomizedPairs[1]), Utils.permutation(randomizedPairs[2])};
		//} while (lastLearnPair != null && !pairsEqual(lastLearnPair,learnPairs[0]));
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
			return 2; //6;
		case LEARNING_PHASE:
			return 2; //20;
		case TEST_PHASE:
			return 1;
		default:
			return 0;  //error
		}
	}
	
	Answer getResult(Choice choise, int chosenImage) {
		Phase curPhase = phase;
		updateState();
		boolean goodAnswer = isGoodAnswer(chosenImage);
		Answer answer = new Answer(phase != Phase.END_PHASE, goodAnswer, phaseChanged, curPhase); //perNo==0 && phase!=phaseZero && phase!=trialPhase
		return answer;
	}
	//
	private boolean isGoodAnswer(int chosenImage) {
		Random rand = new Random();
		double probability = rand.nextDouble();
		//if choise!=NOTHING
		if(phase == Phase.LEARNING_PHASE || phase == Phase.TEST_PHASE) {
			if (probability <= Probabilities.get(chosenImage)) {
			return true;
			} else {
				return false;
			}
		}
		return false;
	}
	//
	void updateState() {
		phaseChanged = false;
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
			phaseChanged = true;
		}
	}
	
	static class ResultRow {
		int leftImage;
		int rightImage;
		enum Result {PRICE, PENALTY};
		boolean betterChoise;
	}
	
	static class Answer {
		public boolean doContinue;
		public boolean goodAnswer; 
		public boolean ifPhaseChanged;
		public Phase phase;
		
		Answer(boolean doContinue, boolean goodAnswer, boolean ifPhaseChanged, Phase phase) {
			this.doContinue = doContinue;
			this.goodAnswer = goodAnswer;
			this.ifPhaseChanged = ifPhaseChanged;
			this.phase = phase;
		}
	}
}
