package com.example.tangliang.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tangliang.activity.R;

/**
 * RxAndroid示例选择界面
 */
public class MainFragment extends Fragment
{

    public MainFragment()
    {
        // Required empty public constructor
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View fragmentView = inflater.inflate(R.layout.fragment_main, container, false);
        initViews(fragmentView);
        return fragmentView;
    }

    /**
     * 初始化控件
     *
     * @param fragmentView
     */
    private void initViews(View fragmentView)
    {
        Button AsyncTaskButton = (Button) fragmentView.findViewById(R.id.BtnAsyncTask_MainFragment);
        Button BufferButton = (Button) fragmentView.findViewById(R.id.BtnBuffer_MainFragment);

        AsyncTaskButton.setOnClickListener(onBtnClickListener);
        BufferButton.setOnClickListener(onBtnClickListener);
    }


    /**
     * 按钮点击监听器
     */
    private View.OnClickListener onBtnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            int id = v.getId();
            switch (id)
            {
                case R.id.BtnAsyncTask_MainFragment:
                    getFragmentManager().beginTransaction()
                                        .addToBackStack(this.toString())
                                        .replace(R.id.Container_MainActivity, new AsyncTaskFragment())
                                        .commit();
                    break;
                case R.id.BtnBuffer_MainFragment:
                    getFragmentManager().beginTransaction()
                                        .addToBackStack(this.toString())
                                        .replace(R.id.Container_MainActivity, new BufferFragment())
                                        .commit();
                    break;

            }
        }
    };
}
