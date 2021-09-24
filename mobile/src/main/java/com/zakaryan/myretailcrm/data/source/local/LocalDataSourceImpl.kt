package com.zakaryan.myretailcrm.data.source.local

import android.content.Context
import com.zakaryan.myretailcrm.data.source.LocalDataSource
import javax.inject.Inject

class LocalDataSourceImpl
@Inject constructor(private val context: Context) : LocalDataSource {

    // здесь можно загружать что-то из БД при необходимости

}