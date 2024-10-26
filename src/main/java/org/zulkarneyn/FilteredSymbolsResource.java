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
    public Uni<Response> getSymbols() {
        return service.get()
                .flatMap(this::success)
                .onFailure()
                .recoverWithUni(failure -> failure("redis: get operation failed."));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> postSymbols(FilteredSymbols symbols) {
        return service.set(symbols).flatMap(object -> {
            if (!object) return failure("redis: set operation failed.");

            return Uni.createFrom().item(Response.ok(Boolean.TRUE).build());
        });
    }

    private Uni<Response> success(FilteredSymbols symbols) {
        return Uni.createFrom()
                .item(Response.ok(symbols).build());
    }

    private Uni<Response> failure(String message) {
        return Uni.createFrom()
                .item(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message).build())
                .log();
    }
}
