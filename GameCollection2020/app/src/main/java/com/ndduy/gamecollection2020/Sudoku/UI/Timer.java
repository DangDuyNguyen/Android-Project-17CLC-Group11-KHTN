package com.ndduy.gamecollection2020.Sudoku.UI;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

abstract class Timer extends Handler {
    private long mTickInterval;
    private boolean mIsRunning;
    private long mNextStepTime; // time at which to execute the next step()
    private int mTickCount; // number of step()
    private long mAccumTime;
    private long mLastSaveTime; // time at which last added to AccumTime

    public Timer(long iterval) {
        mTickInterval = iterval;
        mIsRunning = false;
        mAccumTime = 0;
    }

    public void start() {
        if (mIsRunning)
            return;

        mIsRunning = true;
        long now = SystemClock.uptimeMillis();
        mLastSaveTime = now;
        mNextStepTime = now;
        postAtTime(runner, mNextStepTime);
    }

    public void stop() {
        if (mIsRunning) {
            mIsRunning = false;
            long now = SystemClock.uptimeMillis();
            mAccumTime += now - mLastSaveTime;
            mLastSaveTime = now;
        }
    }

    public final void reset() {
        stop();
        mTickCount = 0;
        mAccumTime = 0;
    }

    public long getTotalTime() { return mAccumTime; }

    public final boolean isRunning() {
        return mIsRunning;
    }

    public final long getTime() {
        return mAccumTime;
    }

    protected abstract boolean step(int count, long time);

    protected void done() { }

    private final Runnable runner = new Runnable() {
        public final void run() {
            if (mIsRunning) {
                long now = SystemClock.uptimeMillis();
                mAccumTime += now - mLastSaveTime;
                mLastSaveTime = now;

                if (!step(mTickCount++, mAccumTime)) {
                    mNextStepTime += mTickInterval;
                    if (mNextStepTime <= now)
                        mNextStepTime += mTickInterval;
                    postAtTime(runner, mNextStepTime);
                } else {
                    mIsRunning = false;
                    done();
                }
            }
        }
    };

    void saveState(Bundle outState) {
        if (mIsRunning) {
            long now = SystemClock.uptimeMillis();
            mAccumTime += now - mLastSaveTime;
            mLastSaveTime = now;
        }

        outState.putLong("tickInterval", mTickInterval);
        outState.putBoolean("isRunning", mIsRunning);
        outState.putInt("tickCount", mTickCount);
        outState.putLong("accumTime", mAccumTime);
    }

    boolean restoreState(Bundle map) {
        return restoreState(map, true);
    }

    boolean restoreState(Bundle map, boolean run) {
        mTickInterval = map.getLong("tickInterval");
        mIsRunning = map.getBoolean("isRunning");
        mTickCount = map.getInt("tickCount");
        mAccumTime = map.getLong("accumTime");
        mLastSaveTime = SystemClock.uptimeMillis();

        if (mIsRunning) {
            if (run)
                start();
            else
                mIsRunning = false;
        }

        return true;
    }
}
