package org.meng.allitbooks;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IndexPageSupplier implements Supplier<List<String>> {
    private String BASE;

    private int start;
    private int end;

    public IndexPageSupplier(String BASE, int start, int end) {
        this.BASE = BASE;
        this.start = start;
        this.end = end;
    }

    @Override
    public List<String> get() {
        return IntStream.rangeClosed(start, end)
                .mapToObj(i -> BASE + i)
                .collect(Collectors.toList());
    }
}
