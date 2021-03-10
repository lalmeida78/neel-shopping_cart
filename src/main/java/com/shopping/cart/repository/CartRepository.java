package com.shopping.cart.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.shopping.cart.entity.*;

@Transactional
public interface CartRepository extends CrudRepository<Cart, Integer> {

	public List<Cart> findByOwnerLikeAndDeliveryaddressLike(String owner, String address) throws RuntimeException;
}
