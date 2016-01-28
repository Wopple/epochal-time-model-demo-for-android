package com.iheartradio.hack.epoch;

import com.annimon.stream.function.BiFunction;
import com.annimon.stream.function.Function;

public abstract class EpochCursor<Root, View> implements Identity<View> {
    private Identity<Root> mIdentity;

    public EpochCursor(final Identity<Root> identity) {
        mIdentity = identity;
    }

    protected abstract View get(final Root rootValue);

    protected abstract Root update(final Root rootValue, final View viewValue);

    @Override
    public View get() {
        return get(mIdentity.get());
    }

    @Override
    public View set(final View value) {
        return update(ignored -> value);
    }

    @Override
    public View update(final Function<View, View> function) {
        return update(function, (original, current) -> true);
    }

    @Override
    public View update(final Function<View, View> function, final BiFunction<View, View, Boolean> canUpdate) {
        return update(function, canUpdate, get());
    }

    @Override
    public View update(final Function<View, View> function, final BiFunction<View, View, Boolean> canUpdate, final View original) {
        return get(mIdentity.update(current -> update(current, function.apply(get(current))),
                                    (previous, current) -> canUpdate.apply(get(previous), get(current)),
                                    mIdentity.get()));
    }
}
