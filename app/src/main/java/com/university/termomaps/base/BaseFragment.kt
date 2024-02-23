package com.university.termomaps.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.university.termomaps.R

abstract class BaseFragment<T : ViewBinding> : Fragment() {

  private var _binding: T? = null
  protected val binding get() = _binding!!

  abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): T

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = inflateBinding(inflater, container)
    return binding.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  protected fun back() {
    requireActivity().onBackPressedDispatcher.onBackPressed()
  }

  protected fun replaceWithBackStack(fragment: Fragment) {
    requireActivity()
      .supportFragmentManager
      .beginTransaction()
      .replace(R.id.fragment_container, fragment)
      .addToBackStack(fragment::class.java.canonicalName)
      .commit()
  }
}