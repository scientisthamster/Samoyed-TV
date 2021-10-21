package com.scientisthamsterssofiandjohn.samoyedtv.view.ui.account

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.scientisthamsterssofiandjohn.samoyedtv.R
import com.scientisthamsterssofiandjohn.samoyedtv.databinding.FragmentAccountBinding

class AccountFragment : Fragment(R.layout.fragment_account) {

    private lateinit var binding: FragmentAccountBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAccountBinding.bind(view)
        setupBackArrow()
    }

    private fun setupBackArrow() {
        binding.imBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

}