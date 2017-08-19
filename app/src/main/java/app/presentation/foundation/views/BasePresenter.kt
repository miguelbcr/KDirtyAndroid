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
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Base class for every new presenter.

 * @param <V> the associated view abstraction with this presenter.
 */
open class BasePresenter<V : ViewPresenter> @Inject constructor() : LifecycleObserver {
  @Inject lateinit var transformations: Transformations
  @Inject lateinit var notifications: Notifications
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
}
