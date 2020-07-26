package de.dowinter.rest.client;

import java.net.URI;
import javax.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

public class InventoryServiceProducer {

    @ConfigProperty(name = "inventory.base.uri")
    URI baseUri;

    @Produces
    InventoryService produceServiceClient() {
        return RestClientBuilder.newBuilder().baseUri(baseUri).build(InventoryService.class);
    }
}
