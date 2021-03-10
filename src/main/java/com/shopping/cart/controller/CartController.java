package com.shopping.cart.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.cart.entity.Cart;
import com.shopping.cart.model.CartSearch;
import com.shopping.cart.model.Items;
import com.shopping.cart.service.CartService;
import com.shopping.cart.service.CartServiceException;
import com.shopping.cart.service.UtilsService;

@RestController
@RequestMapping("/cart")
public class CartController {

	@Autowired
	CartService cartService;
	
	@Autowired
	UtilsService utilsService;
	
	
	@RequestMapping(value = "/add-cart", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> createCart(@RequestBody Cart cart) {
		cart.setCreation_date_time(LocalDateTime.now());
		Cart results = cartService.addCart(cart);
		utilsService.addOrUpdateCart(results.getId(), results.getCreation_date_time());
		results.setStatus("Active");
		return new ResponseEntity<Object>(results, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/update-cart/{cartId}", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateCart(@RequestBody Items updatedItems,@PathVariable("cartId") String cartId) {
		int id = Integer.parseInt(cartId);
		if(utilsService.isValidCart(id)) {
			Cart results = cartService.updateCart(updatedItems,id);
			results.setStatus("Active");
			return new ResponseEntity<Object>(results, HttpStatus.CREATED);
		} else {
			throw new CartServiceException("Cart is expired. we cant update it", HttpStatus.LOCKED);
		}
		
	}
	@RequestMapping(value = "/delete-cart/{cartId}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteCart(@PathVariable("cartId") String cartId) {
		int id = Integer.parseInt(cartId);
		if(utilsService.isValidCart(id)) {
			cartService.deleteCart(id);
			return new ResponseEntity<Object>("Cart is deleted successfully",HttpStatus.OK);
		} else {
			throw new CartServiceException("Cart is expired. we cant update it", HttpStatus.LOCKED);
		}
	}


	@RequestMapping("/{cartId}")
	public ResponseEntity<Object> getCartById(@PathVariable("cartId") String cartId) {
		 Optional<Cart> result = cartService.getCartDetailsById(Integer.parseInt(cartId));
		 if(result.isPresent()) {
			 return new ResponseEntity<Object>(result.get(), HttpStatus.OK);
		 } else {
			 throw new CartServiceException("There is no cart with id as "+ cartId, HttpStatus.NOT_FOUND);
		 }
	}
	
	@RequestMapping(value = "/search-cart", method = RequestMethod.GET)
	public ResponseEntity<Object> searchCart(@RequestParam(name="owner", required = false) String owner,
											 @RequestParam(name="address", required = false) String address,
											 @RequestParam(name="status", required = false) String status) {	
		CartSearch cartSearch = CartSearch.builder().address(address).owner(owner).status(status).build();
		List<Cart> cartList = cartService.searchCart(cartSearch);
		if(Objects.nonNull(cartList) && cartList.size()>0) {
			cartList.stream().forEach(c -> {
				if(utilsService.isValidCart(c.getId())) {
					c.setStatus("Active");
				} else {
					c.setStatus("Expired");
				}
			});
			if(Objects.nonNull(cartSearch.getStatus())){
				cartList = cartList.stream().filter(c->c.getStatus().equalsIgnoreCase(cartSearch.getStatus())).collect(Collectors.toList());
				if(cartList.isEmpty()) {
					throw new CartServiceException("There is no cart with given owner as "+ cartSearch.getOwner()+" address as "+cartSearch.getAddress() +" status as "+ cartSearch.getStatus(), HttpStatus.NOT_FOUND);
				}
			}
			return new ResponseEntity<Object>(cartList, HttpStatus.OK);
		} else {
			throw new CartServiceException("There is no cart with given owner as "+ cartSearch.getOwner()+" address as "+cartSearch.getAddress(), HttpStatus.NOT_FOUND);
		}
				
	}

}
