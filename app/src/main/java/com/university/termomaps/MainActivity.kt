package com.university.termomaps

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.university.termomaps.databinding.ActivityMainBinding
import com.university.termomaps.features.list.MapListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private val viewModel: MainActivityViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    //    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragment_container)) { v, insets ->
    //      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
    //      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
    //      insets
    //    }

    if (savedInstanceState == null) {
      supportFragmentManager
        .beginTransaction()
        .replace(R.id.fragment_container, MapListFragment.newInstance())
        .addToBackStack(null)
        .commit()
    }

    val uri = intent.data

    if (uri != null) {
      val inputStream = contentResolver.openInputStream(uri) ?: return
      val json = inputStream.bufferedReader().use { it.readText() }
      viewModel.importMap(json)
    }
  }
}