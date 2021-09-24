package com.zakaryan.myretailcrm.data.http

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.zakaryan.myretailcrm.LiveDataTestUtil
import com.zakaryan.myretailcrm.MainCoroutineRule
import com.zakaryan.myretailcrm.base.http.FakeHttpManager
import com.zakaryan.myretailcrm.base.http.model.data.AuthParams
import com.zakaryan.myretailcrm.base.http.model.data.Customer
import com.zakaryan.myretailcrm.base.http.model.request.customers.CustomerRequest
import com.zakaryan.myretailcrm.base.http.model.response.auth.AuthResponse
import com.zakaryan.myretailcrm.base.http.model.response.customers.CustomerResponse
import com.zakaryan.myretailcrm.base.http.result.HttpResult
import com.zakaryan.myretailcrm.oauth.FakeTokenManager
import com.zakaryan.myretailcrm.oauth.model.TokenCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class ExtendedHttpManagerImplTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeHttpManager: FakeHttpManager
    private lateinit var fakeTokenManager: FakeTokenManager

    // тестируем класс ExtendedHttpManagerImpl
    private lateinit var extendedHttpManager: ExtendedHttpManagerImpl

    @ExperimentalCoroutinesApi
    @Before
    fun createExtendedHttpManager() {
        fakeHttpManager = FakeHttpManager()
        fakeTokenManager = FakeTokenManager()
        extendedHttpManager = ExtendedHttpManagerImpl(fakeHttpManager, fakeTokenManager, Dispatchers.Main)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun auth_success() = runBlockingTest {
        // успешная авторизация
        fakeHttpManager.authResult = HttpResult.Success(AuthResponse())
        val result = extendedHttpManager.auth("", AuthParams.fromRefreshToken(""))

        // проверяем, что прокси передал ответ
        assertThat(result).isEqualTo(fakeHttpManager.authResult)

        // проверяем, что live data обновила свое состояние
        assertThat(LiveDataTestUtil.getValue(extendedHttpManager.authState)).isTrue()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun auth_fail_api() = runBlockingTest {
        // авторизация завершается ошибкой от API
        fakeHttpManager.authResult = HttpResult.ApiError("Failed!")
        val result = extendedHttpManager.auth("", AuthParams.fromRefreshToken(""))

        // проверяем, что прокси передал ответ
        assertThat(result).isEqualTo(fakeHttpManager.authResult)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun auth_fail_http() = runBlockingTest {
        // авторизация завершается ошибкой HTTP
        fakeHttpManager.authResult = HttpResult.HttpError(404)
        val result = extendedHttpManager.auth("", AuthParams.fromRefreshToken(""))

        // проверяем, что прокси передал ответ
        assertThat(result).isEqualTo(fakeHttpManager.authResult)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun auth_fail_exception() = runBlockingTest {
        // авторизация завершается исключением при выполнении сетевого запроса
        fakeHttpManager.authResult = HttpResult.Exception(IOException())
        val result = extendedHttpManager.auth("", AuthParams.fromRefreshToken(""))

        // проверяем, что прокси передал ответ
        assertThat(result).isEqualTo(fakeHttpManager.authResult)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun query_after_successful_auth() = runBlockingTest {
        // успешная авторизация
        fakeHttpManager.authResult = HttpResult.Success(AuthResponse())
        extendedHttpManager.auth("", AuthParams.fromRefreshToken(""))

        // успешный запрос (токен принят)
        fakeHttpManager.queryResult = listOf(HttpResult.Success(CustomerResponse(Customer())))
        val result = extendedHttpManager.query(CustomerRequest(""))

        // проверяем, что прокси передал ответ
        assertThat(result).isEqualTo(fakeHttpManager.queryResult[0])
    }

    @ExperimentalCoroutinesApi
    @Test
    fun query_after_failed_auth() = runBlockingTest {
        // неуспешная авторизация
        fakeHttpManager.authResult = HttpResult.Exception(IOException())
        extendedHttpManager.auth("", AuthParams.fromRefreshToken(""))

        // делаем query запрос
        val result = extendedHttpManager.query(CustomerRequest(""))

        // проверяем, что запрос завершился исключением
        assertThat(result).isInstanceOf(HttpResult.Exception::class.java)

        // проверяем, что live data обновила свое состояние
        assertThat(LiveDataTestUtil.getValue(extendedHttpManager.authState)).isFalse()
    }


    @ExperimentalCoroutinesApi
    @Test
    fun query_token_expired_refresh_success() = runBlockingTest {
        // добавляем refresh токен
        fakeTokenManager.saveTokenCache(TokenCache.Access("", "", ""))

        // auth запрос с refresh токеном должен завершиться успехом
        fakeHttpManager.authResult = HttpResult.Success(AuthResponse())
        // делаем query запрос, который сначала вернет HTTP 401, а потом success
        fakeHttpManager.queryResult = listOf(
            HttpResult.HttpError(401),
            HttpResult.Success(CustomerResponse(Customer()))
        )
        val result = extendedHttpManager.query(CustomerRequest(""))

        // проверяем, что успешный ответ получен
        assertThat(result).isEqualTo(fakeHttpManager.queryResult[1])

        // проверяем, что live data обновила свое состояние
        assertThat(LiveDataTestUtil.getValue(extendedHttpManager.authState)).isTrue()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun query_token_expired_refresh_failed() = runBlockingTest {
        // добавляем refresh токен
        fakeTokenManager.saveTokenCache(TokenCache.Access("", "", ""))

        // auth запрос с refresh токеном должен завершиться ошибкой
        fakeHttpManager.authResult = HttpResult.ApiError("")
        // делаем query запрос, который сначала вернет HTTP 401, а второй запрос запущен не будет
        fakeHttpManager.queryResult = listOf(
            HttpResult.HttpError(401),
            HttpResult.Success(CustomerResponse(Customer()))
        )
        val result = extendedHttpManager.query(CustomerRequest(""))

        // проверяем, что прокси передал ошибку полученную при обновлении токена
        assertThat(result).isEqualTo(fakeHttpManager.authResult)

        // проверяем, что live data обновила свое состояние
        assertThat(LiveDataTestUtil.getValue(extendedHttpManager.authState)).isFalse()
    }


}