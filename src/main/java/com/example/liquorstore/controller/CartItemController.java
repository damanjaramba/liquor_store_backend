package com.example.liquorstore.controller;

import com.example.liquorstore.entity.CartItem;
import com.example.liquorstore.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cart/api/v1")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @PostMapping("/addToCart")
    public ResponseEntity<?> addToCart(@RequestParam long liquorId, @RequestParam int quantity) {
        try {
            CartItem cartItem = cartItemService.addCartItem(liquorId, quantity);
            return new ResponseEntity<>(cartItem, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while adding to cart", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/removeFromCart/{cartItemId}")
    public ResponseEntity<?> removeFromCart(@PathVariable long cartItemId) {
        try {
            cartItemService.removeCartItem(cartItemId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while removing from cart", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getCartItems")
    public ResponseEntity<?> getCartItems() {
        try {
            return new ResponseEntity<>(cartItemService.getCartItems(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching cart items", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
