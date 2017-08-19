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

package app.presentation.sections

import app.presentation.foundation.transformations.Transformations
import io.reactivex.Single
import io.reactivex.SingleTransformer

class TransformationsMock : Transformations {
    override fun <T> safely(): SingleTransformer<T, T> = SingleTransformer { it }

    override fun <T> reportOnSnackBar(): SingleTransformer<T, T> = SingleTransformer {
        it.onErrorResumeNext {
            Single.never()
        }
    }

    override fun <T> reportOnToast(): SingleTransformer<T, T> = SingleTransformer {
        it.onErrorResumeNext {
            Single.never()
        }
    }

    override fun <T> loading(): SingleTransformer<T, T> = SingleTransformer { it }

    override fun <T> loading(content: String): SingleTransformer<T, T> = SingleTransformer { it }
}
