package com.shopping.cart.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.shopping.cart.checksum.CheckSumCalculatorFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChecksumValidation {

	@Value("${checksum}")
	private String checksum;

	public String getChecksum(byte[] input) {
		CheckSumCalculatorFactory calculatorFactory = new CheckSumCalculatorFactory();
		return calculatorFactory.getCheckSumCalculator(checksum).calculateCheckSum(input);
	}

}
