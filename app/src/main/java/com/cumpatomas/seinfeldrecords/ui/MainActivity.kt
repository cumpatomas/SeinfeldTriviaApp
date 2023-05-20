package com.cumpatomas.seinfeldrecords.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.cumpatomas.seinfeldrecords.data.database.ApplicationModule
import com.cumpatomas.seinfeldrecords.databinding.ActivityMainBinding
import com.cumpatomas.seinfeldrecords.R
import com.cumpatomas.seinfeldrecords.domain.ScrapScripts
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.jsoup.nodes.Document


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    val scraping = ScrapScripts()



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
//        scrapingService()

    }

    private fun setAppBarConfig() {
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fragment2, // setea el fragment como HOME
                R.id.CharListFragment, // setea el fragment como HOME

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


