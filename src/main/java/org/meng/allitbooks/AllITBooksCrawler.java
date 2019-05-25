package org.meng.allitbooks;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AllITBooksCrawler {
//    private static ExecutorService pool = Executors.newFixedThreadPool(5);


    public static void main(String[] args) {
        IndexPageSupplier supplier = new IndexPageSupplier("http://www.allitebooks.com/page/", 2, 3);
        List<String> a = supplier.get();
        System.out.println(a);
        supplier.get().stream().forEach(System.out::println);
        List<String> pdfURLs = supplier.get().stream()
                .flatMap(indexURL -> Optional.ofNullable((new IndexPageParser()).parsePage(indexURL))
                        .map(urls -> urls.stream())
                        .orElseGet(Stream::empty))
                .map(pageURL -> new DetailPageParser().parsePage(pageURL))
                .collect(Collectors.toList());

        System.out.println(pdfURLs);
    }


}
