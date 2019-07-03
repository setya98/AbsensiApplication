package com.pertamina.absensiapplication.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.pertamina.absensiapplication.R
import com.pertamina.absensiapplication.databinding.ActivityMainBinding
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {
    private val auth by inject<FirebaseAuth>()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_main
        )
        val topDestination = setOf(R.id.dashboard_dest,
                R.id.profile_dest,
                R.id.login_dest,
                R.id.history_dest)
        navController = findNavController(R.id.football_nav_fragment)
        appBarConfiguration = AppBarConfiguration(topDestination)
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.mainBottomNav.setupWithNavController(navController)
        OnDestinationChanged()
    }

    private fun OnDestinationChanged() =
            navController.addOnDestinationChangedListener { _, destination, _ ->
                val destId = destination.id
                if (destId == R.id.dashboard_dest || destId == R.id.history_dest || destId == R.id.profile_dest) {
                    supportActionBar?.hide()
                    binding.mainBottomNav.visibility = View.VISIBLE
                } else {
                    supportActionBar?.show()
                    supportActionBar?.title = ""
                    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_arrow_left)
                    binding.mainBottomNav.visibility = View.GONE
                }
            }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authStateListener)
    }

    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        if (auth.currentUser == null) {
            navController.navigate(R.id.login_dest, null, NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_in_right)
                    .setExitAnim(R.anim.slide_out_left)
                    .setPopEnterAnim(R.anim.slide_in_left)
                    .setPopExitAnim(R.anim.slide_out_right)
                    .setPopUpTo(R.id.dashboard_dest, true).build())
        }
    }
}