package com.shopping.cart.checksum;

public class CheckSumCalculatorFactory {

	public CheckSumCalculator getCheckSumCalculator(String type) {
		if (type.equalsIgnoreCase("MD5")) {
			return new Md5CheckSumCalculator();
		} else if (type.equalsIgnoreCase("base64")) {
			return new Base64CheckSumCalculator();
		} else {
			return null;
		}
	}

}
