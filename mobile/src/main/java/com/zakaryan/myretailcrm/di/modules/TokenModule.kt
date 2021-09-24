package com.zakaryan.myretailcrm.di.modules

import android.content.Context
import com.zakaryan.myretailcrm.di.scopes.AppScope
import com.zakaryan.myretailcrm.oauth.TokenManager
import com.zakaryan.myretailcrm.oauth.TokenManagerImpl
import dagger.Module
import dagger.Provides

@Module
object TokenModule {

    @JvmStatic
    @AppScope
    @Provides
    fun provideTokenManager(context: Context): TokenManager = TokenManagerImpl(context)

}