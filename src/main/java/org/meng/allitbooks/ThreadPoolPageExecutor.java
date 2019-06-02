package org.meng.allitbooks;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * ThreadPoolPageExecutor plays two roles
 * Consumer: consume index page url from inputQueue
 * Producer: produce book link and add to outputQueue
 */
@Slf4j
public class ThreadPoolPageExecutor<T> {
    private PageParser<T> indexPageParser;
    private BlockingQueue<String> inputQueue;
    private BlockingQueue<String> outputQueue;
    private Semaphore semaphore;//concurrent task number
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public ThreadPoolPageExecutor(PageParser<T> indexPageParser, BlockingQueue<String> inputQueue,
                                  BlockingQueue<String> outputQueue, int semaphore) {
        this.indexPageParser = indexPageParser;
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
        this.semaphore = new Semaphore(semaphore == 0 ? 5 : semaphore);
    }

    public void stop() throws InterruptedException {
        executorService.shutdownNow();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        log.info("current thread {} will interrupt", Thread.currentThread().getName());
        print();
    }


    public void start() throws InterruptedException {
            log.info("Start pool executor in thread {}", Thread.currentThread().getName());
            while (true) {
                final String inputUrl = consume();
                if (StringUtils.isBlank(inputUrl)) {
                    continue;
                } else if (Objects.equals("N/A", inputUrl)) {
                    break;
                }
                try {
                    submitTask(inputUrl, indexPageParser::parsePage);
                } catch (InterruptedException e) {
                    log.error("Task execution failed", e);
                }
            }
    }

    private Future<T> submitTask(String inputUrl, Function<String, T> task) throws InterruptedException {
        semaphore.acquire();
        try {
            return executorService.submit(() -> {
                try {
                    T outputUrls = task.apply(inputUrl);
                    handleResult(outputUrls);
                    return outputUrls;
                } catch (InterruptedException e) {
                    log.error("put page url to outputQueue interrupted", e);
                } finally {
                    semaphore.release();
                }
                return null;
            });
        } catch (RejectedExecutionException e) {
            semaphore.release();
        }
        return null;
    }

    private void handleResult(T outputUrls) throws InterruptedException {
        log.info("Parsing result: {}", outputUrls);
        if (outputUrls == null) {
            return;
        }
        if (outputUrls instanceof Collection) {
            for (Object outputUrl : (Collection<?>) outputUrls) {
                outputQueue.put(outputUrl.toString());
            }
        } else {
            outputQueue.put((String) outputUrls);
        }
    }

    private String consume() throws InterruptedException {
        try {
            return inputQueue.take();
        } catch (InterruptedException e) {
            log.error("Consume url interrupted", e);
            throw e;
        }
    }

    public void print() {
        log.info("downloadLink:{}", outputQueue.toString());
    }
}


