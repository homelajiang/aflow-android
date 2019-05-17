package com.anglll.simple;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anglll.beelayout.BeeAdapter;
import com.anglll.beelayout.BeeLayout;
import com.anglll.beelayout.BeeViewHolder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BeeLayout beeLayout = (BeeLayout) findViewById(R.id.bee_layout);
        MainAdapter adapter = new MainAdapter();
        beeLayout.setAdapter(adapter);
    }

    public class BeeHolder extends BeeViewHolder {

        public BeeHolder(View itemView) {
            super(itemView);
        }
    }

    public class MainAdapter extends BeeAdapter<BeeViewHolder> {

        @Override
        public BeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new BeeHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bee_item_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(BeeViewHolder viewHolder, int position) {

        }

        @Override
        public void notifyItemChanged(int position) {

        }
    }
}
