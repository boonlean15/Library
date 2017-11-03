package com.utils;

import java.util.Random;

public class RandomUtils {

	private static final Random random = new Random();
	
	public static int getRandomId(int num){
		int id = random.nextInt(num);
		return id;
	}
}
