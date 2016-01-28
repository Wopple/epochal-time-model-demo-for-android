package com.iheartradio.hack.data;

public class PlayerState {
    public final Station station;
    public final Track track;
    public final boolean isPlaying;

    public PlayerState(final Station station, final Track track, final boolean isPlaying) {
        this.station = station;
        this.track = track;
        this.isPlaying = isPlaying;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final PlayerState that = (PlayerState) o;

        if (isPlaying != that.isPlaying) return false;
        if (!station.equals(that.station)) return false;
        return track.equals(that.track);

    }

    @Override
    public int hashCode() {
        int result = station.hashCode();
        result = 31 * result + track.hashCode();
        result = 31 * result + (isPlaying ? 1 : 0);
        return result;
    }

    public Builder buildUpon() {
        return new Builder(this);
    }

    public static class Builder {
        private Station mStation;
        private Track mTrack;
        private boolean mIsPlaying;

        public Builder() {
        }

        public Builder(final PlayerState state) {
            mStation = state.station;
            mTrack = state.track;
            mIsPlaying = state.isPlaying;
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
            return new PlayerState(mStation, mTrack, mIsPlaying);
        }
    }
}
