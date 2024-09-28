package be.zsoft.system.monitor.services.utils;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FixedSizeList<T> {

    private final List<T> data;
    private final int size;

    public FixedSizeList(int size) {
        this(size, null);
    }

    public FixedSizeList(int size, T defaultValue) {
        this.size = size;
        data = new ArrayList<T>(size);

        for (int i = 0; i < size; i++) {
            data.add(defaultValue);
        }
    }

    public void add(T value) {
        data.add(value);

        if (data.size() > size) {
            data.removeFirst();
        }
    }

    public T get(int index) {
        return data.get(index);
    }
}
