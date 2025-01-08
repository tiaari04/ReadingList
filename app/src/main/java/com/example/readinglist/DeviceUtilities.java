package com.example.readinglist;

import android.content.Context;
import android.provider.Settings;

public class DeviceUtilities {
    public static String getAndroidID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}

