package com.iheartradio.hack.cast;

import android.os.Handler;
import android.os.HandlerThread;

import com.iheartradio.hack.data.PlayerState;
import com.iheartradio.hack.data.Station;
import com.iheartradio.hack.data.Track;

import rx.android.schedulers.HandlerScheduler;

import static com.iheartradio.hack.Globals.*;

public class CastThread {
    private static HandlerThread sThread;

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
                break;
            case PLAY:
                render(castState.update(state -> state.buildUpon().setIsPlaying(true).build()));
                break;
            case STOP:
                render(castState.update(state -> state.buildUpon().setIsPlaying(false).build()));
                break;
            case SYNC:
                render(castState.update(state -> (PlayerState) message.payload));
                break;
            case SET_STATION:
                render(castState.update(state -> state.buildUpon().setStation((Station) message.payload).build()));
                break;
            case SET_TRACK:
                render(castState.update(state -> state.buildUpon().setTrack((Track) message.payload).build()));
                break;
        }
    }

    private static void render(final PlayerState playerState) {
        CastPipe.sendToAppNow(new Message(MessageType.RENDER_CAST, playerState));
    }
}
