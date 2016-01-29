package com.iheartradio.hack.time;

import android.os.SystemClock;

import com.iheartradio.hack.EpochApplication;

public class SteadyTimer {
    public static final int FOREVER = -1;

    private boolean mStopped = false;

    /**
     * Must be called from the main thread. Starts scheduling and running the given task.
     * @param task run when scheduled, null will do nothing
     * @param delayMillis >= 0 : initial delay in milliseconds
     * @param rateMillis >= 0 : delay every time after the initial run in milliseconds
     * @param numTimes >= 0 or FOREVER : number of times the task will be run before the timer stops
     */
    public SteadyTimer(final Runnable task, final long delayMillis, final long rateMillis, final int numTimes) {
        if (delayMillis < 0) {
            throw new IllegalArgumentException("delayMillis is negative: " + delayMillis);
        }

        if (rateMillis < 0) {
            throw new IllegalArgumentException("rateMillis is negative: " + delayMillis);
        }

        if (numTimes < FOREVER) {
            throw new IllegalArgumentException("illegal value for num times: " + numTimes);
        }

        scheduleTask(
                new RepeatingRunnable(task, numTimes),
                SystemClock.uptimeMillis() + delayMillis,
                rateMillis);
    }

    public void stop() {
        mStopped = true;
    }

    private void scheduleTask(
            final RepeatingRunnable repeatingTask,
            final long postTime,
            final long rateMillis) {

        if (repeatingTask.isDone()) {
            stop();
        } else {
            EpochApplication.handler.postAtTime(makeReschedulingTask(repeatingTask, postTime + rateMillis, rateMillis), postTime);
        }
    }

    private Runnable makeReschedulingTask(
            final RepeatingRunnable repeatingTask,
            final long nextPostTime,
            final long rateMillis) {
        return () -> {
            if (!mStopped) {
                repeatingTask.run();
                scheduleTask(repeatingTask, nextPostTime, rateMillis);
            }
        };
    }

    public static class Builder {
        private Runnable mTask;
        private long mDelayMillis;
        private long mRateMillis;
        private int mNumTimes = FOREVER;

        public Builder() {
        }

        public Builder withTask(Runnable task) {
            mTask = task;
            return this;
        }

        public Builder withDelay(long delayMillis) {
            mDelayMillis = delayMillis;
            return this;
        }

        public Builder withRate(long rateMillis) {
            mRateMillis = rateMillis;
            return this;
        }

        public Builder withNumTimes(int numTimes) {
            mNumTimes = numTimes;
            return this;
        }

        public SteadyTimer build() {
            return new SteadyTimer(mTask, mDelayMillis, mRateMillis, mNumTimes);
        }
    }
}
