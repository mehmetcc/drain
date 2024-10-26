package org.zulkarneyn;


import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/api/v1/symbols")
public class FilteredSymbolsResource {
    @Inject
    private FilteredSymbolsService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<FilteredSymbols> getSymbols() {
        return service.get();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Boolean> postSymbols(FilteredSymbols symbols) {
        return service.set(symbols);
    }
}
