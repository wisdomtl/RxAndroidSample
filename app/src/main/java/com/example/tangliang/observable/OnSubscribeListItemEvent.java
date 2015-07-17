package com.example.tangliang.observable;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.AndroidSubscriptions;
import rx.android.internal.Assertions;
import rx.android.view.OnClickEvent;
import rx.functions.Action0;

/**
 * Created by Tangliang on 2015/7/14.
 */
public class OnSubscribeListItemEvent implements Observable.OnSubscribe<OnClickEvent>
{
    private final ListView listView;

    public OnSubscribeListItemEvent(final ListView listView)
    {
        this.listView = listView;
    }

    @Override
    public void call(final Subscriber<? super OnClickEvent> observer)
    {
        Assertions.assertUiThread();
        final CompositeOnClickListener composite = CachedListeners.getFromViewOrCreate(listView);

        final AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id)
            {
                observer.onNext(OnClickEvent.create(listView));

            }
        };

        final Subscription subscription = AndroidSubscriptions.unsubscribeInUiThread(new Action0()
        {
            @Override
            public void call()
            {
                composite.removeOnClickListener(listener);
            }
        });

        composite.addOnClickListener(listener);
        observer.add(subscription);
    }

    /**
     * 复合表项点击监听器
     */
    private static class CompositeOnClickListener implements AdapterView.OnItemClickListener
    {
        private final List<AdapterView.OnItemClickListener> listeners = new ArrayList<AdapterView.OnItemClickListener>();

        public boolean addOnClickListener(final AdapterView.OnItemClickListener listener)
        {
            return listeners.add(listener);
        }

        public boolean removeOnClickListener(final AdapterView.OnItemClickListener listener)
        {
            return listeners.remove(listener);
        }

        @Override
        public void onItemClick(AdapterView<?> parent,
                                View view,
                                int position,
                                long id)
        {
            for (final AdapterView.OnItemClickListener listener : listeners)
            {
                listener.onItemClick(parent, view, position, id);
            }
        }
    }

    /**
     * 复合点击监听器缓冲器
     */
    private static class CachedListeners
    {
        /** 建立控件和复合点击监听器连接 */
        private static final Map<ListView, CompositeOnClickListener> sCachedListeners = new WeakHashMap<ListView, CompositeOnClickListener>();

        /**
         * 获得和列表对应的复合点击监听器
         *
         * @param listView 需要被监听点击的列表
         * @return 复合点击监听器
         */
        public static CompositeOnClickListener getFromViewOrCreate(final ListView listView)
        {
            final CompositeOnClickListener cached = sCachedListeners.get(listView);

            if (cached != null)
            {
                return cached;
            }

            final CompositeOnClickListener listener = new CompositeOnClickListener();

            sCachedListeners.put(listView, listener);
            listView.setOnItemClickListener(listener);

            return listener;
        }
    }
}
