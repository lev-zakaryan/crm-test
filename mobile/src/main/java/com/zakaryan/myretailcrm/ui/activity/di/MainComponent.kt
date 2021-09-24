package com.zakaryan.myretailcrm.ui.activity.di

import com.zakaryan.myretailcrm.ui.activity.MainActivity
import dagger.Subcomponent

@Subcomponent
interface MainComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun inject(activity: MainActivity)

}