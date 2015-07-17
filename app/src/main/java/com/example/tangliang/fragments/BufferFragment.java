package com.example.tangliang.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.tangliang.activity.R;
import com.example.tangliang.observable.ListItemObservable;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.view.OnClickEvent;
import rx.functions.Func1;

import static android.util.Log.e;
import static android.util.Log.i;

/**
 * 缓冲发送示例界面
 */
public class BufferFragment extends Fragment
{
    private ListView citiesListView;

    public BufferFragment()
    {
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View fragmentView = inflater.inflate(R.layout.fragment_buffer, null);
        initViews(fragmentView);
        return fragmentView;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        bufferClick();
    }


    /**
     * 初始化控件
     *
     * @param fragmentView 控件宿主Fragment
     */
    private void initViews(View fragmentView)
    {
        citiesListView = (ListView) fragmentView.findViewById(R.id.LvScores_AsyncTaskFragment);
    }


    /**
     * 获得异步数据消费者
     *
     * @return 异步数据消费者
     */
    private Observer<List<OnClickEvent>> getObserver()
    {
        return new Observer<List<OnClickEvent>>()
        {
            @Override
            public void onCompleted()
            {

            }

            @Override
            public void onError(Throwable e)
            {

            }

            @Override
            public void onNext(List<OnClickEvent> onClickEvents)
            {
                e("ttangliang", "Buffer text onNext");
            }
        };
    }

    /**
     * 计算分数(后台)
     */
    private void bufferClick()
    {
        int timespan = 900;
        ListItemObservable.clicks(citiesListView)
                          .buffer(timespan, TimeUnit.MILLISECONDS)
                          .filter(new Func1<List<OnClickEvent>, Boolean>()
                          {
                              @Override
                              public Boolean call(List<OnClickEvent> onClickEvents)
                              {
                                  if (onClickEvents.size() == 2)
                                  {
                                      return true;
                                  }
                                  else
                                  {
                                      return false;
                                  }
                              }
                          })
                          .observeOn(AndroidSchedulers.mainThread())
                          .subscribe(getObserver());
    }
}
