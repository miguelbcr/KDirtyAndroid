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

package app.presentation.sections.launch

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import app.data.foundation.extensions.addTo
import app.presentation.foundation.views.BasePresenter
import app.presentation.foundation.views.ViewPresenter
import javax.inject.Inject

class LaunchPresenter @Inject constructor(
    private val wireframe: LaunchWireframe) : BasePresenter<LaunchPresenter.View>() {

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  fun onCreate() {
    wireframe.dashboard()
        .subscribe()
        .addTo(disposables)
  }

  interface View : ViewPresenter
}
