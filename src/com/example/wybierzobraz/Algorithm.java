package com.example.wybierzobraz;

import java.util.ArrayList;

public class Algorithm {
	enum Phase {ZERO_PHASE, TRIAL_PHASE, LEARNING_PHASE, TEST_PHASE, END_PHASE};
	private Phase phase = Phase.TRIAL_PHASE;
	enum Choise {LEFT, RIGHT};
	int pairNo;
	int cycleNo;
	
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
	}
	
	void initTestPhasePairs() {
		ArrayList<Integer[]> pairs = new ArrayList<Integer[]>(75);
		for (int i=0; i<6; i++) {
			for (int j=0; j<6; j++){
				if(i!=j) {
					pairs.add(new Integer[]{images[i], images[j]});
				}
			}
		}
		testPairs = pairs.toArray(new Integer[][]{});
	}
	
	void initCycle() {
		if(phase == Phase.LEARNING_PHASE) {
			initLearnigPhaseOrder();
		}
	}
	

	void initLearnigPhaseOrder() {
		Integer[][] randomizedPairs = Utils.permutation(learnPairs);
		learnPairs = new Integer[][]{Utils.permutation(randomizedPairs[0]),Utils.permutation(randomizedPairs[1]), Utils.permutation(randomizedPairs[2])};
		pairNo = 0;
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
	
	boolean getAnswer(Choise choise) {
		updateState();
		return true;
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
		
		}
	}
	
	static class ResultRow {
		int leftImage;
		int rightImage;
		enum Result {PRICE, PENALTY};
		boolean betterChoise;
	}
}
