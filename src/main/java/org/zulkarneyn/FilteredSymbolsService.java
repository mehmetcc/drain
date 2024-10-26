package org.zulkarneyn;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.value.ReactiveValueCommands;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.logging.Filter;

@Slf4j
@ApplicationScoped
public class FilteredSymbolsService {
    private static final String KEY = "zulkarneyn:drain:filtered_symbols";

    private final ReactiveValueCommands<String, String> commands;

    public FilteredSymbolsService(ReactiveRedisDataSource source) {
        commands = source.value(String.class);
    }

    public Uni<FilteredSymbols> get() {
        return commands.get(KEY)
                .flatMap(this::symbols);
    }

    public Uni<Boolean> set(FilteredSymbols symbols) {
        return get()
                .map(object -> object.add(symbols.symbols))
                .flatMap(object -> json(object).flatMap(parsed -> commands.set(KEY, parsed)))
                .onItem()
                .transform(object -> Boolean.TRUE)
                .onFailure()
                .invoke(this::log);
    }

    private Uni<String> json(FilteredSymbols symbols) {
        return Uni.createFrom()
                .item(symbols)
                .onItem()
                .transform(object -> Unchecked.supplier(() -> new ObjectMapper().writeValueAsString(object)).get());
    }

    private Uni<FilteredSymbols> symbols(String json) {
        return Uni.createFrom()
                .item(json)
                .onItem()
                .transform(object -> Unchecked.supplier(() -> new ObjectMapper().readValue(object, FilteredSymbols.class)).get());
    }

    private Uni<Boolean> log(Throwable failure) {
        log.error(failure.getMessage());
        return Uni.createFrom().item(Boolean.FALSE);
    }
}
