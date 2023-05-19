package com.coderbinotechworld.upluck

import android.content.Intent
import android.graphics.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.View
import android.view.animation.AnimationUtils
import com.coderbinotechworld.upluck.ViewModels.GroupMembers
import com.coderbinotechworld.upluck.databinding.ActivitySpinBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SpinActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySpinBinding
    private lateinit var memberList: ArrayList<String>
    private lateinit var randomMember: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        memberList = bundle?.getStringArrayList("members") as ArrayList<String>

        binding.tvGName.text = bundle?.getCharSequence("groupName")

        startSelectingRandomMembers(memberList)

        binding.btnHome.setOnClickListener {
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(mainActivityIntent)
        }

        binding.btnShare.setOnClickListener {
            // share the result
            val bitmap = takeScreenshot()

            // show the Buttons and other views after take screenshot
            binding.btnShare.visibility = View.VISIBLE
            binding.btnHome.visibility = View.VISIBLE
            binding.tvStatus.visibility = View.VISIBLE
            binding.tvProgressPercentage.visibility = View.VISIBLE
            binding.cpb.visibility = View.VISIBLE

            val uri = getImageUri(bitmap)
            shareImage(uri, binding.tvGName.text.toString(), randomMember)
        }

    }

    // Function to take a screenshot of the current view
    private fun takeScreenshot(): Bitmap {
        // hide button and other view before taking screenshot
        binding.btnShare.visibility = View.GONE
        binding.btnHome.visibility = View.GONE
        binding.tvStatus.visibility = View.GONE
        binding.tvProgressPercentage.visibility = View.GONE
        binding.cpb.visibility = View.GONE

//        change the height of view

        val screenshot = Bitmap.createBitmap(
            binding.root.width,
            binding.root.height - 480,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(screenshot)
        canvas.drawColor(Color.WHITE) // Set canvas background to white
        binding.root.draw(canvas)

        // this paint is for date and time
        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 48f
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
        }
        val currentDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(Date())
        canvas.drawText("Selection Date : $currentDate", canvas.width/2f, canvas.height - 150f, paint)
        canvas.drawText("Selection Time : $currentTime", canvas.width/2f, canvas.height - 90f, paint)

        // this paint is for group members
        val paintForMembers = Paint().apply {
            color = Color.BLUE
            textSize = 48f
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.LEFT
        }

        var memberHeight = 1110f

        for (i in 0 until memberList.size){
            canvas.drawText("Member ${i+1} \t: ${memberList[i]}", canvas.width/4f, canvas.height - memberHeight, paintForMembers)
            memberHeight -= 60f
        }

        return screenshot
    }

    // Function to get the URI of the image
    private fun getImageUri(bitmap: Bitmap): Uri {
        val path = MediaStore.Images.Media.insertImage(
            contentResolver,
            bitmap,
            "screenshot_${System.currentTimeMillis()}.png",
            null
        )
        return Uri.parse(path)
    }

    // Function to share the image
    private fun shareImage(uri: Uri, groupName: String, selectedMember: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.putExtra(Intent.EXTRA_TEXT, "Group Name: $groupName \nSelected Member: $selectedMember")
        startActivity(Intent.createChooser(intent, "Share Result via"))
    }


    private fun startSelectingRandomMembers(mList: ArrayList<String>?) {

        randomMember = memberList.random()
        randomMember = memberList.random()
        randomMember = memberList.random()

        binding.btnHome.visibility = View.GONE
        binding.tvStatus.text = getString(R.string.selection_in_progress)

        startCircularProgressBar()


    }

    private fun startCircularProgressBar() {

        var currentIndex = 0
        var initialProgressPercentage  = 0
        binding.cpb.progressMax = 600 * memberList.size.toFloat()

        val animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val animationZoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in)

        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            private val startTime = System.currentTimeMillis()
            override fun run() {

                val elapsedTime = System.currentTimeMillis() - startTime

                if (elapsedTime < 600 * memberList.size){
                    binding.tvMember.text = memberList[currentIndex]
                    binding.tvMember.startAnimation(animationFadeIn)
                    binding.tvMember.startAnimation(animationFadeOut)

                    currentIndex = (currentIndex + 1) % memberList.size
                    binding.cpb.apply {
                        progress = (initialProgressPercentage).toFloat()
                    }
                    binding.tvProgressPercentage.text = "${(binding.cpb.progress / binding.cpb.progressMax * 100).toInt()}%"
                    initialProgressPercentage += 300
                    handler.postDelayed(this, 300)
                }else{
                    initialProgressPercentage = 600 * memberList.size
                    binding.tvMember.startAnimation(animationZoomIn)
                    binding.cpb.apply {
                        progress = (initialProgressPercentage).toFloat()
                    }
                    binding.tvProgressPercentage.text = "${(binding.cpb.progress / binding.cpb.progressMax * 100).toInt()}%"
                    binding.tvMember.text = randomMember
                    binding.tvStatus.text = getString(R.string.selection_completed)

                    // Home Button enabled
                    binding.btnHome.visibility = View.VISIBLE

                    // Share Button enabled
                    binding.btnShare.visibility = View.VISIBLE
                }


            }

        }

        handler.post(runnable)


    }
}