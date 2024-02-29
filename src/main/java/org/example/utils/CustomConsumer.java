package org.example.utils;

@FunctionalInterface
public interface CustomConsumer<T, R> {
    void accept(T t1, R t2);
}
