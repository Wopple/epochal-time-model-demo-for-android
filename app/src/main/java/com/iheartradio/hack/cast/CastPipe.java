package com.iheartradio.hack.cast;

import com.iheartradio.hack.EpochApplication;

import rx.Observable;
import rx.subjects.PublishSubject;

import static com.iheartradio.hack.Globals.*;

public class CastPipe {
    private static final PublishSubject<Message> sToCast = PublishSubject.create();
    private static final PublishSubject<Message> sToApp = PublishSubject.create();

    public static Observable<Message> getCast() {
        return sToCast;
    }

    public static Observable<Message> getApp() {
        return sToApp;
    }

    public static void sendToCast(Message message) {
        EpochApplication.handler.postDelayed(() -> sToCast.onNext(message), delaySupplier.get());
    }

    public static void sendToCastNow(Message message) {
        sToCast.onNext(message);
    }

    public static void sendToApp(Message message) {
        EpochApplication.handler.postDelayed(() -> sToApp.onNext(message), delaySupplier.get());
    }

    public static void sendToAppNow(Message message) {
        sToApp.onNext(message);
    }
}
