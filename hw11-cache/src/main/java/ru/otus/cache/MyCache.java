package ru.otus.cache;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {

    private final Map<K, V> cache = new WeakHashMap<>();

    private final List<WeakReference<HwListener<K, V>>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notify(key,value, "saved in storage");
    }

    @Override
    public void remove(K key) {
        var removedValue = cache.remove(key);
        notify(key, removedValue, "removed from storage");
    }

    @Override
    public V get(K key) {
        var value = cache.get(key);
        notify(key, value, "got from storage");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.removeIf(it -> listener == it.get());
    }

    private void notify(K key, V value, String action) {
        var iterator = listeners.iterator();

        while (iterator.hasNext()) {
            var reference = iterator.next();
            var ref = reference.get();
            if (ref != null) {
                ref.notify(key, value, action);
            } else {
                iterator.remove();
            }
        }
    }
}