package com.coderbinotechworld.upluck

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.coderbinotechworld.upluck.ViewModels.SelectProfileColorViewModel
import com.coderbinotechworld.upluck.databinding.FragmentBottomSheetForSelectProfileColorBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetForSelectProfileColorFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetForSelectProfileColorBinding
    private lateinit var selectProfileColorViewModel: SelectProfileColorViewModel

    private var selectedProfileColor: String = "Gray"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity()
        selectProfileColorViewModel = ViewModelProvider(activity)[SelectProfileColorViewModel::class.java]

        binding.vGray.setOnClickListener {
            selectedProfileColor = "Gray"
            binding.btnAdd.setBackgroundResource(R.color.profile_gray)
        }

        binding.vGreen.setOnClickListener {
            selectedProfileColor = "Green"
            binding.btnAdd.setBackgroundResource(R.color.profile_green)
        }

        binding.vPink.setOnClickListener {
            selectedProfileColor = "Pink"
            binding.btnAdd.setBackgroundResource(R.color.profile_pink)
        }

        binding.vRed.setOnClickListener {
            selectedProfileColor = "Red"
            binding.btnAdd.setBackgroundResource(R.color.profile_red)
        }

        binding.vTulip.setOnClickListener {
            selectedProfileColor = "Tulip"
            binding.btnAdd.setBackgroundResource(R.color.profile_tulip)
        }

        binding.vYellow.setOnClickListener {
            selectedProfileColor = "Yellow"
            binding.btnAdd.setBackgroundResource(R.color.profile_yellow)
        }

        binding.btnAdd.setOnClickListener {
            setProfileColor()
        }

    }

    private fun setProfileColor() {
        selectProfileColorViewModel.name.value = selectedProfileColor
        dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBottomSheetForSelectProfileColorBinding.inflate(inflater, container, false)

        return binding.root
    }

}