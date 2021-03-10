package com.shopping.cart.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopping.cart.entity.Cart;
import com.shopping.cart.repository.CartRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UtilsService {

	private Map<Integer, LocalDateTime> cartList;

	@Autowired
	public UtilsService(CartRepository cartRepository) {
		// TODO Auto-generated constructor stub
		Iterable<Cart> list = cartRepository.findAll();
		cartList = StreamSupport
				.stream(Spliterators.spliteratorUnknownSize(list.iterator(), Spliterator.ORDERED), false)
				.collect(Collectors.toMap(Cart::getId, Cart::getCreation_date_time));
	}

	public boolean isValidCart(int cartId) {
		if (cartList.containsKey(cartId)) {
			Duration duration = Duration.between(cartList.get(cartId), LocalDateTime.now());
			return duration.toHours() > 3 ? false : true;
		} else {
			return false;
		}

	}

	public synchronized void addOrUpdateCart(int id, LocalDateTime dateTime) {
		cartList.put(id, dateTime);
	}

}
