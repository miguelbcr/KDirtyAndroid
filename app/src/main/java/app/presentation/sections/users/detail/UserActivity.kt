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

package app.presentation.sections.users.detail

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.MenuItem
import app.data.sections.users.User
import app.presentation.foundation.BaseApp
import app.presentation.foundation.views.BaseActivity
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.user_activity.*
import org.base_app_android.R

class UserActivity : BaseActivity<UserPresenter.View, UserPresenter>(), LifecycleRegistryOwner, UserPresenter.View {
  private val registry = LifecycleRegistry(this)
  override fun getLifecycle(): LifecycleRegistry = registry

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.user_activity)
    (application as BaseApp).presentationComponent.inject(this)
    lifecycle.addObserver(presenter.bind(this))
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == android.R.id.home) onBackPressed()
    return super.onOptionsItemSelected(item)
  }

  override fun initViews() {
    setSupportActionBar(toolbar)
    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    supportActionBar!!.title = getString(R.string.user)
  }

  override fun showUser(user: User) {
    userViewGroup.bind(user, 0, 0)
  }

}
