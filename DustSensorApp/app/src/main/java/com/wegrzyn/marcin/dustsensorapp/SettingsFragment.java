package com.wegrzyn.marcin.dustsensorapp;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * Created by Marcin Węgrzyn on 01.02.2018.
 * wireamg@gmail.com
 */

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            addPreferencesFromResource(R.xml.pref_visualizer);
    }
}
