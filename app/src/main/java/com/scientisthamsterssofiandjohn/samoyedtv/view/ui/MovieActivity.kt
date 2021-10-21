package com.scientisthamsterssofiandjohn.samoyedtv.view.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.scientisthamsterssofiandjohn.samoyedtv.R
import com.scientisthamsterssofiandjohn.samoyedtv.databinding.ActivityMovieBinding
import dagger.android.support.DaggerAppCompatActivity

class MovieActivity : DaggerAppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityMovieBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        navController = findNavController(R.id.movieNavHostFragment)

        navController.addOnDestinationChangedListener(this)
        binding.bottomNavigationView.setupWithNavController(findNavController(R.id.movieNavHostFragment))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return configureToolbar(menu)
    }

    private fun configureToolbar(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.common_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                navController.navigate(R.id.findFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.detailFragment -> {
                binding.toolbar.visibility = View.GONE
                binding.bottomNavigationView.visibility = View.GONE
            }
            R.id.findFragment -> {
                binding.profileImage.visibility = View.GONE
                binding.tvToolbarTitle.text = destination.label
            }
            else -> {
                binding.toolbar.visibility = View.VISIBLE
                binding.profileImage.visibility = View.VISIBLE
                binding.bottomNavigationView.visibility = View.VISIBLE
                binding.tvToolbarTitle.text = destination.label
            }
        }
    }
}