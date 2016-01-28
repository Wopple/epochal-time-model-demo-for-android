package com.iheartradio.hack.data;

public class Track {
    public final String title;
    public final String description;
    public final int image;

    public Track(final String title, final String description, final int image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Track track = (Track) o;

        if (image != track.image) return false;
        if (!title.equals(track.title)) return false;
        return description.equals(track.description);

    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + image;
        return result;
    }

    public Builder buildUpon() {
        return new Builder(this);
    }

    public static class Builder {
        private String mTitle;
        private String mDescription;
        private int mImage;

        public Builder() {
        }

        public Builder(final Track track) {
            mTitle = track.title;
            mDescription = track.description;
            mImage = track.image;
        }

        public Builder setTitle(final String title) {
            mTitle = title;
            return this;
        }

        public Builder setDescription(final String description) {
            mDescription = description;
            return this;
        }

        public Builder setImage(final int image) {
            mImage = image;
            return this;
        }

        public Track build() {
            return new Track(mTitle, mDescription, mImage);
        }
    }
}
