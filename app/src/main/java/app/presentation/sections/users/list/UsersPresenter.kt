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

package app.presentation.sections.users.list

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.support.annotation.VisibleForTesting
import android.view.View
import app.data.foundation.extemsions.addTo
import app.data.foundation.fcm.FcmMessageReceiver
import app.data.sections.users.User
import app.data.sections.users.UserRepository
import app.presentation.foundation.views.BasePresenter
import app.presentation.foundation.views.ViewPresenter
import app.presentation.sections.users.UsersWireframe
import io.reactivex.Observable
import miguelbcr.ok_adapters.recycler_view.Pager
import rx_fcm.Message
import javax.inject.Inject


class UsersPresenter @Inject constructor(private val repository: UserRepository,
    private val wireframe: UsersWireframe) : BasePresenter<UsersPresenter.View>() {
  @VisibleForTesting
  val users: MutableList<User> = mutableListOf()

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  fun onCreate() {
    view.initViews()

    view.setUpLoaderPager(users, Pager.LoaderPager { lastUser ->
      Pager.Call<User> { nextPage(lastUser, it) }
    })

    view.setUpRefreshList(Pager.Call { this.refreshList(it) })

    view.userSelectedClicks()
        .flatMap { (user, view) -> wireframe.userScreen(user, view).toObservable<Any>() }
        .subscribe()
        .addTo(disposables)
  }

  @VisibleForTesting
  fun nextPage(user: User?, callback: Pager.Callback<User>) {
    repository.getUsers(user?.id, false)
        .compose(transformations.safely<List<User>>())
        .compose(transformations.reportOnSnackBar<List<User>>())
        .subscribe { users ->
          callback.supply(users)
          this.users.addAll(users)
        }.addTo(disposables)
  }

  @VisibleForTesting
  fun refreshList(callback: Pager.Callback<User>) {
    repository.getUsers(null, true)
        .compose(transformations.safely<List<User>>())
        .compose(transformations.reportOnSnackBar<List<User>>())
        .subscribe { users ->
          callback.supply(users)
          this.users.clear()
          this.users.addAll(users)
          view.hideLoadingOnRefreshList()
        }.addTo(disposables)
  }

  override fun onTargetNotification(oMessage: Observable<Message>) {
    repository.getRecentUser()
        .compose(transformations.safely<Any>())
        .compose(transformations.reportOnSnackBar())
        .subscribe { user ->
          users.add(0, user as User)
          view.showNewUser(user)
        }
  }

  override fun matchesTarget(key: String) = FcmMessageReceiver.USERS_FCM == key

  interface View : ViewPresenter {

    fun initViews()
    fun setUpLoaderPager(initialLoad: List<User>, loaderPager: Pager.LoaderPager<User>)
    fun setUpRefreshList(call: Pager.Call<User>)
    fun userSelectedClicks(): Observable<Pair<User, android.view.View>>
    fun hideLoadingOnRefreshList()
    fun showNewUser(user: User)
  }
}
