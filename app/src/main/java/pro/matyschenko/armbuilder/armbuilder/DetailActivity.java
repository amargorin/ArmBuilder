package pro.matyschenko.armbuilder.armbuilder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import pro.matyschenko.armbuilder.armbuilder.adapter.DetailPageAdapter;

public class DetailActivity extends AppCompatActivity {

    ViewPager viewPager;
    DetailPageAdapter detailPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        long exercise_group_id = intent.getLongExtra("exercise_group_id", 0);
        Log.d("DatabaseHandler","Detail group id = " + exercise_group_id );
        setContentView(R.layout.activity_detail);
        detailPagerAdapter = new DetailPageAdapter(this, exercise_group_id);
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(detailPagerAdapter);
        viewPager.setCurrentItem(viewPager.getAdapter().getCount()); //Устанавливаем последний item текущим
    }
}
