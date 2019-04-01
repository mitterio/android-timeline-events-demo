package io.mitter.recipes

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import org.greenrobot.eventbus.EventBus

class AppLifecycleListener : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {
        EventBus.getDefault().post(EnterForeground())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEnterBackground() {
        EventBus.getDefault().post(EnterBackground())
    }
}
