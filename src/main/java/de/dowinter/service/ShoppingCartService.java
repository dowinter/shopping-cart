/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.dowinter.service;

import de.dowinter.data.ShoppingCartRepository;
import de.dowinter.model.ShoppingCart;
import de.dowinter.model.StockReservation;
import de.dowinter.rest.client.InventoryService;
import de.dowinter.rest.representations.InventoryItem;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;

@RequestScoped
public class ShoppingCartService {
    @Inject
    ShoppingCartRepository shoppingCarts;

    // This is no longer the service implementation, but a REST-Client which calls another microservice
    @Inject
    InventoryService inventoryService;

    public ShoppingCart createShoppingCart() {
        ShoppingCart cart = new ShoppingCart();
        shoppingCarts.save(cart);

        return cart;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public boolean reserveItem(ShoppingCart cart, long itemId, int count) {
        InventoryItem item;
        try { // In case the Item doesnt exist, we get a WebApplicationException (Status != 2xx)
            item = inventoryService.getSingleItem(itemId);
        } catch (WebApplicationException exception) {
            return false;
        }
        
        if (!inventoryService.getAvailablity(item.getId(), count).isAvailable()) {
            return false;
        }

        inventoryService.removeFromInventory(item.getId(), count);
        shoppingCarts.addReservation(cart, new StockReservation().withItem(item.getId()).withReservedCount(count));

        return true;
    }

    public Optional<ShoppingCart> getShoppingCart(long shoppingCartId) {
        return shoppingCarts.findById(shoppingCartId);
    }
}
