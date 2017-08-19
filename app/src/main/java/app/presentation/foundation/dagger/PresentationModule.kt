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

package app.presentation.foundation.dagger

import android.os.AsyncTask
import app.data.foundation.dagger.DataModule
import app.presentation.foundation.BaseApp
import app.presentation.foundation.transformations.Transformations
import app.presentation.foundation.transformations.TransformationsBehaviour
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.base_app_android.BuildConfig
import timber.log.Timber
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = arrayOf(DataModule::class))
class PresentationModule(private val app: BaseApp) {

  @Provides @Singleton fun provideApplication() = app

  @Provides internal fun provideTransformations(
      transformationsBehaviour: TransformationsBehaviour): Transformations {
    return transformationsBehaviour
  }

  /**
   * Sync with main thread after subscribing to observables emitting from data layer.
   */
  @Named("mainThread")
  @Provides
  internal fun provideSchedulerMainThread(): Scheduler = AndroidSchedulers.mainThread()

  /**
   * Using this executor as the scheduler for all async operations allow us to tell espresso when
   * the app is in an idle state in order to wait for the response.
   */
  @Named("backgroundThread")
  @Provides
  internal fun provideSchedulerBackgroundThread(): Scheduler = Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)

  /**
   * Resolve a Timber tree which logs important information for crash reporting.
   */
  @Provides
  @Singleton internal fun provideTimberTree(): Timber.Tree {
    return if (BuildConfig.DEBUG) {
      Timber.DebugTree()
    } else {
      object : Timber.Tree() {
        override fun log(priority: Int, tag: String, message: String, t: Throwable) {
          //FakeCrashLibrary.logError(t);
        }
      }
    }
  }

}
