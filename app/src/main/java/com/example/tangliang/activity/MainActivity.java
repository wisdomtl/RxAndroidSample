package com.example.tangliang.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;

import com.example.tangliang.fragments.MainFragment;

import static android.util.Log.e;


public class MainActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //显示主Fragment
        getFragmentManager().beginTransaction()
                            .replace(R.id.Container_MainActivity, new MainFragment())
                            .commit();
    }


    /**
     * 判断当前线程是否是主线程
     *
     * @return
     */
    private boolean isMainThread()
    {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    
}
