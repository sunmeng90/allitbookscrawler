package org.meng.allitbooks;

import org.junit.Test;

public class DetailPageParserTest {

    @Test
    public void parsePage() {
        DetailPageParser pageParser = new DetailPageParser();
        pageParser.parsePage("http://www.allitebooks.com/next-generation-databases-nosql-newsql-and-big-data/");
    }
}