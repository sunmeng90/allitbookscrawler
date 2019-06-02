package org.meng.allitbooks;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

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

    @Test
    public void parsePage2() throws ExecutionException, InterruptedException {
        IndexPageParser pageParser = new IndexPageParser();
        FutureTask<List<String>> future = new FutureTask<>(() -> {
            List<String> urls = pageParser.parsePage("http://www.allitebooks.com/datebases/big-data/page/2/");
            return urls;
        });
        new Thread(future).start();
        Thread.sleep(1000);
        future.cancel(true);
        System.out.println(future.get());
    }
}