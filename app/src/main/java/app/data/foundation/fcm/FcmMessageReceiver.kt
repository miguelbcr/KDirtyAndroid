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

package app.data.foundation.fcm

import android.support.annotation.VisibleForTesting
import app.data.sections.users.User
import app.data.sections.users.UserRepository
import app.presentation.foundation.BaseApp
import io.reactivex.Observable
import io.reactivex.Single
import io.victoralbertos.jolyglot.GsonSpeaker
import io.victoralbertos.jolyglot.JolyglotGenerics
import io.victoralbertos.jolyglot.Types
import rx_fcm.FcmReceiverData
import rx_fcm.Message
import javax.inject.Inject

/**
 * Use to update cache data models when a new push notification has been received injecting as much
 * repositories are needed.
 */
class FcmMessageReceiver @Inject constructor() : FcmReceiverData {
  companion object {
    val USERS_FCM = "users_fcm"
  }

  private val jolyglot: JolyglotGenerics = GsonSpeaker()
  @Inject lateinit var userRepository: UserRepository

  override fun onNotification(oMessage: Observable<Message>): Observable<Message> {
    return oMessage.flatMap { message ->
      val baseApp = message.application() as BaseApp

      //Inject the Dagger graph to access the required dependencies.
      baseApp.presentationComponent.inject(this)

      var oMessageRet = Observable.just(message)
      val target = message.target()

      if (target == USERS_FCM) {
        oMessageRet = addNewUser(message).toObservable()
      }

      oMessageRet
    }
  }

  private fun addNewUser(message: Message): Single<Message> {
    val user = getModel(User::class.java, message)
    return userRepository.addNewUser(user).map { _ -> message }
  }

  /**
   * Serialize the bundle included in a Message to a model instance specifying the expected type.
   *
   * @param classData the class of the associated type.
   * @param message the notification received from Fcm.
   * @param <T> the type to serialize the data included in the 'payload' json param.
   * @return the serialized model.
  </T> */
  @VisibleForTesting internal fun <T> getModel(classData: Class<T>, message: Message): T {
    val payload = message.payload().getString("payload")
    val type = Types.newParameterizedType(classData)
    return jolyglot.fromJson(payload, type)
  }
}
