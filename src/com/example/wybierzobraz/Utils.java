package com.example.wybierzobraz;

import java.lang.reflect.Array;
import java.util.Random;

public class Utils {
	
	private static Random random = new Random(System.currentTimeMillis());
	
	static <T> T[] permutation(T[] inputArray){
		boolean[] used = new boolean[inputArray.length];
		T[] outputArray = (T[]) Array.newInstance(inputArray[0].getClass(), inputArray.length);
		
		for (int i=0; i<inputArray.length; i++) {
			int number = random.nextInt(inputArray.length);
			while(used[number]){
				number = random.nextInt(inputArray.length);
			}
			used[number]=true;
			outputArray[i]=inputArray[number];
		}	
		
		return outputArray;
	}


}
