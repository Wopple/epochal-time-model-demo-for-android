package com.iheartradio.hack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.iheartradio.hack.cast.CastPipe;
import com.iheartradio.hack.cast.Message;
import com.iheartradio.hack.cast.MessageType;
import com.iheartradio.hack.data.PlayerState;
import com.iheartradio.hack.epoch.R;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.iheartradio.hack.Globals.*;

public class MainActivity extends AppCompatActivity {
    private List<Subscription> mSubscriptions;

    private View mPlay;
    private View mStop;
    private View mNext;

    private View mKtu;
    private View mMars;
    private View mPerry;

    private TextView mAppIsPlaying;
    private TextView mAppStation;
    private TextView mAppTitle;
    private TextView mAppDescription;
    private ImageView mAppImage;

    private TextView mCastIsPlaying;
    private TextView mCastStation;
    private TextView mCastTitle;
    private TextView mCastDescription;
    private ImageView mCastImage;

    private SeekBar mDelayBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlay = findViewById(R.id.play);
        mStop = findViewById(R.id.stop);
        mNext = findViewById(R.id.next);

        mKtu = findViewById(R.id.ktu);
        mMars = findViewById(R.id.mars);
        mPerry = findViewById(R.id.perry);

        mAppIsPlaying = (TextView) findViewById(R.id.app_is_playing);
        mAppStation = (TextView) findViewById(R.id.app_station);
        mAppTitle = (TextView) findViewById(R.id.app_title);
        mAppDescription = (TextView) findViewById(R.id.app_description);
        mAppImage = (ImageView) findViewById(R.id.app_image);

        mCastIsPlaying = (TextView) findViewById(R.id.cast_is_playing);
        mCastStation = (TextView) findViewById(R.id.cast_station);
        mCastTitle = (TextView) findViewById(R.id.cast_title);
        mCastDescription = (TextView) findViewById(R.id.cast_description);
        mCastImage = (ImageView) findViewById(R.id.cast_image);

        mDelayBar = (SeekBar) findViewById(R.id.delay_bar);

        mPlay.setOnClickListener(v -> {
            CastPipe.sendToCast(new Message(MessageType.PLAY));
        });

        mStop.setOnClickListener(v -> {
            CastPipe.sendToCast(new Message(MessageType.STOP));
        });

        mNext.setOnClickListener(v -> {
            CastPipe.sendToCast(new Message(MessageType.NEXT, appState.get().track));
        });

        mKtu.setOnClickListener(v -> {
            CastPipe.sendToCast(new Message(MessageType.SET_STATION, KTU_STATION));
        });

        mMars.setOnClickListener(v -> {
            CastPipe.sendToCast(new Message(MessageType.SET_STATION, BRUNO_MARS_STATION));
        });

        mPerry.setOnClickListener(v -> {
            CastPipe.sendToCast(new Message(MessageType.SET_STATION, KATY_PERRY_STATION));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSubscriptions = new ArrayList<>();

        mSubscriptions.add(CastPipe.getApp()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleApp));

        mSubscriptions.add(CastPipe.getCast()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleCast));

        renderApp(appState.get());
        renderCast(castState.get());

        delaySupplier = () -> mDelayBar.getProgress() * 10L;
    }

    @Override
    protected void onPause() {
        super.onPause();

        Stream.of(mSubscriptions).forEach(Subscription::unsubscribe);
        mSubscriptions = null;

        delaySupplier = () -> 0L;
    }

    private void handleApp(final Message message) {
        switch (message.type) {
            case SYNC:
                renderApp(appState.set((PlayerState) message.payload));
                break;
            case RENDER_CAST:
                renderCast((PlayerState) message.payload);
                break;
        }
    }

    private void handleCast(final Message message) {
    }

    private void renderApp(final PlayerState playerState) {
        render(playerState, mAppIsPlaying, mAppStation, mAppTitle, mAppDescription, mAppImage);
    }

    private void renderCast(final PlayerState playerState) {
        render(playerState, mCastIsPlaying, mCastStation, mCastTitle, mCastDescription, mCastImage);
    }

    private void render(
            final PlayerState playerState,
            final TextView isPlaying,
            final TextView station,
            final TextView title,
            final TextView description,
            final ImageView image) {
        if (playerState.station == NO_STATION) {
            isPlaying.setText(isPlayingString(false));
            station.setText("");
            title.setText("");
            description.setText("");
            image.setBackground(null);
        } else {
            isPlaying.setText(isPlayingString(playerState.isPlaying));
            station.setText(playerState.station.name);

            if (playerState.track == NO_TRACK) {
                title.setText("");
                description.setText("");
                image.setBackgroundResource(playerState.station.image);
            } else {
                title.setText(playerState.track.title);
                description.setText(playerState.track.description);
                image.setBackgroundResource(playerState.track.image);
            }
        }
    }

    private String isPlayingString(final boolean isPlaying) {
        return isPlaying ? "playing" : "stopped";
    }
}
