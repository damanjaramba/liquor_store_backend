package com.example.liquorstore.service;

import com.example.liquorstore.entity.CartItem;
import com.example.liquorstore.entity.Liquor;
import com.example.liquorstore.entity.User;
import com.example.liquorstore.repository.CartItemRepository;
import com.example.liquorstore.repository.LiquorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private LiquorRepository liquorRepository;

    @Autowired
    SessionUserService sessionUserService;

    public CartItem addCartItem(Long liquorId, int quantity) {
        User sessionUser = sessionUserService.getSessionUser();

        Liquor liquor = liquorRepository.findById(liquorId)
                .orElseThrow(() -> new RuntimeException("Liquor not found"));

        CartItem cartItem = new CartItem();
        cartItem.setUser(sessionUser);
        cartItem.setLiquors(List.of(liquor));
        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(liquor.getPrice().multiply(BigDecimal.valueOf(quantity)));
        return cartItemRepository.save(cartItem);
    }

    public void removeCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
    public List<CartItem> getCartItems() {
        User sessionUser = sessionUserService.getSessionUser();
        return cartItemRepository.findByUser(sessionUser);
    }
}
