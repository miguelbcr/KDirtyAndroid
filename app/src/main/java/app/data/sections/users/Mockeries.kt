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

package app.data.sections.users

import com.google.common.truth.Truth.assertThat
import io.victoralbertos.mockery.api.built_in_mockery.DTOArgs


class UserDTO : DTOArgs.Behaviour<User> {
    override fun validate(candidate: User) {
        assertThat(candidate).isNotNull()
        assertThat(candidate.id).isNotEqualTo(0)
        assertThat(candidate.login).isNotEmpty()
    }

    override fun legal(args: Array<out Any>): User {
        val userName = args[0] as String
        val user = User(1, userName, "https://cdn0.iconfinder.com/data/icons/octicons/1024/mark-github-256.png")
        return user
    }
}

class UsersDTO : DTOArgs.Behaviour<List<User>> {
    override fun validate(candidate: List<User>) {
        assertThat(candidate).isNotNull()
        assertThat(candidate).isNotEmpty()

        val (id, login) = candidate[0]
        assertThat(id).isNotEqualTo(0)
        assertThat(login).isNotEmpty()
    }

    override fun legal(args: Array<out Any>): List<User> {
        val lastIdQueried = args[0] as Int + 1
        var perPage = args[1] as Int
        perPage = if (perPage == 0) 30 else perPage

        return (lastIdQueried..lastIdQueried + perPage).map {
            User(it, "User " + it, "https://cdn0.iconfinder.com/data/icons/octicons/1024/mark-github-256.png")
        }
    }
}

