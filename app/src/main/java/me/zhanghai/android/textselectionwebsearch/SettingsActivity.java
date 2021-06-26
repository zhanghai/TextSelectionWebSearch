/*
 * Copyright (c) 2018 Hai Zhang <dreaming.in.code.zh@gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.textselectionwebsearch;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import androidx.annotation.Nullable;

@SuppressWarnings("deprecation")
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        // android.R.attr.preferenceListStyle isn't a public API.
        getListView().setDivider(null);
    }
}
