/*
 * Copyright 2017 Victor Albertos
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

package app.presentation.sections.users.list

import app.data.sections.users.User
import app.data.sections.users.UserRepository
import app.presentation.foundation.transformations.Transformations
import app.presentation.foundation.widgets.Notifications
import app.presentation.sections.TransformationsMock
import app.presentation.sections.users.UsersWireframe
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Observable
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import miguelbcr.ok_adapters.recycler_view.Pager
import org.junit.Before
import org.junit.Test

class UsersPresenterTest {
    val USER_ID = 1
    val transformations: Transformations = spy(TransformationsMock())
    val userRepository: UserRepository = mock()
    val wireframe: UsersWireframe = mock()
    val view: UsersPresenter.View = mock()
    var callback: Pager.Callback<User> = mock()
    val notifications: Notifications = mock()
    lateinit var usersPresenterUT: UsersPresenter

    @Before fun init() {
        usersPresenterUT = UsersPresenter(userRepository, wireframe).bind(view) as UsersPresenter
      usersPresenterUT.transformations = transformations
        whenever(view.userSelectedClicks()).thenReturn(Observable.never())
    }

    @Test fun When_Call_OnBindView_Then_SetUpLoaderPager_Is_Called() {
        usersPresenterUT.onCreate()
        verify(view).setUpLoaderPager(any(), any())
    }

    @Test fun When_Call_OnBindView_Then_SetUpRefreshList_Is_Called() {
        usersPresenterUT.onCreate()
        verify(view).setUpRefreshList(any())
    }

    @Test fun When_Call_OnBindView_Then_UserSelectedClicks_Is_Called() {
        usersPresenterUT.onCreate()
        verify(view).userSelectedClicks()
    }

    @Test fun When_Call_NextPage_Second_Time_Then_UsersState_Preserve_Users() {
        mockSuccessResponse()
        usersPresenterUT.onCreate()
        usersPresenterUT.nextPage(aUser(), callback)
        usersPresenterUT.nextPage(aUser(), callback)

        assertEquals(usersPresenterUT.users.size, 2)
    }

    @Test fun When_Call_RefreshList_Then_UsersState_Is_Cleared() {
        mockSuccessResponse()
        usersPresenterUT.onCreate()
        usersPresenterUT.nextPage(aUser(), callback)
        usersPresenterUT.nextPage(aUser(), callback)
        usersPresenterUT.refreshList(callback)

        assertEquals(usersPresenterUT.users.size, 1)
    }

    @Test fun When_Call_NextPage_With_Null_User_Then_Id_Is_Null() {
        mockSuccessResponse()
        usersPresenterUT.onCreate()

        usersPresenterUT.nextPage(null, callback)
        verify(userRepository).getUsers(null, false)
    }

    @Test fun Verify_NextPage_With_Success_Response() {
        mockSuccessResponse()
        usersPresenterUT.onCreate()
        usersPresenterUT.nextPage(aUser(), callback)

        verify(userRepository).getUsers(USER_ID, false)
        verify(transformations).safely<List<User>>()
        verify(transformations).reportOnSnackBar<List<User>>()
        verify(callback).supply(any())
    }

    @Test fun Verify_NextPage_With_Error_Response() {
        mockErrorResponse()
        usersPresenterUT.onCreate()
        usersPresenterUT.nextPage(aUser(), callback)

        verify(userRepository).getUsers(USER_ID, false)
        verify(transformations).safely<List<User>>()
        verify(transformations).reportOnSnackBar<List<User>>()
        verify(callback, never()).supply(any())
    }

    @Test fun Verify_RefreshList_With_Success_Response() {
        mockSuccessResponse()
        usersPresenterUT.onCreate()
        usersPresenterUT.refreshList(callback)

        verify(userRepository).getUsers(null, true)
        verify(transformations).safely<List<User>>()
        verify(transformations).reportOnSnackBar<List<User>>()
        verify(callback).supply(any())
        assertEquals(usersPresenterUT.users.size, 1)
    }

    @Test fun Verify_RefreshList_With_Error_Response() {
        mockErrorResponse()
        usersPresenterUT.onCreate()
        usersPresenterUT.refreshList(callback)

        verify(userRepository).getUsers(null, true)
        verify(transformations).safely<List<User>>()
        verify(transformations).reportOnSnackBar<List<User>>()
        verify(callback, never()).supply(any())
    }

    private fun mockSuccessResponse() {
        whenever(userRepository.getUsers(anyOrNull(), any()))
                .thenReturn(Single.just(listOf(aUser())))
    }

    private fun aUser(): User {
        return User(USER_ID, "user", "avatar")
    }

    private fun mockErrorResponse() {
        whenever(userRepository.getUsers(anyOrNull(), any()))
                .thenReturn(Single.error(RuntimeException()))
    }
}
