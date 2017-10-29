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

import android.os.Bundle
import app.presentation.foundation.BaseApp
import app.presentation.foundation.views.BaseActivity

class LaunchActivity : BaseActivity<LaunchPresenter.View, LaunchPresenter>(), LaunchPresenter.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as BaseApp).presentationComponent.inject(this)
        lifecycle.addObserver(presenter.bind(this))
    }
}
