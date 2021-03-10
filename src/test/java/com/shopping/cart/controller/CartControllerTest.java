package com.shopping.cart.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.cart.entity.Cart;
import com.shopping.cart.entity.Item;
import com.shopping.cart.model.CartSearch;
import com.shopping.cart.model.Items;
import com.shopping.cart.repository.CartRepository;
import com.shopping.cart.service.CartService;
import com.shopping.cart.service.CartServiceException;
import com.shopping.cart.service.UtilsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class CartControllerTest {

	@Mock
	CartService cartService;

	@Mock
	UtilsService utilsService;

	@Mock
	CartRepository cartRepository;

	@InjectMocks
	CartController cartController;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(cartController).setControllerAdvice(CartExceptionController.class)
				.build();
	}

	@Test
	public void testAddCart_Success() throws Exception {
		Cart cart = mockCart();
		Mockito.when(cartService.addCart(Mockito.any(Cart.class))).thenReturn(cart);
		Mockito.doNothing().when(utilsService).addOrUpdateCart(Mockito.anyInt(), Mockito.any(LocalDateTime.class));
		mockMvc.perform(post("/cart/add-cart").content(getJsonString(cart)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.items[0].name").value("tv1"))
				.andExpect(jsonPath("$.items[1].name").value("tv2"));
	}

	@Test
	public void testAddCart_Failure() throws Exception {
		Cart cart = mockCart();
		CartServiceException cartServiceException = new CartServiceException("Internal error",
				HttpStatus.INTERNAL_SERVER_ERROR);
		Mockito.when(cartService.addCart(Mockito.any(Cart.class))).thenThrow(cartServiceException);
		Mockito.doNothing().when(utilsService).addOrUpdateCart(Mockito.anyInt(), Mockito.any(LocalDateTime.class));
		mockMvc.perform(post("/cart/add-cart").content(getJsonString(cart)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.code").value("500")).andExpect(jsonPath("$.message").value("Internal error"));
	}

	@Test
	public void testUpdateCart_Success() throws Exception {
		Cart cart = mockCart();
		Items items = new Items();
		items.setItems(cart.getItems());
		Mockito.when(utilsService.isValidCart(Mockito.anyInt())).thenReturn(true);
		Mockito.when(cartService.updateCart(Mockito.any(Items.class), Mockito.anyInt())).thenReturn(cart);
		mockMvc.perform(put("/cart/update-cart/1").content(getJsonString(items)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.items[0].name").value("tv1"))
				.andExpect(jsonPath("$.items[1].name").value("tv2"));
	}

	@Test
	public void testUpdateCart_expired_Success() throws Exception {
		Cart cart = mockCart();
		Items items = new Items();
		items.setItems(cart.getItems());
		Mockito.when(utilsService.isValidCart(Mockito.anyInt())).thenReturn(false);
		mockMvc.perform(put("/cart/update-cart/1").content(getJsonString(items)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isLocked())
				.andExpect(jsonPath("$.code").value("423"))
				.andExpect(jsonPath("$.message").value("Cart is expired. we cant update it"));
	}

	@Test
	public void testUpdateCart_failure() throws Exception {
		Cart cart = mockCart();
		Items items = new Items();
		items.setItems(cart.getItems());
		CartServiceException cartServiceException = new CartServiceException("Internal error",
				HttpStatus.INTERNAL_SERVER_ERROR);
		Mockito.when(utilsService.isValidCart(Mockito.anyInt())).thenReturn(true);
		Mockito.when(cartService.updateCart(Mockito.any(Items.class), Mockito.anyInt()))
				.thenThrow(cartServiceException);
		Mockito.doNothing().when(utilsService).addOrUpdateCart(Mockito.anyInt(), Mockito.any(LocalDateTime.class));
		mockMvc.perform(put("/cart/update-cart/1").content(getJsonString(items)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.code").value("500")).andExpect(jsonPath("$.message").value("Internal error"));
	}

	@Test
	public void testDeleteCart_Success() throws Exception {
		Mockito.when(utilsService.isValidCart(Mockito.anyInt())).thenReturn(true);
		Mockito.doNothing().when(cartService).deleteCart(Mockito.anyInt());
		mockMvc.perform(delete("/cart/delete-cart/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testDeleteCart_expired_Success() throws Exception {
		Mockito.when(utilsService.isValidCart(Mockito.anyInt())).thenReturn(false);
		mockMvc.perform(delete("/cart/delete-cart/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isLocked())
				.andExpect(jsonPath("$.code").value("423"))
				.andExpect(jsonPath("$.message").value("Cart is expired. we cant update it"));
	}

	@Test
	public void testDeleteCart_failure() throws Exception {
		CartServiceException cartServiceException = new CartServiceException("Internal error",
				HttpStatus.INTERNAL_SERVER_ERROR);
		Mockito.when(utilsService.isValidCart(Mockito.anyInt())).thenReturn(true);
		Mockito.doThrow(cartServiceException).when(cartService).deleteCart(Mockito.anyInt());
		mockMvc.perform(delete("/cart/delete-cart/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.code").value("500")).andExpect(jsonPath("$.message").value("Internal error"));
	}

	@Test
	public void testSearchCart_Success() throws Exception {
		List<Cart> list = new ArrayList<Cart>();
		list.add(mockCart());
		Mockito.when(cartService.searchCart(Mockito.any(CartSearch.class))).thenReturn(list);
		Mockito.when(utilsService.isValidCart(Mockito.anyInt())).thenReturn(true);
		mockMvc.perform(get("/cart/search-cart?owner=neel&address=testAddress&status=active")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testSearchCart_empty_Success() throws Exception {
		Mockito.when(cartService.searchCart(Mockito.any(CartSearch.class))).thenReturn(null);
		mockMvc.perform(get("/cart/search-cart?owner=neel&address=testAddress&status=active")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$.code").value("404"));
	}

	@Test
	public void testSearchCart_failure() throws Exception {
		CartServiceException cartServiceException = new CartServiceException("Internal error",
				HttpStatus.INTERNAL_SERVER_ERROR);
		Mockito.when(cartService.searchCart(Mockito.any(CartSearch.class))).thenThrow(cartServiceException);
		mockMvc.perform(get("/cart/search-cart?owner=neel&address=testAddress&status=active")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError()).andExpect(jsonPath("$.code").value("500"))
				.andExpect(jsonPath("$.message").value("Internal error"));
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

	private String getJsonString(Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
