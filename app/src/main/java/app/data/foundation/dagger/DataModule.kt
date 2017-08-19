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

package app.data.foundation.dagger

import android.support.annotation.StringRes
import app.data.foundation.Resources
import app.data.foundation.net.ApiModule
import app.presentation.foundation.BaseApp
import dagger.Module
import dagger.Provides
import io.reactivecache2.ReactiveCache
import io.victoralbertos.jolyglot.GsonSpeaker
import io.victoralbertos.jolyglot.JolyglotGenerics
import java.util.*
import javax.inject.Singleton

/**
 * Resolve the dependencies for data layer. For networking dependencies {@see ApiModule}, which its
 * concrete implementation depends on the current build variant in order to be able to provide a
 * mock implementation for the networking layer.
 */
@Module(includes = arrayOf(ApiModule::class))
class DataModule {

  @Singleton
  @Provides internal fun provideUiUtils(baseApp: BaseApp): Resources {
    return object : Resources {
      override fun getString(@StringRes idResource: Int) = baseApp.getString(idResource)
      override fun getLang() = Locale.getDefault().language
    }
  }

  @Singleton
  @Provides
  fun provideReactiveCache(baseApp: BaseApp, jolyglot: JolyglotGenerics): ReactiveCache =
      ReactiveCache.Builder()
          .using(baseApp.filesDir, jolyglot)

  @Singleton
  @Provides
  fun provideJolyglot(): JolyglotGenerics = GsonSpeaker()
}
