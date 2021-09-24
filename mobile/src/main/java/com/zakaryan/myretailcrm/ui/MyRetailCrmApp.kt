package com.zakaryan.myretailcrm.ui

import android.app.Activity
import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.zakaryan.myretailcrm.di.components.AppComponent
import com.zakaryan.myretailcrm.di.components.DaggerAppComponent

class MyRetailCrmApp : MultiDexApplication() {

    val appComponent: AppComponent by lazy { initComponent() }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun initComponent(): AppComponent {
        return DaggerAppComponent.factory().create(this)
    }

    companion object {
        fun get(activity: Activity): MyRetailCrmApp = activity.application as MyRetailCrmApp
        fun get(context: Context): MyRetailCrmApp = context.applicationContext as MyRetailCrmApp
    }

}