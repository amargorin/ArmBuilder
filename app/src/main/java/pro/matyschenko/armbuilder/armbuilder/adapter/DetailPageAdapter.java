package pro.matyschenko.armbuilder.armbuilder.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import pro.matyschenko.armbuilder.armbuilder.R;

public class DetailPageAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    ArrayList<String> pages = new ArrayList<>();
    private Context context;

    public DetailPageAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        pages.add("Page 1");
        pages.add("Page 2");

    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(R.layout.detail_item, container, false);
        TextView textView = itemView.findViewById(R.id.text_detail);
        textView.setText(pages.get(position));
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


}
