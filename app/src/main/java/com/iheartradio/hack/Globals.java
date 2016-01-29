package com.iheartradio.hack;

import com.annimon.stream.function.Function;
import com.annimon.stream.function.Supplier;
import com.iheartradio.hack.cursor.StationCursor;
import com.iheartradio.hack.data.PlayerState;
import com.iheartradio.hack.data.Station;
import com.iheartradio.hack.data.Track;
import com.iheartradio.hack.epoch.EpochIdentity;
import com.iheartradio.hack.epoch.Identity;
import com.iheartradio.hack.epoch.R;

import java.util.Arrays;
import java.util.List;

public class Globals {
    public static final Track NO_TRACK = new Track.Builder()
            .setTitle("")
            .setDescription("")
            .setImage(0)
            .build();

    public static final Track ROAR_TRACK = new Track.Builder()
            .setTitle("Roar")
            .setDescription("Meow?")
            .setImage(R.drawable.roar)
            .build();

    public static final Track GRENADE_TRACK = new Track.Builder()
            .setTitle("Grenade")
            .setDescription("Gonna explode!")
            .setImage(R.drawable.grenade)
            .build();

    public static final Station NO_STATION = new Station.Builder()
            .setName("")
            .setImage(0)
            .build();

    public static final Station KTU_STATION = new Station.Builder()
            .setName("KTU")
            .setImage(R.drawable.ktu)
            .build();

    public static final Station KATY_PERRY_STATION = new Station.Builder()
            .setName("Katy Perry Radio")
            .setImage(R.drawable.perry)
            .build();

    public static final Station BRUNO_MARS_STATION = new Station.Builder()
            .setName("Bruno Mars Radio")
            .setImage(R.drawable.mars)
            .build();

    public static final PlayerState DEFAULT_STATE = new PlayerState.Builder()
            .setStation(NO_STATION)
            .setIsPlaying(false)
            .setTrack(NO_TRACK)
            .build();

    public static final Identity<PlayerState> appState = new EpochIdentity<>(DEFAULT_STATE);
    public static final Identity<PlayerState> castState = new EpochIdentity<>(DEFAULT_STATE);

    public static final Identity<Station> appStation = new StationCursor(appState);
    public static final Identity<Station> appTrack = new StationCursor(appState);

    public static Supplier<Long> delaySupplier = () -> 0L;

    public static final Function<Track, Track> nextTrack = track -> {
        List<Track> tracks = Arrays.asList(GRENADE_TRACK, ROAR_TRACK);
        return tracks.get((tracks.indexOf(track) + 1) % tracks.size());
    };

    public static final Function<Station, Station> nextStation = station -> {
        List<Station> stations = Arrays.asList(KTU_STATION, BRUNO_MARS_STATION, KATY_PERRY_STATION);
        return stations.get((stations.indexOf(station) + 1) % stations.size());
    };

    public static final Function<Station, Track> firstTrack = station -> {
        if (station == KTU_STATION) {
            return GRENADE_TRACK;
        } else if (station == BRUNO_MARS_STATION) {
            return GRENADE_TRACK;
        } else if (station == KATY_PERRY_STATION) {
            return ROAR_TRACK;
        } else {
            return ROAR_TRACK;
        }
    };
}
