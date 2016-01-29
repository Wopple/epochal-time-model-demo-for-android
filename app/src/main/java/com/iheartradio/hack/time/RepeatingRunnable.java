package com.iheartradio.hack.time;

class RepeatingRunnable implements Runnable {
    private final Runnable mRunnable;

    private int mNumTimes;

    RepeatingRunnable(Runnable runnable, final int numTimes) {
        if (runnable == null) {
            throw new IllegalArgumentException("runnable is null");
        }

        mRunnable = runnable;
        mNumTimes = numTimes;
    }

    @Override
    public void run() {
        if (mNumTimes == SteadyTimer.FOREVER) {
            mRunnable.run();
        } else if (mNumTimes > 0) {
            --mNumTimes;
            mRunnable.run();
        }
    }

    public boolean isDone() {
        return mNumTimes == 0;
    }
}
