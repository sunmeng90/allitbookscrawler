package org.meng.allitbooks;


import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class IndexPageSupplierTest {

    @Test
    public void testGenerateURLForSite() {
        String base = "http://www.allitebooks.com/page/";
        IndexPageSupplier supplier = new IndexPageSupplier(base, 1, 100);
        List<String> pageUrls = supplier.get();
        assertTrue(pageUrls.size() > 0);
        assertEquals(100, pageUrls.size());
        assertTrue(pageUrls.contains(base + 1));
        assertFalse(pageUrls.contains(base + 101));
        assertFalse(pageUrls.contains(base + (-1)));
    }

}