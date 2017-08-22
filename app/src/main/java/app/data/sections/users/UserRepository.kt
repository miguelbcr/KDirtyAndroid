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

package app.data.sections.users

import android.os.Bundle
import android.support.annotation.VisibleForTesting
import app.data.foundation.fcm.FcmMessageReceiver
import app.data.foundation.net.NetworkResponse
import io.reactivecache2.ProviderGroup
import io.reactivecache2.ReactiveCache
import io.reactivex.Single
import io.rx_cache2.Reply
import rx_fcm.internal.RxFcmMock
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class UserRepository @Inject constructor(private val githubUsersApi: GithubUsersApi,
                                         private val networkResponse: NetworkResponse,
                                         reactiveCache: ReactiveCache) {
    private val FIRST_DEFAULT_ID = 0;
    private val USERS_PER_PAGE = 50
    private val cacheProvider = reactiveCache.providerGroup<MutableList<User>>()
            .withKey<ProviderGroup<MutableList<User>>>("users")

    fun getUsers(lastIdQueried: Int?, refresh: Boolean): Single<List<User>> =
            getUsersReply(lastIdQueried, refresh).map { it.data }

    fun getRecentUser(): Single<User> = getUsersReply(FIRST_DEFAULT_ID, false).map { it.data[0] }

    fun searchByUserName(username : String) : Single<User> =
            githubUsersApi.getUserByName(username).compose(networkResponse.process())

    @VisibleForTesting
    fun getUsersReply(lastIdQueried: Int?, refresh: Boolean): Single<Reply<MutableList<User>>> {
        val id = lastIdQueried ?: FIRST_DEFAULT_ID
        val apiCall: Single<MutableList<User>> = githubUsersApi
                .getUsers(id, USERS_PER_PAGE)
                .compose(networkResponse.process())

        return if (refresh) {
            apiCall.compose(cacheProvider.replaceAsReply(id))
        } else {
            apiCall.compose(cacheProvider.readWithLoaderAsReply(id))
        }
    }

  fun addNewUser(user: User): Single<Unit> {
    return cacheProvider.read(FIRST_DEFAULT_ID)
        .doOnSuccess { users -> users.add(0, user) }
        .compose(cacheProvider.replace(FIRST_DEFAULT_ID))
        .map({ _ -> Unit })
  }

  fun mockAFcmNotification(): Single<Unit> {
    return Single.just(Unit)
        .delay(3, TimeUnit.SECONDS)
        .flatMap({ _ ->
          val payload = Bundle()

          payload.putString("payload", "{\n"
              + "\t\"id\":\"0\",\n"
              + "\t\"login\": \"FcmUserMock\",\n"
              + "\t\"avatar_url\":\"https://cdn0.iconfinder.com/data/icons/octicons/1024/mark-github-256.png\"\n"
              + "}")
          payload.putString("rx_fcm_key_target", FcmMessageReceiver.USERS_FCM)
          payload.putString("title", "Received a new user mock")
          payload.putString("body", "Go to users screen and check it!")

          RxFcmMock.Notifications.newNotification(payload)

          Single.just(Unit)
        })
  }
}