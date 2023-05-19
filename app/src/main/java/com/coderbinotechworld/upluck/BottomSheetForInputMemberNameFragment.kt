package com.coderbinotechworld.upluck

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.coderbinotechworld.upluck.ViewModels.CreateGroupViewModel
import com.coderbinotechworld.upluck.databinding.FragmentBottomSheetForInputMemberNameBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetForInputMemberNameFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetForInputMemberNameBinding
    private lateinit var createGroupViewModel: CreateGroupViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity()
        createGroupViewModel = ViewModelProvider(activity)[CreateGroupViewModel::class.java]

        binding.btnAdd.setOnClickListener {

            if (binding.etNameOfMember.text.toString().isNotEmpty())
                addMember()
            else
                Toast.makeText(activity.applicationContext, "Please Enter Name of Member", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBottomSheetForInputMemberNameBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun addMember() {

        createGroupViewModel.name.value = binding.etNameOfMember.text.toString()
        binding.etNameOfMember.setText("")
        dismiss()

    }



}