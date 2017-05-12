package com.eagle.examples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final Class<? extends Fragment>[] exampleFragments = new Class[]{
            SvgaMovieFragment.class,
            LottieFragment.class,
    };

    @BindView(R.id.example_list)
    ListView exampleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        exampleList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return exampleFragments.length;
            }

            @Override
            public Class<?> getItem(int position) {
                return exampleFragments[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) convertView;
                if (convertView == null) {
                    textView = (TextView) View.inflate(parent.getContext(), android.R.layout.simple_list_item_1, null);
                    textView.setMinimumHeight(getResources().getDimensionPixelSize(R.dimen.list_item_height));
                }
                textView.setText(getItem(position).getSimpleName());
                return textView;
            }
        });

        exampleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Class<? extends Fragment> clazz = (Class<? extends Fragment>) parent.getAdapter().getItem(position);
                Log.i("MainActivity", "onItemClick fragment = " + clazz.getSimpleName());
                try {
                    Fragment fragment = clazz.newInstance();
                    getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment, clazz.getSimpleName()).show(fragment).addToBackStack(clazz.getSimpleName()).commitAllowingStateLoss();
                } catch (Exception e) {
                    Log.e("MainActivity", "onItemClick fragment = " + clazz.getSimpleName(), e);
                }
            }
        });
    }
}
