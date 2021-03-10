package com.shopping.cart.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopping.cart.entity.Cart;
import com.shopping.cart.model.CartSearch;
import com.shopping.cart.model.Items;
import com.shopping.cart.repository.CartRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CartService {

	@Autowired
	CartRepository cartRepository;

	@Autowired
	ChecksumValidation checksumValidation;

	public Optional<Cart> getCartDetailsById(int id) {
		try {
			return cartRepository.findById(id);
		} catch (Exception e) {
			throw new CartServiceException("Got exception while fetching cart details",
					HttpStatus.INTERNAL_SERVER_ERROR, e);
		}
	}

	@Transactional
	public Cart updateCart(Items item, int cartId) {
		try {
			Optional<Cart> result = getCartDetailsById(cartId);
			if (result.isPresent()) {
				Cart cart = result.get();
				cart.getItems().removeAll(cart.getItems());
				cart.getItems().addAll(item.getItems());
				return cartRepository.save(cart);
			} else {
				throw new CartServiceException("Unable to found cart with id:" + cartId, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			throw new CartServiceException("Got exception while updating cart details",
					HttpStatus.INTERNAL_SERVER_ERROR, e);
		}

	}

	@Transactional
	public Cart addCart(Cart cart) {
		try {
			String input = cart.getOwner() + "" + cart.getDeliveryaddress();
			String checksum = checksumValidation.getChecksum(input.getBytes());
			cart.setChecksum(checksum);
			cart = cartRepository.save(cart);
			return cart;
		} catch (Exception e) {
			throw new CartServiceException("Got exception while adding cart details", HttpStatus.INTERNAL_SERVER_ERROR,
					e);
		}
	}

	@Transactional
	public void deleteCart(int id) {
		try {
			cartRepository.deleteById(id);
		} catch (Exception e) {
			throw new CartServiceException("Got exception while deleting cart", HttpStatus.INTERNAL_SERVER_ERROR, e);
		}
	}

	public List<Cart> searchCart(CartSearch cartSearch) {
		try {
			String address = cartSearch.getAddress();
			String owner = cartSearch.getOwner();
			return cartRepository.findByOwnerLikeAndDeliveryaddressLike(Objects.nonNull(owner) ? owner : "",
					Objects.nonNull(address) ? address : "");
		} catch (Exception e) {
			throw new CartServiceException("Got exception while searching cart", HttpStatus.INTERNAL_SERVER_ERROR, e);
		}
	}
}
