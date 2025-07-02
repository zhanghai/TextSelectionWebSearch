package me.zhanghai.android.textselectionwebsearch;

import android.content.Context;
import android.os.Build;
import android.preference.EditTextPreference;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

@SuppressWarnings("deprecation")
public class EditTextPreferenceCompat extends EditTextPreference {

    private boolean mTextSetCompat;

    public EditTextPreferenceCompat(@NonNull Context context) {
        super(context);
    }

    public EditTextPreferenceCompat(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextPreferenceCompat(@NonNull Context context, @Nullable AttributeSet attrs,
                                    @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EditTextPreferenceCompat(@NonNull Context context, @Nullable AttributeSet attrs,
                                    @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Nullable
    @Override
    public CharSequence getSummary() {
        final CharSequence summary = super.getSummary();
        final String text = getText();
        if (summary == null || TextUtils.isEmpty(text)) {
            return summary;
        } else {
            return String.format(summary.toString(), text);
        }
    }

    @Override
    public void setText(@Nullable String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            super.setText(text);
        } else {
            final boolean changed = !TextUtils.equals(getText(), text);
            if (changed || !mTextSetCompat) {
                super.setText(text);
                mTextSetCompat = true;
                if (changed) {
                    notifyChanged();
                }
            }
        }
    }
}
