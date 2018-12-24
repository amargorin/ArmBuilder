package pro.matyschenko.armbuilder.armbuilder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import pro.matyschenko.armbuilder.armbuilder.adapter.DetailPageAdapter;

public class DetailActivity extends AppCompatActivity {

    ViewPager viewPager;
    DetailPageAdapter detailPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        detailPagerAdapter = new DetailPageAdapter(this);
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(detailPagerAdapter);
        viewPager.setCurrentItem(viewPager.getAdapter().getCount());
    }
}
