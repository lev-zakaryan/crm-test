package com.zakaryan.myretailcrm.di.modules

import com.zakaryan.myretailcrm.data.DefaultRepository
import com.zakaryan.myretailcrm.data.Repository
import com.zakaryan.myretailcrm.di.scopes.AppScope
import dagger.Binds
import dagger.Module

@Module
abstract class AppModuleBinds {

    @AppScope
    @Binds
    abstract fun bindAuthRepository(repo: DefaultRepository): Repository
}
