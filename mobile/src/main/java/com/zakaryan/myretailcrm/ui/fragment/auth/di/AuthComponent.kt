package com.zakaryan.myretailcrm.ui.fragment.auth.di

import com.zakaryan.myretailcrm.ui.fragment.auth.AuthFragment
import com.zakaryan.myretailcrm.ui.fragment.list.di.ListComponent
import dagger.Subcomponent

@Subcomponent
interface AuthComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AuthComponent
    }

    fun inject(fragment: AuthFragment)

}