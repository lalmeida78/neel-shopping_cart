package com.shopping.cart.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	/*
	 * @ManyToOne(fetch = FetchType.LAZY) private Cart cart;
	 */
	private String name;
	private String description;
	private float unit_price;
	private int quantity;	
}
