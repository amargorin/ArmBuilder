package pro.matyschenko.armbuilder.armbuilder.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

public class AbstractTabFragment extends Fragment {
    protected  String title;
    protected Context context;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
