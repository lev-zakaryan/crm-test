package com.zakaryan.myretailcrm.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.zakaryan.myretailcrm.R
import com.zakaryan.myretailcrm.databinding.ActivityMainBinding
import com.zakaryan.myretailcrm.ui.MyRetailCrmApp
import com.zakaryan.myretailcrm.ui.activity.viewmodel.MainViewModel
import com.zakaryan.myretailcrm.ui.viewmodel.SavedStateViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var mainViewModelFactory: MainViewModel.Factory

    private val mainViewModel by viewModels<MainViewModel> {
        SavedStateViewModelFactory(mainViewModelFactory, this)
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.AuthFragment, R.id.ListFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)

        MyRetailCrmApp
            .get(this).appComponent
            .mainComponent()
            .create()
            .inject(this)

        initializeObservers()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun initializeObservers() {
        mainViewModel.isAuthorizedLiveData.observe(this) { isAuthorized ->
            if (!isAuthorized) {
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                navController.navigate(R.id.action_global_to_Auth_Graph)
            }
        }
    }
}