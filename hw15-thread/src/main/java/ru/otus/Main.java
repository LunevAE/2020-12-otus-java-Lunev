package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
    private String lastThreadName = "Thread2";

    public static void main(String[] args) {
        new Main().printNumbers();
    }

    public void printNumbers() {
        final List<Thread> threads = List.of(
                new Thread(() -> numbers.forEach(this::log), "Thread1"),
                new Thread(() -> numbers.forEach(this::log), "Thread2")
        );

        threads.forEach(Thread::start);
    }

    private synchronized void log(final int number) {
        final Thread currentThread = Thread.currentThread();
        final String currentThreadName = currentThread.getName();

        while (lastThreadName.equals(currentThreadName)) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                currentThread.interrupt();
            }
        }

        lastThreadName = currentThreadName;
        logger.info("thread name: {}, number : {}", currentThread.getName(), number);
        notifyAll();
    }
}