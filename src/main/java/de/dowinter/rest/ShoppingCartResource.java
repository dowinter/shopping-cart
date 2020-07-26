package de.dowinter.rest;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import de.dowinter.model.ShoppingCart;
import de.dowinter.rest.representations.ErrorResult;
import de.dowinter.rest.representations.ReservationRequest;
import de.dowinter.service.ShoppingCartService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/cart")
public class ShoppingCartResource {
    @Inject
    ShoppingCartService shoppingCartService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createShoppingCart() throws URISyntaxException {
        ShoppingCart createdCart = shoppingCartService.createShoppingCart();
        return Response.created(new URI("/cart/" + createdCart.getId())).entity(createdCart).build();
    }

    @PUT
    @Path("/{cartId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response putItemIntoCart(@PathParam("cartId") long cartId, ReservationRequest reservationRequest) {
        Optional<ShoppingCart> shoppingCart = shoppingCartService.getShoppingCart(cartId);

        if (shoppingCart.isPresent()) {
            boolean success = shoppingCartService.reserveItem(shoppingCart.get(),
                    reservationRequest.getItemId(),
                    reservationRequest.getReservationCount());

            if (success) {
                return Response.ok().build();
            } else {
                return Response.status(NOT_FOUND).build();
            }
        } else {
            return Response.status(NOT_FOUND).entity(new ErrorResult("Shopping cart not found.")).build();
        }
    }

    @GET
    @Path("/{cartId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getShoppingCart(@PathParam("cartId") long cartId) {
        Optional<ShoppingCart> cart = shoppingCartService.getShoppingCart(cartId);
        if (cart.isPresent()) {
            return Response.ok(cart).build();
        } else {
            return Response.status(NOT_FOUND).build();
        }
    }

}
