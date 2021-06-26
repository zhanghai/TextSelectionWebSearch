/*
 * Copyright (c) 2018 Hai Zhang <dreaming.in.code.zh@gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.textselectionwebsearch;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.webkit.URLUtil;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;

public class WebSearchActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String query = null;
        final Intent intent = getIntent();
        final String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case Intent.ACTION_PROCESS_TEXT: {
                    CharSequence text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
                    query = text != null ? text.toString() : null;
                    break;
                }
                case Intent.ACTION_SEND: {
                    CharSequence text = intent.getCharSequenceExtra(Intent.EXTRA_TEXT);
                    query = text != null ? text.toString() : null;
                    break;
                }
                case Intent.ACTION_WEB_SEARCH: {
                    query = intent.getStringExtra(SearchManager.QUERY);
                    break;
                }
            }
        }
        if (!TextUtils.isEmpty(query)) {
            webSearch(query);
        }
        finish();
    }

    private void webSearch(@NonNull String query) {
        if (URLUtil.isNetworkUrl(query) && shouldOpenUrl()) {
            openUrl(query);
            return;
        }
        final String url = String.format(getSearchEngineUrlFormat(), Uri.encode(query));
        openUrl(url);
    }

    private boolean shouldOpenUrl() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                getString(R.string.pref_key_open_url), getResources().getBoolean(
                        R.bool.pref_default_value_open_url));
    }

    @NonNull
    private String getSearchEngineUrlFormat() {
        final String[] urlFormats = getResources().getStringArray(
                R.array.config_search_engine_url_formats);
        final int index = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getString(R.string.pref_key_search_engine), getString(
                        R.string.pref_default_value_search_engine)));
        return urlFormats[index];
    }

    private void openUrl(@NonNull String url) {
        final Bitmap settingsIcon = getSettingsIcon();
        final String settingsDescription = getString(R.string.settings_title);
        final PendingIntent settingsPendingIntent = PendingIntent.getActivity(this,
                SettingsActivity.class.hashCode(), new Intent(this, SettingsActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        final CustomTabsIntent intent = new CustomTabsIntent.Builder()
                .setActionButton(settingsIcon, settingsDescription, settingsPendingIntent, true)
                .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
                .setShareState(CustomTabsIntent.SHARE_STATE_ON)
                .setShowTitle(true)
                .setUrlBarHidingEnabled(true)
                .build();
        if (!shouldUseCustomTabs()) {
            CustomTabsIntent.setAlwaysUseBrowserUI(intent.intent);
        }
        final Uri uri = Uri.parse(url);
        try {
            intent.launchUrl(this, uri);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.activity_not_found, Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    private Bitmap getSettingsIcon() {
        final Drawable drawable = getDrawable(R.drawable.settings_icon);
        final Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private boolean shouldUseCustomTabs() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                getString(R.string.pref_key_use_custom_tabs), getResources().getBoolean(
                        R.bool.pref_default_value_use_custom_tabs));
    }
}
