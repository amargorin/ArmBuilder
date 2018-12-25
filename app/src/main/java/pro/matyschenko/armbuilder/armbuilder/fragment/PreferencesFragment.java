package pro.matyschenko.armbuilder.armbuilder.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import pro.matyschenko.armbuilder.armbuilder.R;

public class PreferencesFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
