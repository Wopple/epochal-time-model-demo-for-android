package com.iheartradio.hack.data;

public class Station {
    public final String name;
    public final int image;

    public Station(final String name, final int image) {
        this.name = name;
        this.image = image;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Station station = (Station) o;

        if (image != station.image) return false;
        return name.equals(station.name);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + image;
        return result;
    }

    public Builder buildUpon() {
        return new Builder(this);
    }

    public static class Builder {
        private String mName;
        private int mImage;

        public Builder() {
        }

        public Builder(final Station station) {
            mName = station.name;
            mImage = station.image;
        }

        public Builder setName(final String name) {
            mName = name;
            return this;
        }

        public Builder setImage(final int image) {
            mImage = image;
            return this;
        }

        public Station build() {
            return new Station(mName, mImage);
        }
    }
}
