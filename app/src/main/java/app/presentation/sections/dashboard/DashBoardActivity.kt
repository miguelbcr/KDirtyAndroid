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

package app.presentation.sections.dashboard

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.content.res.Configuration
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import app.presentation.foundation.BaseApp
import app.presentation.foundation.views.BaseActivity
import app.presentation.foundation.views.FragmentsManager
import com.jakewharton.rxbinding2.support.design.widget.itemSelections
import io.reactivex.Observable
import kotlinx.android.synthetic.main.dashboard_activity.*
import kotlinx.android.synthetic.main.toolbar.*
import org.base_app_android.R

class DashBoardActivity : BaseActivity<DashboardPresenter.View, DashboardPresenter>(), LifecycleRegistryOwner, DashboardPresenter.View {
  private val registry = LifecycleRegistry(this)
  override fun getLifecycle(): LifecycleRegistry = registry
  lateinit var drawerToggle: ActionBarDrawerToggle

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.dashboard_activity)
    (application as BaseApp).presentationComponent.inject(this)
    lifecycle.addObserver(presenter.bind(this))
  }

  override fun initViews() {
    setSupportActionBar(toolbar)
    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    supportActionBar!!.setHomeButtonEnabled(true)

    drawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name)
    drawerLayout.addDrawerListener(drawerToggle)
  }

  override fun onDestroy() {
    drawerLayout.removeDrawerListener(drawerToggle)
    super.onDestroy()
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    drawerToggle.syncState()
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    drawerToggle.onConfigurationChanged(newConfig)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (drawerToggle.onOptionsItemSelected(item)) {
      return true
    } else if (item.itemId == android.R.id.home) {
      onBackPressed()
    }
    return super.onOptionsItemSelected(item)
  }

  override fun clicksItemSelected(): Observable<MenuItem> = navigationView.itemSelections()

  override fun setCheckedItemMenu(@IdRes id: Int) {
    navigationView.setCheckedItem(id)
  }

  override fun setTitleActionBar(@StringRes id: Int) {
    if (supportActionBar != null) supportActionBar?.title = getString(id)
  }

  override fun replaceFragment(fragmentsManager: FragmentsManager,
      classFragment: Class<out Fragment>): Boolean =
      fragmentsManager.replaceFragment(supportFragmentManager, R.id.fl_fragment, classFragment,
          false)

  override fun closeDrawer() {
    drawerLayout.closeDrawers()
  }
}
