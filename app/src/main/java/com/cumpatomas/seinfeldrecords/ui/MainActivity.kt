package com.cumpatomas.seinfeldrecords.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.cumpatomas.seinfeldrecords.R
import com.cumpatomas.seinfeldrecords.data.database.ApplicationModule
import com.cumpatomas.seinfeldrecords.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ApplicationModule.initialiseApplicationContext(this.application)
        setSupportActionBar(binding.toolBar)
        setNavHostFragment()
        setAppBarConfig()
        setNavigationIcon()
        setupActionBarWithNavController(navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.topAppBar, navController)

    }

    private fun setAppBarConfig() {
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.HomeFragment,
                R.id.CharListFragment,
                R.id.QuizFragment,

            )
        )
    }

    // Funcion para quitar la barra de Menu de los fragmentos que nos son topLevel
    private fun setNavigationIcon() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (!appBarConfiguration.topLevelDestinations.contains(destination.id)) {
                binding.topAppBar.isGone = true
            } else {
                binding.topAppBar.isVisible = true
            }
        }
    }

    private fun setNavHostFragment() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentsContainer) as NavHostFragment
        navController = navHostFragment.findNavController()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}


