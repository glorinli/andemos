package com.dogold.andemos.widgets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.dogold.andemos.widgets.activities.HoleViewActivity;
import com.dogold.andemos.widgets.activities.ProgressBarActivity;
import com.dogold.andemos.widgets.activities.PulseFrameLayoutActivity;
import com.dogold.andemos.widgets.activities.SlideChoiceBarActivity;
import com.dogold.andemos.widgets.activities.SeekBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import xyz.dogold.andemos.common.utils.ActivityUtils;

public class MainActivity extends AppCompatActivity {
    private Unbinder mUnbinder;

    @BindView(R.id.btnSeekBar)
    Button btnSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @OnClick({R.id.btnSeekBar, R.id.btnSlideChoiceBar, R.id.btnHoleViews, R.id.btnPulse, R.id.btnProgressBar})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btnSeekBar:
                ActivityUtils.startActivity(this, SeekBarActivity.class);
                break;
            case R.id.btnSlideChoiceBar:
                ActivityUtils.startActivity(this, SlideChoiceBarActivity.class);
                break;
            case R.id.btnHoleViews:
                ActivityUtils.startActivity(this, HoleViewActivity.class);
                break;
            case R.id.btnPulse:
                ActivityUtils.startActivity(this, PulseFrameLayoutActivity.class);
                break;
            case R.id.btnProgressBar:
                ActivityUtils.startActivity(this, ProgressBarActivity.class);
                break;
        }
    }
}
