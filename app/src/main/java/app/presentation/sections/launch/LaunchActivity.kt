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

package app.presentation.sections.launch

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import app.presentation.foundation.BaseApp
import javax.inject.Inject

class LaunchActivity : AppCompatActivity(), LifecycleRegistryOwner, LaunchPresenter.View {
  @Inject lateinit var presenter : LaunchPresenter
  private val registry = LifecycleRegistry(this)
  override fun getLifecycle(): LifecycleRegistry = registry

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (application as BaseApp).presentationComponent.inject(this)
    lifecycle.addObserver(presenter.bind(this))
  }
}
