package org.zulkarneyn;

import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@ToString
public class FilteredSymbols {
    public Set<String> symbols;

    public FilteredSymbols() {
        this.symbols = new HashSet<>();
    }

    public FilteredSymbols add(String value) {
        symbols.add(value);
        return this;
    }

    public static FilteredSymbols empty() {
        return new FilteredSymbols();
    }

    public FilteredSymbols add(Set<String> values) {
        symbols.addAll(values);
        return this;
    }
}
