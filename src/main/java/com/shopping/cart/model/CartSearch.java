package com.shopping.cart.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
@ToString
@Builder
@Getter
public class CartSearch {
	
	private String owner;
	private String address;
	private String status;

}
