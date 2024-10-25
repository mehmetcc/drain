package org.zulkarneyn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;


public class FilteredSymbols {
    private final List<String> symbols;

    public FilteredSymbols() {
        this.symbols = new ArrayList<>();
    }

    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(symbols);
    }

    public FilteredSymbols fromJson(final String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, FilteredSymbols.class);
    }
}
