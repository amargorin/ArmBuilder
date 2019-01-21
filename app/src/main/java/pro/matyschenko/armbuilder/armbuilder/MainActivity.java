package pro.matyschenko.armbuilder.armbuilder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import pro.matyschenko.armbuilder.armbuilder.adapter.TabsPagerFragmentAdapter;

public class MainActivity extends AppCompatActivity  {
//        implements ExerciseFragment.OnFragmentInteractionListener{

    SharedPreferences sp;
    Toolbar toolbar;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        sp = PreferenceManager.getDefaultSharedPreferences(this);
//
        initTabs();
    }

//    @Override
//    public void onFragmentInteraction(String name) {
//        MeasurementFragment fragment;
//        fragment = (MeasurementFragment) getFragmentManager()
//                .findFragmentById(R.id.detailFragment);
//        if (fragment != null && fragment.isInLayout()) {
//            fragment.setName(name);
//        }
//    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        int threshold_value = Integer.parseInt(sp.getString("threshold_value", "5"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, PrefActivity.class));
                return true;
            case R.id.select_device:
                startActivity(new Intent(this, BluetoothListActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void initTabs() {
        viewPager = findViewById(R.id.viewPager);
        TabsPagerFragmentAdapter adapter = new TabsPagerFragmentAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

    }
}
