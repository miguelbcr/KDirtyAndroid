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

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.data.sections.users.User
import app.presentation.foundation.BaseApp
import app.presentation.foundation.views.BaseFragment
import app.presentation.sections.users.UserViewGroup
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.users_fragment.*
import miguelbcr.ok_adapters.recycler_view.OkRecyclerViewAdapter
import miguelbcr.ok_adapters.recycler_view.Pager
import org.base_app_android.R



class UsersFragment : BaseFragment<UsersPresenter.View, UsersPresenter>(), LifecycleRegistryOwner, UsersPresenter.View {
  private lateinit var adapter: OkRecyclerViewAdapter<User, UserViewGroup>
  private var positionRecyclerState: Int = 0
  private val registry = LifecycleRegistry(this)
  override fun getLifecycle(): LifecycleRegistry = registry

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater?.inflate(R.layout.users_fragment, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    (activity.application as BaseApp).presentationComponent.inject(this)
    lifecycle.addObserver(presenter.bind(this))
  }

  override fun onDestroyView() {
    positionRecyclerState = (rvUsers.layoutManager as GridLayoutManager)
        .findFirstCompletelyVisibleItemPosition()
    super.onDestroyView()
  }

  override fun initViews() {
    adapter = object : OkRecyclerViewAdapter<User, UserViewGroup>() {
      override fun onCreateItemView(parent: ViewGroup, viewType: Int): UserViewGroup {
        return UserViewGroup(activity)
      }
    }

    val layoutManager = GridLayoutManager(activity, 2)
    adapter.configureGridLayoutManagerForPagination(layoutManager)

    rvUsers.layoutManager = layoutManager
    rvUsers.adapter = adapter

    layoutManager.scrollToPosition(positionRecyclerState)
  }

  override fun setUpLoaderPager(initialLoad: List<User>,
      loaderPager: Pager.LoaderPager<User>) {
    adapter.setPager(R.layout.srv_progress, initialLoad, loaderPager)
  }

  override fun setUpRefreshList(call: Pager.Call<User>) {
    swipeRefreshUsers.setOnRefreshListener { adapter.resetPager(call) }
  }

  override fun userSelectedClicks(): Observable<User> {
    val clicks = PublishSubject.create<User>()
    adapter.setOnItemClickListener { user, _, _ -> clicks.onNext(user) }
    return clicks
  }

  override fun hideLoadingOnRefreshList() {
    swipeRefreshUsers.isRefreshing = false
  }

  override fun showNewUser(user: User) {
    adapter.all.add(0, user)
    adapter.notifyDataSetChanged()
  }
}
