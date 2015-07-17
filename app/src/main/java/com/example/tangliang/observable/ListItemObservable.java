package com.example.tangliang.observable;

import android.view.View;
import android.widget.ListView;

import rx.Observable;
import rx.android.view.OnClickEvent;

/**
 * 将表项作为被观察对象
 */
public class ListItemObservable
{
    /**
     * 观察表项点击事件
     * @param listView 列表
     * @return
     */
    public static Observable<OnClickEvent> clicks(final ListView listView) {
        return Observable.create(new OnSubscribeListItemEvent(listView));
    }
}
