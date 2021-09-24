package com.zakaryan.myretailcrm.ui.fragment.details.di

import com.zakaryan.myretailcrm.ui.fragment.details.DetailsFragment
import dagger.Subcomponent

@Subcomponent
interface DetailsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): DetailsComponent
    }

    fun inject(fragment: DetailsFragment)

}