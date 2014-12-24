package org.weyoung.photoview.sample;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LauncherActivity extends ListActivity {

    public static final String[] options = {"Simple Sample", "ViewPager Sample", "Rotation Sample", "Android Universal Image Loader"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Class c;

        switch (position) {
            default:
            case 0:
                c = SimpleSampleActivity.class;
                break;
            case 1:
                c = ViewPagerActivity.class;
                break;
            case 2:
                c = RotationSampleActivity.class;
                break;
            case 3:
                c = AUILSampleActivity.class;
                break;
        }

        startActivity(new Intent(this, c));
    }

}
