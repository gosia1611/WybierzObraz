package com.example.wybierzobraz;

import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Algorithm {
	enum Phase {ZERO_PHASE, TRIAL_PHASE, LEARNING_PHASE, TEST_PHASE, PROBABILITY_PHASE, END_PHASE};
	private Phase phase = Phase.ZERO_PHASE;
	enum Choice {LEFT, RIGHT, NOTHING};
	int pairNo;
	int cycleNo;
	Integer[] lastLearnPair;
	boolean phaseChanged = false;
	CSVWriter writer, writer2;
	List<String[]> data = new ArrayList<String[]>();
	long time = System.currentTimeMillis();
	
	Integer[] images = {R.drawable.chi, R.drawable.ha, R.drawable.ka, R.drawable.ma, R.drawable.sa, R.drawable.so};
	Integer[] trialPair = {R.drawable.ya1, R.drawable.yo1};
	Integer[][] learnPairs;
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
	}
	
	void initTestPhasePairs() {
		ArrayList<Integer[]> allPairs = new ArrayList<Integer[]>(75);
		for (int i=0; i<images.length; i++) {
			for (int j=i+1; j<images.length; j++){
				Integer[] pair = Utils.permutation(new Integer[]{images[i], images[j]});
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
			return trialPair;
		case TRIAL_PHASE:
			return getTrialPhaseImages();
		case LEARNING_PHASE: 
			return getLearnPhaseImages();
		case TEST_PHASE:
			return getTestPhaseImages();
		case PROBABILITY_PHASE:
			return images;
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
			return 6; //6;
		case LEARNING_PHASE:
			return 20; //20;
		case TEST_PHASE:
			return 1;
		default:
			return 0;  //error
		}
	}
	
	Phase getCurrentPhase() {
		return phase;
	}
	
	State getUpdatedState() {
		updateState();
		State answer = new State(phase != Phase.END_PHASE, phaseChanged); //perNo==0 && phase!=phaseZero && phase!=trialPhase
		return answer;
	}

	boolean isGoodAnswer(Choice choise, int chosenImage) {
		Random rand = new Random(System.currentTimeMillis());
		double probability = rand.nextDouble();
		//if choise!=NOTHING
		if(phase == Phase.TRIAL_PHASE || phase == Phase.LEARNING_PHASE || phase == Phase.TEST_PHASE) {
			if (probability <= Probabilities.get(chosenImage)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

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
				data.add(new String[] {phase.toString()});
				break;
			case LEARNING_PHASE:
				phase = Phase.TEST_PHASE;
				data.add(new String[] {phase.toString()});
				break;
			case TEST_PHASE:
				phase = Phase.PROBABILITY_PHASE;
				//writeFile();
				break;
			case PROBABILITY_PHASE:
				phase = Phase.END_PHASE;
				//writeFile();
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
	
	static class State {
		public boolean doContinue;
		public boolean ifPhaseChanged;
		
		State(boolean doContinue, boolean ifPhaseChanged) {
			this.doContinue = doContinue;
			this.ifPhaseChanged = ifPhaseChanged;
		}
	}
	
	void setPhase(Phase phase) {
		this.phase = phase;
	}

	void writeFile() {
		try {
			writer = new CSVWriter(new FileWriter("mnt/sdcard/file_images.csv"), ',');
			writer.writeNext(new String[] {"", "LEFT_IMAGE", "RIGHT_IMAGE", "CHOICE", "IS_RIGHT_CHOICE", "IS_BETTER_CHOICE"});
			writer.writeAll(data);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void addToFile(int leftImage, int rightImage, Choice choice, boolean isGoodAnswer){
		String answer = "";
		String isBetterChoice = "";
		if (choice == Choice.NOTHING){
			answer = "NOTHING";
			isBetterChoice = "NOTHING";
		}
		else if (isGoodAnswer == true)
			answer = "RIGHT";
		else if (isGoodAnswer == false)
			answer = "WRONG";
		
		if (choice != Choice.NOTHING){
			if ((choice == Choice.LEFT && (Probabilities.get(leftImage) > Probabilities.get(rightImage))) || (choice == Choice.RIGHT && (Probabilities.get(rightImage) > Probabilities.get(leftImage)))) {
				isBetterChoice = "YES";
			} else {
				isBetterChoice = "NO";
			}
		}
		String leftIm = ""; 
		String rightIm = "";
		for(int i=0; i<images.length; i++) {
			if (leftImage == images[i])
				leftIm = Integer.toString(i+1);
			else if (rightImage == images[i])
				rightIm = Integer.toString(i+1);
		}
		data.add(new String[] {"", leftIm, rightIm, choice.toString(), answer, isBetterChoice});
	}
	
	void writeProbabilitiesFile() {
		try {
			writer2 = new CSVWriter(new FileWriter("mnt/sdcard/file_probabilities.csv"), ',');
			writer2.writeNext(new String[] {"IMAGE_NUMBER", "PROBABILITY"});
			writer2.writeAll(data);
			writer2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void addProbabilityToFile(int image, int probability) {
		data.add(new String[] {Integer.toString(image), Integer.toString(probability)});
	}
}