package com.shopping.cart.checksum;

import org.apache.commons.codec.digest.DigestUtils;

public class Md5CheckSumCalculator implements CheckSumCalculator {

	@Override
	public String calculateCheckSum(byte[] input) {
		// TODO Auto-generated method stub
		return DigestUtils.md5Hex(input);
	}

}
