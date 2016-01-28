package com.iheartradio.hack.epoch;

import com.annimon.stream.function.BiFunction;
import com.annimon.stream.function.Function;

import java.util.concurrent.atomic.AtomicReference;

public class SpinLock<T> {
    private final AtomicReference<T> mIdentity;

    public SpinLock(final AtomicReference<T> identity) {
        mIdentity = identity;
    }

    public T update(final Function<T, T> function, final BiFunction<T, T, Boolean> canUpdate, final T original) {
        T value = original;

        while (canUpdate.apply(original, value)) {
            final T updatedValue = function.apply(value);

            if (mIdentity.compareAndSet(value, updatedValue)) {
                return updatedValue;
            } else {
                value = mIdentity.get();
            }
        }

        return null;
    }
}
