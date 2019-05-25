package org.meng.allitbooks;


public interface PageParser<T> {

    public T parsePage(String url);
}