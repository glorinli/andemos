package com.dogold.andemos.widgets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.dogold.andemos.widgets.utils.ActivityUtils;
import com.dogold.andemos.widgets.views.SeekBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btnSeekBar)
    Button btnSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSeekBar)
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btnSeekBar:
                ActivityUtils.startActivity(this, SeekBarActivity.class);
                break;
        }
    }
}
