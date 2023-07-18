package com.cumpatomas.seinfeldrecords.ui

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var coffeeDialog: RoundedDialog
    private lateinit var starDialog: RoundedDialog

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
        initListeners()
        setupActionBarWithNavController(navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.topAppBar, navController)
        alertsDialogSetUp()
    }

    private fun alertsDialogSetUp() {
        coffeeDialog = RoundedDialog(
            "Don't be a bad tipper...buy me a coffee!",
            "Buy",
            "https://paypal.me/cumpatomas"
        )
        starDialog = RoundedDialog(
            "Here's for those who like the app\n" +
                    "and those who don't... can go back",
            "Rate",
            "https://play.google.com/store/apps/details?id=com.cumpatomas.seinfeldrecords"
        )
    }

    private fun initListeners() {
        binding.ivShareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            val body = "Don't keep this app in a vault, share it!"
            val sub = "https://play.google.com/store/apps/details?id=com.cumpatomas.seinfeldrecords"
            intent.putExtra(Intent.EXTRA_TEXT, body)
            intent.putExtra(Intent.EXTRA_TEXT, sub)
            startActivity(Intent.createChooser(intent, "Don't keep this app in a vault, share it!"))
        }

        binding.ivCoffeeButton.setOnClickListener {
            coffeeDialog.show(supportFragmentManager, "Coffee")
        }

        binding.ivStarButton.setOnClickListener {
            starDialog.show(supportFragmentManager, "Rate App")
        }
    }

    private fun setAppBarConfig() {
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.CharListFragment,
                R.id.HomeFragment,
                R.id.QuizFragment,
                R.id.QuotesFragment,
                R.id.charGesturesFragment,
            ),
        )
    }

    // Funcion para quitar la barra de Menu de los fragmentos que nos son topLevel
    private fun setNavigationIcon() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (!appBarConfiguration.topLevelDestinations.contains(destination.id)) {
                binding.topAppBar.isGone = true
                binding.toolBar.navigationIcon = null
            } else {
                binding.topAppBar.isVisible = true
            }
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.charGesturesFragment) {
                binding.topAppBar.isGone = true
            }
        }
    }

    private fun setNavHostFragment() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(com.cumpatomas.seinfeldrecords.R.id.fragmentsContainer) as NavHostFragment
        navController = navHostFragment.findNavController()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}

class RoundedDialog(
    private val messageToShow: String,
    private val okButton: String,
    private val link: String,
) :
    DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(requireActivity(), R.style.RoundedCornersDialog)
            .setMessage(messageToShow)
            .setPositiveButton(
                okButton
            ) { dialog, _ ->
                val url = link
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .create()

    override fun getTheme() = R.style.DialogCorners
}

