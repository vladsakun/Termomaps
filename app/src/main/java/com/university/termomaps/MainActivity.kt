package com.university.termomaps

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.university.termomaps.database.TermoMarker
import com.university.termomaps.databinding.ActivityMainBinding
import com.university.termomaps.map.TermoMapFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private val viewModel: MainActivityViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.content)) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }

    // Use binding to access views
    val toggle = ActionBarDrawerToggle(
      this,
      binding.drawerLayout,
      R.string.navigation_drawer_open,
      R.string.navigation_drawer_close
    )
    binding.btnMenu.setOnClickListener {
      // Invert drawerLayout state
      if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
      } else {
        binding.drawerLayout.openDrawer(GravityCompat.START)
      }
    }
    binding.drawerLayout.addDrawerListener(toggle)
    toggle.syncState()

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.fragment_container, TermoMapFragment())
        .commit()
    }

    val uri = intent.data
    if (uri != null) {
      val inputStream = contentResolver.openInputStream(uri) ?: return
      val json = inputStream.bufferedReader().use { it.readText() }
      viewModel.importMarkers(json)
    }

    binding.navView.setNavigationItemSelectedListener { menuItem ->
      // Handle navigation view item clicks here.
      when (menuItem.itemId) {
        R.id.nav_first_item -> {
          // Handle the click on the first item
        }
        // Handle other items
      }
      binding.drawerLayout.closeDrawer(GravityCompat.START)
      true
    }
  }

  override fun onBackPressed() {
    if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
      binding.drawerLayout.closeDrawer(GravityCompat.START)
    } else {
      super.onBackPressed()
    }
  }
}