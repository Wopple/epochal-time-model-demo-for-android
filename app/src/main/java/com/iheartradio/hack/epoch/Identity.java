package com.iheartradio.hack.epoch;

import com.annimon.stream.function.BiFunction;
import com.annimon.stream.function.Function;

public interface Identity<T> {
    T get();
    T set(final T value);
    T update(final Function<T, T> function);
    T update(final Function<T, T> function, final BiFunction<T, T, Boolean> canUpdate);
    T update(final Function<T, T> function, final BiFunction<T, T, Boolean> canUpdate, final T original);
}
