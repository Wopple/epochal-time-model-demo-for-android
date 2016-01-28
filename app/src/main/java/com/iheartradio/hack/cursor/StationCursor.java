package com.iheartradio.hack.cursor;

import com.iheartradio.hack.data.PlayerState;
import com.iheartradio.hack.data.Station;
import com.iheartradio.hack.epoch.EpochCursor;
import com.iheartradio.hack.epoch.Identity;

public class StationCursor extends EpochCursor<PlayerState, Station> {
    public StationCursor(final Identity<PlayerState> identity) {
        super(identity);
    }

    @Override
    protected Station get(final PlayerState rootValue) {
        return rootValue.station;
    }

    @Override
    protected PlayerState update(final PlayerState state, final Station station) {
        return state.buildUpon().setStation(station).build();
    }
}
