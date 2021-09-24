package com.zakaryan.myretailcrm.ui.fragment.list.di

import com.zakaryan.myretailcrm.ui.fragment.list.ListFragment
import dagger.Subcomponent

@Subcomponent
interface ListComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ListComponent
    }

    fun inject(fragment: ListFragment)

}