package com.example.project.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class InputControlState {
    private static final String PREFIX = InputControl.class.getName();

    private SharedPreferences mPreferences;

    public InputControlState(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveState(InputControl controlPanel) {
        StateBundle cpState = new StateBundle(mPreferences, PREFIX + "", true);
        cpState.putInt("activeMethodIndex", controlPanel.getActiveMethodIndex());
        cpState.commit();

        for (InputMethod method : controlPanel.getInputMethods()) {
            StateBundle outState = new StateBundle(mPreferences, PREFIX + "" + method.getInputMethodName(), true);
            method.onSaveState(outState);
            outState.commit();
        }
    }

    public void restoreState(InputControl controlPanel) {
        StateBundle cpState = new StateBundle(mPreferences, PREFIX + "", false);
        int methodId = cpState.getInt("activeMethodIndex", 0);
        if (methodId != -1) {
            controlPanel.activateInputMethod(methodId);
        }

        for (InputMethod method : controlPanel.getInputMethods()) {
            StateBundle savedState = new StateBundle(mPreferences, PREFIX + "" + method.getInputMethodName(), false);
            method.onRestoreState(savedState);
        }
    }

    public static class StateBundle {
        private final SharedPreferences mPreferences;
        private final SharedPreferences.Editor mPrefEditor;
        private final String mPrefix;
        private final boolean mEditable;

        public StateBundle(SharedPreferences preferences, String prefix,
                           boolean editable) {
            mPreferences = preferences;
            mPrefix = prefix;
            mEditable = editable;

            if (mEditable) {
                mPrefEditor = preferences.edit();
            } else {
                mPrefEditor = null;
            }
        }

        public boolean getBoolean(String key, boolean defValue) {
            return mPreferences.getBoolean(mPrefix + key, defValue);
        }

        public float getFloat(String key, float defValue) {
            return mPreferences.getFloat(mPrefix + key, defValue);
        }

        public int getInt(String key, int defValue) {
            return mPreferences.getInt(mPrefix + key, defValue);
        }

        public String getString(String key, String defValue) {
            return mPreferences.getString(mPrefix + key, defValue);
        }

        public void putBoolean(String key, boolean value) {
            if (!mEditable) {
                throw new IllegalStateException("StateBundle is not editable");
            }
            mPrefEditor.putBoolean(mPrefix + key, value);
        }

        public void putFloat(String key, float value) {
            if (!mEditable) {
                throw new IllegalStateException("StateBundle is not editable");
            }
            mPrefEditor.putFloat(mPrefix + key, value);
        }

        public void putInt(String key, int value) {
            if (!mEditable) {
                throw new IllegalStateException("StateBundle is not editable");
            }
            mPrefEditor.putInt(mPrefix + key, value);
        }

        public void putString(String key, String value) {
            if (!mEditable) {
                throw new IllegalStateException("StateBundle is not editable");
            }
            mPrefEditor.putString(mPrefix + key, value);
        }

        public void commit() {
            if (!mEditable) {
                throw new IllegalStateException("StateBundle is not editable");
            }
            mPrefEditor.commit();
        }
    }
}