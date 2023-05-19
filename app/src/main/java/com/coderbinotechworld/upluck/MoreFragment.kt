package com.coderbinotechworld.upluck

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.coderbinotechworld.upluck.databinding.FragmentMoreBinding
import com.google.android.material.snackbar.Snackbar

class MoreFragment : Fragment() {

    private lateinit var binding: FragmentMoreBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnOpenInstagramOfRanjana.setOnClickListener {
            context?.let { it1 -> openInstagram(it1, "iranjanasahu") }
        }

        binding.btnOpenInstagramOfBikash.setOnClickListener {
            context?.let { it1 -> openInstagram(it1, "bwalabikash") }
        }

        binding.btnManageGroup.setOnClickListener {
            clickEffect(binding.btnManageGroup, R.color.profile_gray)
            Snackbar.make(binding.root, "Manage Group is in working stage!", Snackbar.LENGTH_SHORT).show()
        }

        binding.btnCreateGroup.setOnClickListener {

            clickEffect(binding.btnCreateGroup, R.color.profile_gray)

            val intent = Intent(context, CreateGroupActivity::class.java)
            startActivity(intent)
        }

        binding.btnHistory.setOnClickListener {
            clickEffect(binding.btnHistory, R.color.profile_gray)
            Snackbar.make(binding.root, "History feature is for Sem-4!", Snackbar.LENGTH_SHORT).show()
        }

        binding.btnShareTheApp.setOnClickListener {
            clickEffect(binding.btnShareTheApp, R.color.profile_gray)
            shareApp()
        }

        binding.btnRateUs.setOnClickListener {
            clickEffect(binding.btnRateUs, R.color.profile_gray)
            rateUsOnPlayStoreCode()
        }

        binding.btnFeedback.setOnClickListener {
            clickEffect(binding.btnFeedback, R.color.profile_gray)
            giveFeedbackThroughMailApp()
        }

    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun giveFeedbackThroughMailApp(){
        val deviceName = Build.MODEL // Get the device name
        val appName = getString(R.string.app_name) // Get the app name from string resources

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("bwalabikash@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Feedback for $appName")
            putExtra(Intent.EXTRA_TEXT, "Device name: $deviceName\n\n" +
                    "Write feedback:\n\n")
        }

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(Intent.createChooser(intent, "Send email"))
        } else {
            Toast.makeText(
                requireContext(),
                "No email app found",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun rateUsOnPlayStoreCode(){
        val packageName = requireContext().packageName
        val uri = Uri.parse("market://details?id=$packageName")
        val playStoreIntent = Intent(Intent.ACTION_VIEW, uri)
        playStoreIntent.setPackage("com.android.vending") // Set Play Store app package name
        playStoreIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            startActivity(playStoreIntent)
        } catch (e: ActivityNotFoundException) {
            // If Play Store app is not installed, open the Play Store website
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
            startActivity(webIntent)
        }


    }

    private fun shareApp(){
        val appName = getString(R.string.app_name)
        val packageName = requireContext().packageName

// Create the share intent
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this app!")
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Do you ever struggle with deciding who gets to choose the movie or restaurant? " +
                    "\n\n$appName is the solution! It's a fun and easy app that selects a random member from a group. " +
                    "\n\nDownload $appName now from the Play Store: \nhttps://play.google.com/store/apps/details?id=$packageName"
        )

// Show the share dialog
        startActivity(Intent.createChooser(shareIntent, "Share with Friends"))
    }

    private fun clickEffect(view: View, color: Int) {
        val viewColor = view.solidColor

        view.setBackgroundColor(ContextCompat.getColor(requireContext(), color))
        Handler(Looper.getMainLooper()).postDelayed({
            view.setBackgroundColor(viewColor)
        }, 30)
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun openInstagram(context: Context, profileName: String) {

        // create an intent to open Instagram with the specified profile name
        val instagramIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/$profileName"))
        instagramIntent.setPackage("com.instagram.android")

        // verify that the Instagram app is installed on the user's device
        val packageManager = context.packageManager
        if (instagramIntent.resolveActivity(packageManager) != null) {
            startActivity(instagramIntent)
        } else {
            // Instagram is not installed, open the profile in a browser
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/$profileName"))
            startActivity(webIntent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding.root
    }
}