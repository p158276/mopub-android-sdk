package com.mopub.mobileads;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.flurry.android.FlurryAgent;

public final class FlurryAgentWrapper {
    public static final String PARAM_API_KEY = "apiKey";
    public static final String PARAM_AD_SPACE_NAME = "adSpaceName";

    private static final String ORIGIN_IDENTIFIER = "Flurry_Mopub_Android";
    private static final String ORIGIN_VERSION = "6.4.0";
    private static FlurryAgentWrapper sWrapper;

    private FlurryAgent.Builder mAgentBuilder;

    public static synchronized FlurryAgentWrapper getInstance() {
        if (sWrapper == null) {
            sWrapper = new FlurryAgentWrapper();
        }

        return sWrapper;
    }

    private FlurryAgentWrapper() {
        mAgentBuilder = new FlurryAgent.Builder()
                .withLogEnabled(false)
                .withLogLevel(Log.INFO);

        FlurryAgent.addOrigin(ORIGIN_IDENTIFIER, ORIGIN_VERSION);
    }

    public synchronized void startSession(@NonNull final Context context, final String apiKey) {
        // validate parameters
        if (TextUtils.isEmpty(apiKey)) {
            return;
        }

        // init
        if (!FlurryAgent.isSessionActive()) {
            mAgentBuilder.build(context, apiKey);

            // sessions are automatic on ICS+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                return;
            }

            FlurryAgent.onStartSession(context);
        }
    }

    public synchronized void endSession(final Context context) {
        // validate parameters
        if (context == null) {
            return;
        }

        if (FlurryAgent.isSessionActive()) {
            // sessions are automatic on ICS+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                return;
            }

            FlurryAgent.onEndSession(context);
        }
    }
}

