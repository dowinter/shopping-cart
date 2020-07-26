package de.dowinter.rest.client;

import de.dowinter.rest.representations.AvailabilityResult;
import de.dowinter.rest.representations.InventoryItem;
import java.util.List;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/inventory/items")
public interface InventoryService {

    @GET
    @Path("/{itemId}/availability")
    @Produces(MediaType.APPLICATION_JSON)
    public AvailabilityResult getAvailablity(@PathParam("itemId") long itemId,
                                             @QueryParam("count") int count);
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<InventoryItem> getAllItems();

    @GET
    @Path("/{itemId}")
    @Produces(MediaType.APPLICATION_JSON)
    public InventoryItem getSingleItem(@PathParam("itemId") long itemId);

    @POST
    @Path("/{itemId}/decrease")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional(Transactional.TxType.REQUIRED)
    public Response removeFromInventory(@PathParam("itemId") long itemId, @QueryParam("count") int count);
}
