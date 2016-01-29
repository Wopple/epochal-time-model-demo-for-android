package com.iheartradio.hack.cast;

import android.os.Handler;
import android.os.HandlerThread;

import com.iheartradio.hack.data.PlayerState;
import com.iheartradio.hack.data.Station;
import com.iheartradio.hack.data.Track;
import com.iheartradio.hack.time.SteadyTimer;

import java.util.concurrent.TimeUnit;

import rx.android.schedulers.HandlerScheduler;

import static com.iheartradio.hack.Globals.*;

public class CastThread {
    private static final long TIMER_RATE = TimeUnit.SECONDS.toMillis(5);

    private static HandlerThread sThread;
    private static SteadyTimer sNextTimer;

    public static void start() {
        if (sThread == null) {
            sThread = new HandlerThread("Cast Thread");
            sThread.start();

            CastPipe.getCast()
                    .observeOn(HandlerScheduler.from(new Handler(sThread.getLooper())))
                    .subscribe(CastThread::handle);
        }
    }

    private static void handle(Message message) {
        switch (message.type) {
            case NEXT:
                if (castState.get().station != NO_STATION) {
                    Track prevTrack = (Track) message.payload;
                    Track next = nextTrack.apply(prevTrack);
                    PlayerState nextState = castState.update(state -> state.buildUpon().setTrack(next).build(),
                                                             (previous, current) -> current.track.equals(prevTrack));
                    process(nextState);

                    if (nextState.isPlaying) {
                        startTimer();
                    }
                }
                break;
            case PLAY:
                process(castState.update(state -> state.buildUpon().setIsPlaying(true).build()));
                startTimer();
                break;
            case STOP:
                process(castState.update(state -> state.buildUpon().setIsPlaying(false).build()));
                stopTimer();
                break;
            case SET_STATION:
                Station station = (Station) message.payload;
                Track first = firstTrack.apply(station);

                process(castState.update(state -> state.buildUpon()
                        .setStation(station)
                        .setTrack(first)
                        .setIsPlaying(true)
                        .build()));

                startTimer();
                break;
            case SYNC:
                render(castState.update(state -> (PlayerState) message.payload));
                break;
        }
    }

    private static void process(PlayerState playerState) {
        render(playerState);
        sync(playerState);
    }

    private static void render(final PlayerState playerState) {
        CastPipe.sendToAppNow(new Message(MessageType.RENDER_CAST, playerState));
    }

    private static void sync(final PlayerState playerState) {
        CastPipe.sendToApp(new Message(MessageType.SYNC, playerState));
    }

    private static void startTimer() {
        stopTimer();
        sNextTimer = new SteadyTimer.Builder()
                .withDelay(TIMER_RATE)
                .withRate(TIMER_RATE)
                .withTask(() -> {
                    if (castState.get().isPlaying) {
                        CastPipe.sendToCast(new Message(MessageType.NEXT, castState.get().track));
                    }
                })
                .build();
    }

    private static void stopTimer() {
        if (sNextTimer != null) {
            sNextTimer.stop();
            sNextTimer = null;
        }
    }
}
