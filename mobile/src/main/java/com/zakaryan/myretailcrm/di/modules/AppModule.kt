package com.zakaryan.myretailcrm.di.modules

import android.content.Context
import com.zakaryan.myretailcrm.data.http.ExtendedHttpManager
import com.zakaryan.myretailcrm.data.source.LocalDataSource
import com.zakaryan.myretailcrm.data.source.RemoteDataSource
import com.zakaryan.myretailcrm.data.source.local.LocalDataSourceImpl
import com.zakaryan.myretailcrm.data.source.remote.RemoteDataSourceImpl
import com.zakaryan.myretailcrm.di.scopes.AppScope
import dagger.Module
import dagger.Provides

@Module
object AppModule {

    @JvmStatic
    @AppScope
    @Provides
    fun provideRemoteDataSource(extendedHttpManager: ExtendedHttpManager): RemoteDataSource {
        return RemoteDataSourceImpl(extendedHttpManager)
    }

    @JvmStatic
    @AppScope
    @Provides
    fun provideLocalDataSource(context: Context): LocalDataSource {
        return LocalDataSourceImpl(context)
    }
}