package com.wegrzyn.marcin.dustsensorapp;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Marcin Węgrzyn on 01.02.2018.
 * wireamg@gmail.com
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {


    static final String TAG = SettingsFragment.class.getSimpleName();
    private String tempData;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_visualizer);

        tempData = getPreferenceManager().getSharedPreferences()
               .getString(getString(R.string.key_number_elements), "10");
        findPreference(getString(R.string.key_number_elements)).setSummary(tempData);
        Log.d(TAG, "onCreatePreferences: rootKey: " + rootKey);
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        super.onDisplayPreferenceDialog(preference);
        Log.d(TAG, "onDisplayPreference");
    }

    @Override
    public void setDivider(Drawable divider) {
        super.setDivider(divider);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.key_number_elements))) {

            String data = sharedPreferences
                    .getString(getString(R.string.key_number_elements), "10");

            if(TextUtils.isDigitsOnly(data)&& 0!=Integer.parseInt(data)){
                findPreference(getString(R.string.key_number_elements)).setSummary(data);
            }else {
                findPreference(getString(R.string.key_number_elements)).setSummary(tempData);
                sharedPreferences.edit().putString(getString(R.string.key_number_elements),tempData).commit();
                Toast toast = Toast.makeText(getContext(), R.string.enter_cor_number,Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            Log.d(SettingsFragment.TAG, "onSharedPreferenceChanged");

        }
    }
}