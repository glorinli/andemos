package li.glorin.arouterdemo.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

@Route(path = "/test/activity")
public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";

    @Autowired(name = "date")
    String mDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Call this inject method to autowire data
        ARouter.getInstance().inject(this);

        Log.d(TAG, "Date = " + mDate);
    }
}
