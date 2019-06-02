package org.meng.allitbooks;

import lombok.extern.slf4j.Slf4j;
import org.meng.allitbooks.task.CancellableTaskExecutor;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class AllITBooksCrawler {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> indexLinkQueue = new LinkedBlockingQueue<>(5);
        BlockingQueue<String> detailLinkQueue = new LinkedBlockingQueue<>();
        BlockingQueue<String> pdfLinkQueue = new LinkedBlockingQueue<>();
        IndexPageParser indexPageParser = new IndexPageParser();
        DetailPageParser detailPageParser = new DetailPageParser();
        new Thread(() -> {
            IndexPageSupplier indexPageSupplier = new IndexPageSupplier("http://www.allitebooks.com/page/", 1, 15);
            List<String> indexUrls = indexPageSupplier.get();
            indexUrls.add("N/A");
            indexUrls.forEach(indexUrl -> {
                try {
                    indexLinkQueue.put(indexUrl);
                } catch (InterruptedException e) {
                    log.error("put index url to outputQueue interrupted");
                }
            });
        }).start();
        ThreadPoolPageExecutor indexPageExecutor = new ThreadPoolPageExecutor(indexPageParser, indexLinkQueue, detailLinkQueue, 5);
        ThreadPoolPageExecutor detailPageExecutor = new ThreadPoolPageExecutor(detailPageParser, detailLinkQueue, pdfLinkQueue, 3);
        CancellableTaskExecutor cancellableIndexTaskExecutor = new CancellableTaskExecutor(indexPageExecutor);
        CancellableTaskExecutor cancellableDetailTaskExecutor = new CancellableTaskExecutor(detailPageExecutor);
        FutureTask<Object> indexCancellableTask = cancellableIndexTaskExecutor.newTask();
        new Thread(indexCancellableTask).start();
        FutureTask<Object> detailCancellableTask = cancellableDetailTaskExecutor.newTask();
        new Thread(detailCancellableTask).start();
        Thread.sleep(60 * 1000);
        indexCancellableTask.cancel(true);
        detailCancellableTask.cancel(true);
    }


}
