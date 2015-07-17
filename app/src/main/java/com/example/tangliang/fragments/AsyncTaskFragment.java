package com.example.tangliang.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.tangliang.activity.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.util.Log.e;

/**
 * 异步任务示例界面
 */
public class AsyncTaskFragment extends Fragment
{
    /** 成绩列表:假定成绩需要计算很久 */
    private ListView scoresListView;
    private ArrayAdapter<String> scoresAdapter;
    private List<String> scoresList = new ArrayList<>();

    private static final int studentNum = 30;
    private Integer[] scores = new Integer[studentNum];

    private int testCount = 0;
    
    public AsyncTaskFragment()
    {
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View fragmentView = inflater.inflate(R.layout.fragment_async_task, null);
        initData();
        initViews(fragmentView);
        return fragmentView;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        computeScores();
    }

    /**
     * 初始化数据(产生随机分数)
     */
    private void initData()
    {
        Random random = new Random();
        final int low = 40;
        final int high = 100;
        for (int i = 0; i < studentNum; i++)
        {
            scores[i] = random.nextInt(high - low) + low; //产生[low,high]随机数
        }
    }

    /**
     * 初始化控件
     *
     * @param fragmentView
     */
    private void initViews(View fragmentView)
    {
        scoresListView = (ListView) fragmentView.findViewById(R.id.LvScores_AsyncTaskFragment);
        scoresAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, scoresList);//此时scoresList没内容
        scoresListView.setAdapter(scoresAdapter);
    }

    /**
     * 模拟费时操作
     */
    private void simulateLongTask()
    {
        try
        {
            TimeUnit.MILLISECONDS.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 新建异步数据流
     *
     * @return
     */
    private Observable<String> getObservable()
    {
        return Observable.from(scores)
                         .map(new Func1<Integer, String>()//将数值转换成字符串
                         {
                             @Override
                             public String call(Integer str)
                             {
                                 simulateLongTask();
                                 testCount++;
                                 return str + "分" + " No." + testCount;
                             }
                         });
    }

    /**
     * 获得异步数据消费者
     *
     * @return 异步数据消费者
     */
    private Observer<String> getObserver()
    {
        return new Observer<String>()
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
            public void onNext(String s)
            {
                //更新成绩列表
                scoresList.add(s);
                scoresAdapter.notifyDataSetChanged();
            }
        };
    }

    /**
     * 计算分数(后台)
     */
    private void computeScores()
    {
        AppObservable.bindFragment(this, getObservable()).subscribeOn(Schedulers.io())//后台计算
                .observeOn(AndroidSchedulers.mainThread())//主线程显示结果
                .subscribe(getObserver());
    }
}
