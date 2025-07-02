/*
 * Copyright (c) 2018 Hai Zhang <dreaming.in.code.zh@gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.textselectionwebsearch;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.text.TextUtils;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

@SuppressWarnings("deprecation")
public class SettingsActivity extends PreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Preference mCustomSearchEngineUrlFormatPreference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            // Work around the issue that the fix for edge-to-edge enforcement
            // https://android.googlesource.com/platform/frameworks/base/+/213033ecf41f17aebd766abfa7af7c789f245ff7
            // is only available on V QPR2 and above.
            final ViewGroup contentView = requireViewById(android.R.id.content);
            contentView.getChildAt(0).setFitsSystemWindows(true);
        }

        addPreferencesFromResource(R.xml.settings);

        // android.R.attr.preferenceListStyle isn't a public API.
        getListView().setDivider(null);

        final PreferenceScreen preferenceScreen = getPreferenceScreen();
        mCustomSearchEngineUrlFormatPreference = preferenceScreen.findPreference(getString(
                R.string.pref_key_custom_search_engine_url_format));
        updateCustomSearchEngineUrlFormatPreferenceVisibility();
        preferenceScreen.getSharedPreferences().registerOnSharedPreferenceChangeListener(
                this);
    }

    @Override
    public void onSharedPreferenceChanged(@NonNull SharedPreferences sharedPreferences,
                                          @Nullable String key) {
        if (Objects.equals(key, getString(R.string.pref_key_search_engine)) || key == null) {
            updateCustomSearchEngineUrlFormatPreferenceVisibility();
        }
    }

    private void updateCustomSearchEngineUrlFormatPreferenceVisibility() {
        final PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (TextUtils.isEmpty(WebSearchActivity.getSearchEngineUrlFormat(this))) {
            preferenceScreen.addPreference(mCustomSearchEngineUrlFormatPreference);
        } else {
            preferenceScreen.removePreference(mCustomSearchEngineUrlFormatPreference);
        }
    }
}
