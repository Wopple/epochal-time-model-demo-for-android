package com.iheartradio.hack.epoch;

import com.annimon.stream.function.BiFunction;
import com.annimon.stream.function.Function;

import java.util.concurrent.atomic.AtomicReference;

public class EpochIdentity<T> implements Identity<T> {
    private final AtomicReference<T> mIdentity;
    private final SpinLock<T> mSpinLock;

    public EpochIdentity(final T value) {
        mIdentity = new AtomicReference<>(value);
        mSpinLock = new SpinLock<>(mIdentity);
    }

    @Override
    public T get() {
        return mIdentity.get();
    }

    @Override
    public T set(final T value) {
        mIdentity.set(value);
        return value;
    }

    @Override
    public T update(final Function<T, T> function) {
        return update(function, (original, current) -> true);
    }

    @Override
    public T update(final Function<T, T> function, final BiFunction<T, T, Boolean> canUpdate) {
        return update(function, canUpdate, get());
    }

    @Override
    public T update(final Function<T, T> function, final BiFunction<T, T, Boolean> canUpdate, final T original) {
        return mSpinLock.update(function, canUpdate, original);
    }
}
