package com.shopping.cart.checksum;

import java.util.Base64;

public class Base64CheckSumCalculator implements CheckSumCalculator{

	@Override
	public String calculateCheckSum(byte[] input) {
		// TODO Auto-generated method stub
		return Base64.getEncoder().encodeToString(input);
	}

}
