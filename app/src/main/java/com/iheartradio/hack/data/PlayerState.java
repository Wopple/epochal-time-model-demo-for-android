package com.iheartradio.hack.data;

public class PlayerState {
    public final Station station;
    public final Track track;
    public final boolean isPlaying;
    public final long millis;

    public PlayerState(final Station station, final Track track, final boolean isPlaying, final long millis) {
        this.station = station;
        this.track = track;
        this.isPlaying = isPlaying;
        this.millis = millis;
    }

    public Builder buildUpon() {
        return new Builder(this);
    }

    public static class Builder {
        private Station mStation;
        private Track mTrack;
        private boolean mIsPlaying;
        private long mMillis;

        public Builder() {
        }

        public Builder(final PlayerState state) {
            mStation = state.station;
            mTrack = state.track;
            mIsPlaying = state.isPlaying;
            mMillis = state.millis;
        }

        public Builder setStation(final Station station) {
            mStation = station;
            return this;
        }

        public Builder setTrack(final Track track) {
            mTrack = track;
            return this;
        }

        public Builder setIsPlaying(final boolean isPlaying) {
            mIsPlaying = isPlaying;
            return this;
        }

        public PlayerState build() {
            return new PlayerState(mStation, mTrack, mIsPlaying, mMillis);
        }
    }
}
