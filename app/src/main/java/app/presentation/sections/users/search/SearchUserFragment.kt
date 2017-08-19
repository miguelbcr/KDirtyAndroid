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

package app.presentation.sections.users.search

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.data.sections.users.User
import app.presentation.foundation.BaseApp
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.user_search_fragment.*
import org.base_app_android.R
import javax.inject.Inject

class SearchUserFragment : Fragment(), LifecycleRegistryOwner, SearchUserPresenter.View {
  @Inject lateinit var presenter: SearchUserPresenter

  private val registry = LifecycleRegistry(this)
  override fun getLifecycle(): LifecycleRegistry = registry

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val view = inflater?.inflate(R.layout.user_search_fragment, container, false)
    (activity.application as BaseApp).presentationComponent.inject(this)
    return view
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    lifecycle.addObserver(presenter.bind(this))
  }

  override fun initViews() {
  }

  override fun clicksSearchUser(): Observable<Unit> {
    return btFindUser.clicks()
  }

  override fun username(): String {
    return etName.text.toString()
  }

  override fun showUser(user: User) {
    userViewGroup.visibility = View.VISIBLE
    userViewGroup.bind(user, 0, 0)
  }

}
