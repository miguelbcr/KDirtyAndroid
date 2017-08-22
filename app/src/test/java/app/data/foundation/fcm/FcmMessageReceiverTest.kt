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

package app.data.foundation.fcm

import android.os.Bundle
import app.data.foundation.MockModel
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import rx_fcm.Message

class FcmMessageReceiverTest {
  @Mock internal var bundle: Bundle = mock()
  private lateinit var message: Message
  private lateinit var fcmMessageReceiverUT: FcmMessageReceiver

  @Before
  fun init() {
    fcmMessageReceiverUT = FcmMessageReceiver()

    whenever(bundle.getString("title")).thenReturn("title")
    whenever(bundle.getString("body")).thenReturn("body")
    whenever(bundle.getString("payload")).thenReturn("{\"s1\":\"s1\"}")
    message = Message(null, bundle, null, null)
  }

  @Test
  fun Verify_Model() {
    val (s1) = fcmMessageReceiverUT.getModel(MockModel::class.java, message)
    assertEquals(s1, "s1")
  }
}
