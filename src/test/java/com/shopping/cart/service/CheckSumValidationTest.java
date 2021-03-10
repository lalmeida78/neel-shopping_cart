package com.shopping.cart.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class CheckSumValidationTest {

	@Autowired
	ChecksumValidation checksumValidation;

	@Test
	public void testGetChecksum() {
		String input = "test";
		String result = checksumValidation.getChecksum(input.getBytes());
		Assert.assertNotNull(result);
	}

}
