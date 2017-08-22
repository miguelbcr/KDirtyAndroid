/*
 * Copyright 2017 Victor Albertos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.presentation.foundation.views

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import app.presentation.foundation.transformations.Transformations
import app.presentation.foundation.widgets.Notifications
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import rx_fcm.Message
import rx_fcm.internal.RxFcm
import javax.inject.Inject



/**
 * Base class for every new presenter.

 * @param <V> the associated view abstraction with this presenter.
 */
open class BasePresenter<V : ViewPresenter> @Inject constructor() : LifecycleObserver, SyncView.Matcher {
  @Inject lateinit var transformations: Transformations
  @Inject lateinit var notifications: Notifications
  @Inject lateinit var syncView: SyncView
  protected val disposables = CompositeDisposable()
  protected lateinit var view: V

  fun bind(view: V): BasePresenter<V> {
    this.view = view
    return this
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  open fun onDestroy() {
    disposables.dispose()
  }

  /**
   * Override if the Presenter requires to be notified, whether by a Fcm notification or due to some
   * other internal event both of them handled by screensSync instance.
   */
  override fun matchesTarget(key: String): Boolean = false

  /**
   * Called when a Fcm notification is received and it matches with the key provided by [ ][.matchesTarget].
   */
  open fun onTargetNotification(oMessage: Observable<Message>) {
    //Override if sub-class requires to show A Fcm notification.
  }

  /**
   * When a Fcm notification is received and it doesn't match with the key provided by [ ][.matchesTarget] a toast is shown and this key is added to syncView as a flag to notify
   * potential screen callers.
   */
  open fun onMismatchTargetNotification(oMessage: Observable<Message>) {
    notifications.showFcmNotification(
        oMessage.singleOrError().doOnSuccess { message -> message.target()?.let {
          syncView.addScreen(it)
        } })
  }

  /**
   * Gets the current Fcm token
   *
   * @return The Fcm token
   */
  protected fun tokenFcm(): Observable<String> =
      RxFcm.Notifications.currentToken().onErrorResumeNext { _: Throwable -> Observable.just("")  }
}
