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

package app.sections.dashboard

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.NavigationViewActions
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import app.common.closeDrawer
import app.common.openDrawer
import app.presentation.sections.launch.LaunchActivity
import io.victoralbertos.device_animation_test_rule.DeviceAnimationTestRule
import org.base_app_android.R
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test

class DashboardTest {
    companion object {
        @ClassRule
        @JvmField
        var deviceAnimationTestRule = DeviceAnimationTestRule()
    }

    @Rule
    @JvmField
    var mActivityRule = ActivityTestRule(LaunchActivity::class.java)

    @Test
    fun _1_Verify_Open_And_Close_Users() {
        onView(withId(R.id.drawerLayout)).perform(openDrawer())
        onView(withId(R.id.navigationView)).check(matches(isDisplayed()))
        onView(withId(R.id.navigationView)).perform(NavigationViewActions.navigateTo(R.id.drawer_users))

        onView(withId(R.id.drawerLayout)).perform(closeDrawer())
    }

    @Test
    fun _2_Verify_Open_And_Close_Search_User() {
        onView(withId(R.id.drawerLayout)).perform(openDrawer())
        onView(withId(R.id.navigationView)).check(matches(isDisplayed()))

        onView(withId(R.id.navigationView)).perform(NavigationViewActions.navigateTo(R.id.drawer_find_user))

        onView(withId(R.id.drawerLayout)).perform(closeDrawer())
    }
}
