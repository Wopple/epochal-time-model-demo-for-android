package com.iheartradio.hack.cursor;

import com.iheartradio.hack.data.PlayerState;
import com.iheartradio.hack.data.Track;
import com.iheartradio.hack.epoch.EpochCursor;
import com.iheartradio.hack.epoch.Identity;

public class TrackCursor extends EpochCursor<PlayerState, Track> {
    public TrackCursor(final Identity<PlayerState> identity) {
        super(identity);
    }

    @Override
    protected Track get(final PlayerState rootValue) {
        return rootValue.track;
    }

    @Override
    protected PlayerState update(final PlayerState state, final Track track) {
        return state.buildUpon().setTrack(track).build();
    }
}
