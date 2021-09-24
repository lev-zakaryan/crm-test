package com.zakaryan.myretailcrm.di.components

import android.content.Context
import com.zakaryan.myretailcrm.di.modules.AppModule
import com.zakaryan.myretailcrm.di.modules.AppModuleBinds
import com.zakaryan.myretailcrm.di.modules.HttpModule
import com.zakaryan.myretailcrm.di.scopes.AppScope
import com.zakaryan.myretailcrm.ui.activity.di.MainComponent
import com.zakaryan.myretailcrm.ui.fragment.auth.di.AuthComponent
import com.zakaryan.myretailcrm.ui.fragment.details.di.DetailsComponent
import com.zakaryan.myretailcrm.ui.fragment.list.di.ListComponent
import dagger.BindsInstance
import dagger.Component
import dagger.Module

@AppScope
@Component(
    modules = [
        AppModule::class,
        AppModuleBinds::class,
        HttpModule::class,
        SubcomponentsModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    fun mainComponent(): MainComponent.Factory
    fun authComponent(): AuthComponent.Factory
    fun listComponent(): ListComponent.Factory
    fun detailsComponent(): DetailsComponent.Factory

}

@Module(
    subcomponents = [
        AuthComponent::class,
        ListComponent::class,
        DetailsComponent::class
    ]
)
object SubcomponentsModule