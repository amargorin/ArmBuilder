package pro.matyschenko.armbuilder.armbuilder.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageButton;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import pro.matyschenko.armbuilder.armbuilder.DB.DatabaseHandler;
import pro.matyschenko.armbuilder.armbuilder.DB.Exercise;
import pro.matyschenko.armbuilder.armbuilder.R;

public class DetailPageAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    ArrayList<String> pages = new ArrayList<>();
    private Context context;
    DatabaseHandler databaseHandler;
    List<Exercise> data;
    long group_id;

    public DetailPageAdapter(Context context, long group_id) {
        this.context = context;
        this.group_id = group_id;
        layoutInflater = LayoutInflater.from(context);
        databaseHandler = new DatabaseHandler(context);
        data = databaseHandler.getAllExercisesByID(group_id);
    }

    @Override
    public int getCount() {
        Log.d("DatabaseHandler","getCount() return size: " + String.valueOf(data.size()));
        return data.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        Log.d("DatabaseHandler","isViewFromObject() return object: ");
        return view == object;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        Log.d("DatabaseHandler","getItemPosition() return position: " + String.valueOf(super.getItemPosition(object)));
        int position = super.getItemPosition(object);
        if (position == -1) position = data.size();
        return position;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.detail_item, container, false);
        TextView maxView = itemView.findViewById(R.id.text_max);
        maxView.setText(String.valueOf(data.get(position).getMaxValue()));
        TextView avgView = itemView.findViewById(R.id.text_avg);
        avgView.setText(String.valueOf(data.get(position).getAvgValue()));
        TextView totalView = itemView.findViewById(R.id.text_total);
        totalView.setText(String.valueOf(data.get(position).getAddValue()));
        TextView counterView = itemView.findViewById(R.id.text_counter);
        counterView.setText(String.valueOf(data.get(position).getAddCounter()));
        TextView date_View = itemView.findViewById(R.id.text_date);
        date_View.setText(data.get(position).get_date());
        Button delete_detail = itemView.findViewById(R.id.delete_detail);
        delete_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHandler.deleteExercise(data.get(position));
                Log.d("DatabaseHandler","Click delete item: " + String.valueOf(position));
                data.remove(position);
                notifyDataSetChanged();
//                ViewPager vv = (ViewPager)container;
//                Log.d("DatabaseHandler","Detail set current position= " + String.valueOf(data.size()));
//                vv.setCurrentItem(data.size());
            }
        });
        Chronometer chronometer = itemView.findViewById(R.id.time_total);
        long total_elapsedMillis = data.get(position).get_elapsedMillis();
        chronometer.setText(String.format("%02d : %02d ",
                TimeUnit.MILLISECONDS.toMinutes(total_elapsedMillis),
                TimeUnit.MILLISECONDS.toSeconds(total_elapsedMillis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(total_elapsedMillis))
        ));
        GridLayout gridLayout = itemView.findViewById(R.id.results_array);
        Resources res = context.getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        Drawable drawable = res.getDrawable(R.drawable.transparent_bg_bordered_button);
        try{
            JSONArray jsonArray = new JSONArray(data.get(position).getValue());
            for (int i=0; i< jsonArray.length();i++){
                Button button = new Button(context);
                ViewGroup.LayoutParams params = new GridLayout.LayoutParams();
                button.setText(jsonArray.getString(i));
                button.setBackground(drawable);
                params.width = ((width * 30) / 100); // 33%
                button.setLayoutParams(params);
                gridLayout.addView(button);
            }
        } catch (JSONException e){}
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Log.d("DatabaseHandler","destroyItem(): " + String.valueOf(position));
        container.removeView((View) object);
    }
}
