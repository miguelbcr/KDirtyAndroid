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

package app.presentation.foundation.fcm

import android.app.Application
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import app.presentation.foundation.BaseApp
import app.presentation.sections.dashboard.DashBoardActivity
import io.reactivex.Observable
import org.base_app_android.R
import rx_fcm.FcmReceiverUIBackground
import rx_fcm.Message

/**
 * Handle Fcm notifications received when the app is on background state.
 */
class FcmReceiverBackground : FcmReceiverUIBackground {

  override fun onNotification(oMessage: Observable<Message>) {
    oMessage.subscribe { message ->
      val baseApp = message.application() as BaseApp
      showNotification(message, baseApp)
    }
  }

  private fun showNotification(message: Message, application: Application) {
    val title = message.payload().getString("title")
    val body = message.payload().getString("body")

    val notificationBuilder = NotificationCompat.Builder(application)
        .setContentTitle(title)
        .setContentText(body)
        .setDefaults(Notification.DEFAULT_ALL)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setAutoCancel(true)
        .setContentIntent(getPendingIntentForNotification(message))

    val notificationManager = application.getSystemService(
        Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(1, notificationBuilder.build())
  }

  private fun getPendingIntentForNotification(message: Message): PendingIntent {
    val application = message.application()

    val intent = Intent(application, DashBoardActivity::class.java)

    val stackBuilder = TaskStackBuilder.create(application)
    stackBuilder.addParentStack(DashBoardActivity::class.java)
    stackBuilder.addNextIntent(intent)

    return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT)
  }
}

