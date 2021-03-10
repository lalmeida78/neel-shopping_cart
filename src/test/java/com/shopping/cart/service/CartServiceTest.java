package com.shopping.cart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.shopping.cart.entity.Cart;
import com.shopping.cart.entity.Item;
import com.shopping.cart.model.CartSearch;
import com.shopping.cart.model.Items;
import com.shopping.cart.repository.CartRepository;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class CartServiceTest {
	
	@Mock
	CartRepository cartRepository;
	
	@Mock
	ChecksumValidation checksumValidation;
	
	@Mock
	UtilsService utilsService;
	
	@InjectMocks
	CartService cartService;
	
	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void testGetCartDetailsById_success() {
		Optional<Cart> optional = Optional.of(mockCart());
		Mockito.when(cartRepository.findById(Mockito.anyInt())).thenReturn(optional);
		Optional<Cart> result = cartService.getCartDetailsById(1);
		Assert.assertNotNull(result);
		Assert.assertEquals("test", result.get().getOwner());
	}
	
	@Test(expected = CartServiceException.class)
	public void testGetCartDetailsById_failure() {
		Mockito.when(cartRepository.findById(Mockito.anyInt())).thenThrow(CartServiceException.class);
		cartService.getCartDetailsById(1);
	}
	
	@Test
	public void testUpdateCart_success() {
		Optional<Cart> optional = Optional.of(mockCart());
		Items items = new Items();
		items.setItems(optional.get().getItems());
		Mockito.when(cartRepository.findById(Mockito.anyInt())).thenReturn(optional);
		Mockito.when(cartRepository.save(Mockito.any(Cart.class))).thenReturn(optional.get());
		Cart result = cartService.updateCart(items, 1);
		Assert.assertNotNull(result);
		Assert.assertEquals("test", result.getOwner());
	}
	
	@Test(expected = CartServiceException.class)
	public void testUpdateCart_failure() {
		Optional<Cart> optional = Optional.of(mockCart());
		Items items = new Items();
		items.setItems(optional.get().getItems());
		Mockito.when(cartRepository.findById(Mockito.anyInt())).thenReturn(optional);
		Mockito.when(cartRepository.save(Mockito.any(Cart.class))).thenThrow(CartServiceException.class);
		cartService.updateCart(items, 1);
	}
	
	@Test(expected = CartServiceException.class)
	public void testUpdateCart_whenNoCartFound_failure() {
		Optional<Cart> optional = Optional.empty();
		Items items = new Items();
		items.setItems(mockCart().getItems());
		Mockito.when(cartRepository.findById(Mockito.anyInt())).thenReturn(optional);
		cartService.updateCart(items, 1);
	}
	
	@Test
	public void testAddCart_success() {
		Cart cart = mockCart();
		Mockito.when(checksumValidation.getChecksum(Mockito.any())).thenReturn("test");
		Mockito.when(cartRepository.save(Mockito.any(Cart.class))).thenReturn(cart);
		Cart result = cartService.addCart(cart);
		Assert.assertNotNull(result);
		Assert.assertEquals("test", result.getOwner());
	}
	
	@Test(expected = CartServiceException.class)
	public void testAddCart_failure() {
		Cart cart = mockCart();
		Mockito.when(checksumValidation.getChecksum(Mockito.any())).thenReturn("test");
		Mockito.when(cartRepository.save(Mockito.any(Cart.class))).thenThrow(CartServiceException.class);
		cartService.addCart(cart);
	}
	
	@Test
	public void testSearchCart_success() {
		Cart cart = mockCart();
		CartSearch cartSearch = CartSearch.builder().address("testAddress").owner("neel").build();
		List<Cart> list = new ArrayList<Cart>();
		list.add(cart);
		Mockito.when(cartRepository.findByOwnerLikeAndDeliveryaddressLike(Mockito.anyString(),Mockito.anyString())).thenReturn(list);
		List<Cart> result = cartService.searchCart(cartSearch);
		Assert.assertNotNull(result);
		Assert.assertEquals("test", result.get(0).getOwner());
	}
	
	@Test(expected = CartServiceException.class)
	public void testSearchCart_failure() {
		CartSearch cartSearch = CartSearch.builder().address("testAddress").owner("neel").build();
		Mockito.when(cartRepository.findByOwnerLikeAndDeliveryaddressLike(Mockito.anyString(),Mockito.anyString())).thenThrow(CartServiceException.class);
		cartService.searchCart(cartSearch);

	}
	
	private Cart mockCart() {
		float price = 100;
		List<Item> items = new ArrayList<Item>();
		Item item1 = new Item();
				item1.setDescription("description");
				item1.setName("tv1");
				item1.setQuantity(1);
				item1.setUnit_price(price);
		Item item2 = new Item();
		item2.setDescription("description");
		item2.setName("tv2");
		item2.setQuantity(1);
		item2.setUnit_price(price);
		items.add(item1);
		items.add(item2);
		Cart cart = new Cart();
		cart.setOwner("test");
		cart.setDeliveryaddress("test");
		cart.setItems(items);
		cart.setPrice(price);
		return cart;
	}

}
