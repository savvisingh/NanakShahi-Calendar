package com.mdgiitr.nanakshahicalendar.util;

import android.util.Log;

import apps.savvisingh.nanakshahicalendar.BuildConfig;

/** Log utility class to handle the log tag and DEBUG-only logging. */
public class Logr {
  public static void d(String message) {
    if (BuildConfig.DEBUG) {
      Log.d("NanakShahi Calendar", message);
    }
  }

  public static void d(String message, Object... args) {
    if (BuildConfig.DEBUG) {
      d(String.format(message, args));
    }
  }
}
