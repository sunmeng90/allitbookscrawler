package org.meng.allitbooks;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class IndexPageParserTest {

    @Test
    public void parsePage() {
        IndexPageParser pageParser = new IndexPageParser();
        assertTrue(pageParser.parsePage(null).size() == 0);
        assertTrue(pageParser.parsePage("").size() == 0);
        List<String> urls = pageParser.parsePage("http://www.allitebooks.com/datebases/big-data/page/2/");
        assertTrue(urls.size() > 0);
    }
}