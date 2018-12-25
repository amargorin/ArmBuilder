package pro.matyschenko.armbuilder.armbuilder;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import pro.matyschenko.armbuilder.armbuilder.fragment.PreferencesFragment;

public class PreferencesActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PreferencesFragment())
                .commit();
    }
}