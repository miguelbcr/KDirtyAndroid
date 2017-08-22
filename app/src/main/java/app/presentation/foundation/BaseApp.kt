/*
 * Copyright 2016 Victor Albertos
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

package app.presentation.foundation

import android.app.Activity
import android.app.Application
import app.data.foundation.fcm.FcmMessageReceiver
import app.data.foundation.fcm.FcmTokenReceiver
import app.presentation.foundation.dagger.DaggerPresentationComponent
import app.presentation.foundation.dagger.PresentationComponent
import app.presentation.foundation.dagger.PresentationModule
import app.presentation.foundation.fcm.FcmReceiverBackground
import app.presentation.foundation.widgets.Notifications
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import rx_fcm.internal.RxFcm


/**
 * Custom Application
 */
class BaseApp : Application() {

  /**
   * Expose the PresentationComponent single instance in order to inject on demand the dependency
   * graph in both Fragments and Activities.
   */
  val presentationComponent: PresentationComponent by lazy {
    DaggerPresentationComponent.builder()
        .presentationModule(PresentationModule(this))
        .build()
  }

  /**
   * Expose a reference to current Activity to be used for other classes which may depend on it.
   *
   * @see Notifications as an example.
   */
  val liveActivity: Activity?
    get() = AppCare.liveActivity

  override fun onCreate() {
    super.onCreate()
    setupLeakCanary()
    AppCare.registerActivityLifeCycle(this)
    initFcm()
  }

  private fun setupLeakCanary(): RefWatcher {
    return if (LeakCanary.isInAnalyzerProcess(this)) {
      RefWatcher.DISABLED
    } else LeakCanary.install(this)
  }

  private fun initFcm() {
    RxFcm.Notifications.init(this, FcmMessageReceiver(), FcmReceiverBackground())
    RxFcm.Notifications.onRefreshToken(FcmTokenReceiver())
  }
}
