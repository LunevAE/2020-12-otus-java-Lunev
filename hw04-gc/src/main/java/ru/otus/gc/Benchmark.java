package ru.otus.gc;

import java.util.ArrayList;
import java.util.List;

public class Benchmark {
    private final int cnt;

    public Benchmark(int cnt) {
        this.cnt = cnt;
    }


    public void run() {
        List<String> list = new ArrayList<>();
        for (; ;) {
            for (int i = 0; i < cnt; i++) {
                list.add(String.valueOf(i));
            }
            for (int j = 0; j < list.size() / 2; j++) {
                list.set(j, null);
            }
        }
    }
}