package li.glorin.arouterdemo;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Init ARouter
        if (BuildConfig.DEBUG) {
            // Should set log and debug before init
            ARouter.openLog();
            ARouter.openDebug();
        }

        ARouter.init(this);
    }
}
