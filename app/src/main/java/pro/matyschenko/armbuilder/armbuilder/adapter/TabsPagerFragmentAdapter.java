package pro.matyschenko.armbuilder.armbuilder.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import pro.matyschenko.armbuilder.armbuilder.fragment.AbstractTabFragment;
import pro.matyschenko.armbuilder.armbuilder.fragment.ExerciseFragment;
import pro.matyschenko.armbuilder.armbuilder.fragment.MesurementFragment;

public class TabsPagerFragmentAdapter extends FragmentPagerAdapter {

    private Map<Integer, AbstractTabFragment> tabs;
    private Context context;

    public TabsPagerFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        initTabMaps(context);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        //Log.d("ERR",Integer(position))
        return tabs.get(position).getTitle();
    }

    @Override
    public Fragment getItem(int i) {
        return tabs.get(i);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    private void initTabMaps(Context context) {
        tabs = new HashMap<>();
        tabs.put(0,MesurementFragment.newInstance(context));
        tabs.put(1,ExerciseFragment.newInstance(context));
    }
}
