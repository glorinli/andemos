package com.dogold.andemos.widgets.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dogold.andemos.widgets.R;
import com.dogold.andemos.widgets.customwidgets.CircleProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProgressBarActivity extends AppCompatActivity {
    private Unbinder mUnbinder;

    @BindView(R.id.circleProgressBar)
    CircleProgressBar circleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);

        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mUnbinder.unbind();
    }

    @OnClick({R.id.btnAddProgress})
    void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btnAddProgress:
                if (circleProgressBar.getProgress() < circleProgressBar.getMaxProgress()) {
                    circleProgressBar.setProgress(circleProgressBar.getProgress() + 10);
                }
                break;
        }
    }
}
