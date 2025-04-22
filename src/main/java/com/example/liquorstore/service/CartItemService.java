package com.example.liquorstore.service;

import com.example.liquorstore.entity.CartItem;
import com.example.liquorstore.repository.CartItemRepository;
import com.example.liquorstore.repository.LiquorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private LiquorRepository liquorRepository;

//    public CartItem addCartItem(Long userId, Long liquorId, int quantity) {
//        Liquor liquor = liquorRepository.findById(liquorId)
//                .orElseThrow(() -> new RuntimeException("Liquor not found"));
//
//        CartItem cartItem = new CartItem();
//        cartItem.setUser(new User(userId)); // Assuming User is fetched elsewhere
//        cartItem.setLiquors(List.of(liquor));
//        cartItem.setQuantity(quantity);
//        cartItem.updateTotalPrice();
//
//        return cartItemRepository.save(cartItem);
//    }

    public void removeCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}
