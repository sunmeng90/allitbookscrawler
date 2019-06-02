package org.meng.allitbooks.task;

import java.util.concurrent.Callable;

/**
 * A task than can be cancel
 * @param <T>
 */
public interface CancellableTask<T> extends Callable<T> {
    void cancel();
}