package com.raf.transactionalcore.listener;

public interface TransactionListener<T> {

    void handleEvent(T event);

    void handleAfterRollback(T event);

    void handleAfterCompletion(T event);
}
