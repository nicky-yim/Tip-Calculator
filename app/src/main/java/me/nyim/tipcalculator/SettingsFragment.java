package me.nyim.tipcalculator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
/**
 * Created by Nicky on 2017/6/7.
 */

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TIP_VALUE = "default_tip";
    public static final String SPLIT_VALUE = "default_split";

    private Preference pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(TIP_VALUE)) {
            pref = findPreference(key);
            pref.setSummary(String.valueOf(sharedPreferences.getInt(key, 15)));
        }
        if (key.equals(SPLIT_VALUE)) {
            pref = findPreference(key);
            pref.setSummary(String.valueOf(sharedPreferences.getInt(key, 2)));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}