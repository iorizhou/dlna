package com.xiaojiutech.dlna.mvp.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.xiaojiutech.dlna.utils.ActivityHolder;

public class BaseFragmentActivity extends FragmentActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        ActivityHolder.getInstance().addActivity(this);
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onDestroy() {
        ActivityHolder.getInstance().removeActivity(this);
        super.onDestroy();
    }
}
