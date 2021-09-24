package com.zakaryan.myretailcrm.di.modules

import android.content.Context
import com.zakaryan.myretailcrm.base.http.HttpManager
import com.zakaryan.myretailcrm.base.http.HttpManagerImpl
import com.zakaryan.myretailcrm.data.http.AuthManager
import com.zakaryan.myretailcrm.data.http.ExtendedHttpManager
import com.zakaryan.myretailcrm.data.http.ExtendedHttpManagerImpl
import com.zakaryan.myretailcrm.data.http.HttpManagerProxy
import com.zakaryan.myretailcrm.di.scopes.AppScope
import com.zakaryan.myretailcrm.oauth.TokenManager
import dagger.Module
import dagger.Provides

@Module(includes = [TokenModule::class])
object HttpModule {

    @JvmStatic
    @AppScope
    @Provides
    fun provideHttpManager(context: Context): HttpManager = HttpManagerImpl(context)


    @JvmStatic
    @AppScope
    @Provides
    fun provideExtendedHttpManager(
        httpManager: HttpManager, tokenManager: TokenManager
    ): ExtendedHttpManager = ExtendedHttpManagerImpl(httpManager, tokenManager)


    @JvmStatic
    @AppScope
    @Provides
    fun provideAuthManager(
        extendedHttpManager: ExtendedHttpManager
    ): AuthManager = extendedHttpManager


    @JvmStatic
    @AppScope
    @Provides
    fun provideHttpManagerProxy(
        extendedHttpManager: ExtendedHttpManager
    ): HttpManagerProxy = extendedHttpManager

}