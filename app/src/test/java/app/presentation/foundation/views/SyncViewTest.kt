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

package app.presentation.foundation.views

import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat


class SyncViewTest {
  private lateinit var syncViewUT: SyncView

  @Before
  fun init() {
    syncViewUT = SyncView()
  }

  @Test
  fun When_No_Added_Screen_Then_Need_To_Sync_Is_False() {
    val needToSync = syncViewUT.needToSync(MatcherMock())
    assertThat(needToSync).isFalse()
  }

  @Test
  fun When_Added_Screen_Then_Need_To_Sync_Is_True() {
    syncViewUT.addScreen(KEY_1)
    val needToSync = syncViewUT.needToSync(MatcherMock())
    assertThat(needToSync).isTrue()
  }

  @Test
  fun After_Call_Need_To_Sync_Then_Need_To_Sync_Returns_False() {
    syncViewUT.addScreen(KEY_1)

    var needToSync = syncViewUT.needToSync(MatcherMock())
    assertThat(needToSync).isTrue()

    needToSync = syncViewUT.needToSync(MatcherMock())
    assertThat(needToSync).isFalse()
  }

  @Test
  fun After_Call_Need_To_Sync_Repeated_Times_Then_Need_To_Sync_Returns_False() {
    syncViewUT.addScreen(KEY_1)
    syncViewUT.addScreen(KEY_1)
    syncViewUT.addScreen(KEY_1)
    syncViewUT.addScreen(KEY_1)

    var needToSync = syncViewUT.needToSync(MatcherMock())
    assertThat(needToSync).isTrue()

    needToSync = syncViewUT.needToSync(MatcherMock())
    assertThat(needToSync).isFalse()
  }

  @Test
  fun Verify_With_Noise() {
    syncViewUT.addScreen("1")
    syncViewUT.addScreen(KEY_1)
    syncViewUT.addScreen("2")
    syncViewUT.addScreen("3")
    syncViewUT.addScreen(KEY_1)
    syncViewUT.addScreen("4")

    var needToSync = syncViewUT.needToSync(MatcherMock())
    assertThat(needToSync).isTrue()

    needToSync = syncViewUT.needToSync(MatcherMock())
    assertThat(needToSync).isFalse()
  }

  private class MatcherMock : SyncView.Matcher {
    override fun matchesTarget(key: String): Boolean {
      return KEY_1 == key
    }
  }

  companion object {
    private val KEY_1 = "key_1"
  }


}