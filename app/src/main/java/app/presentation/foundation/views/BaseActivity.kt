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

import android.support.v7.app.AppCompatActivity
import io.reactivex.Observable
import rx_fcm.FcmReceiverUIForeground
import rx_fcm.Message
import javax.inject.Inject

abstract class BaseActivity<V : ViewPresenter, P : BasePresenter<V>> : AppCompatActivity(), FcmReceiverUIForeground {
  @Inject lateinit var presenter: P

  /**
   * Delegate the call to presenter.
   */
  override fun onTargetNotification(message: Observable<Message>) {
    presenter.onTargetNotification(message)
  }

  /**
   * Delegate the call to presenter.
   */
  override fun onMismatchTargetNotification(oMessage: Observable<Message>) {
    presenter.onMismatchTargetNotification(oMessage)
  }

  /**
   * Delegate the call to presenter.
   */
  override fun matchesTarget(key: String): Boolean = presenter.matchesTarget(key)
}
