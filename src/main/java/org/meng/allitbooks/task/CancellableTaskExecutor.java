package org.meng.allitbooks.task;

import lombok.extern.slf4j.Slf4j;
import org.meng.allitbooks.ThreadPoolPageExecutor;

import java.util.concurrent.FutureTask;

@Slf4j
public class CancellableTaskExecutor implements CancellableTask<Object> {

    ThreadPoolPageExecutor threadPoolPageExecutor;

    public CancellableTaskExecutor(ThreadPoolPageExecutor threadPoolPageExecutor) {
        this.threadPoolPageExecutor = threadPoolPageExecutor;
    }

    /**
     * when cancel then shutdown the thread pool
     */
    @Override
    public void cancel() {
        try {
            threadPoolPageExecutor.stop();
        } catch (Exception e) {
            log.error("Failed to stop executor");
        }
    }

    @Override
    public Object call() throws Exception {
        log.info("Start running executor in thread {}", Thread.currentThread().getName());
        threadPoolPageExecutor.start();
        return null;
    }

    public FutureTask<Object> newTask() {
        return new FutureTask<Object>(this) {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                log.info("Start cancel executor in thread {}", Thread.currentThread().getName());
                try {
                    CancellableTaskExecutor.this.cancel();
                } finally {
                    return super.cancel(mayInterruptIfRunning);//signal interrupt to task runner thread
                }
            }
        };
    }
}