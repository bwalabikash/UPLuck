package com.coderbinotechworld.upluck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coderbinotechworld.upluck.adapters.RecyclerViewAdapterForGroupMembers
import com.coderbinotechworld.upluck.RoomDB.Groups
import com.coderbinotechworld.upluck.RoomDB.GroupsDatabase
import com.coderbinotechworld.upluck.ViewModels.CreateGroupViewModel
import com.coderbinotechworld.upluck.ViewModels.GroupMembers
import com.coderbinotechworld.upluck.ViewModels.SelectProfileColorViewModel
import com.coderbinotechworld.upluck.databinding.ActivityEditGroupBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditGroupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditGroupBinding

    private lateinit var selectProfileColorViewModel: SelectProfileColorViewModel
    private lateinit var createGroupViewModel: CreateGroupViewModel

    // for selected profile color
    private var selectedProfileColor: String = "Gray"

    // database reference
    private lateinit var groupsDb: GroupsDatabase

    // for members recycler view
    private lateinit var recyclerView: RecyclerView
    private lateinit var groupMembers: ArrayList<GroupMembers>
    private lateinit var recyclerViewAdapterForGroupMembers: RecyclerViewAdapterForGroupMembers

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        groupMembers = ArrayList()

        groupsDb = GroupsDatabase.getGroupsDatabase(this@EditGroupActivity)

        createGroupViewModel = ViewModelProvider(this)[CreateGroupViewModel::class.java]
        selectProfileColorViewModel = ViewModelProvider(this)[SelectProfileColorViewModel::class.java]

        binding.colorSpinner.setOnClickListener {
            BottomSheetForSelectProfileColorFragment().show(supportFragmentManager, "selectProfileColorTag")
        }

        selectProfileColorViewModel.name.observe(this){

            selectedProfileColor = it

            when(it){
                "Gray" ->{
                    binding.colorSpinner.setBackgroundResource(R.drawable.profile_color_radius_gray)
                }
                "Green" ->{
                    binding.colorSpinner.setBackgroundResource(R.drawable.profile_color_radius_green)
                }
                "Pink" ->{
                    binding.colorSpinner.setBackgroundResource(R.drawable.profile_color_radius_pink)
                }
                "Red" ->{
                    binding.colorSpinner.setBackgroundResource(R.drawable.profile_color_radius_red)
                }
                "Tulip" ->{
                    binding.colorSpinner.setBackgroundResource(R.drawable.profile_color_radius_tulip)
                }
                "Yellow" ->{
                    binding.colorSpinner.setBackgroundResource(R.drawable.profile_color_radius_yellow)
                }
            }

        }

        // for intent extra

        val bundle = intent.extras
        val groupId = bundle?.getInt("groupId")
        val groupName: String? = bundle?.getString("groupName")
        val groupProfileColor = bundle?.getString("groupProfileColor")
        val members: ArrayList<String>? = bundle?.getStringArrayList("members")
        val memberCount: Int? = bundle?.getInt("groupMembersCount")

        binding.etForGroupName.text = Editable.Factory.getInstance().newEditable(groupName)
        if (groupProfileColor != null) {
            setBackgroundColorForGroupProfile(groupProfileColor)
        }

        for (i in 0 until memberCount!!){
            if (members != null) {
                groupMembers.add(GroupMembers(members[i]))
            }
        }


        // for members

        recyclerView = findViewById(R.id.rvForGroupMembers)
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerViewAdapterForGroupMembers = RecyclerViewAdapterForGroupMembers(groupMembers)
        recyclerView.adapter = recyclerViewAdapterForGroupMembers


        // btn add member
        binding.btnAddMember.setOnClickListener {
            if (groupMembers.size > 14){
                Snackbar.make(binding.root, "Maximum 15 Members Allowed to a Group!", Snackbar.LENGTH_SHORT).show()
            }else
                BottomSheetForInputMemberNameFragment().show(supportFragmentManager, "bottomSheet")
        }

        // on member added listener
        createGroupViewModel.name.observe(this){
//            binding.memberName1.text = String.format("Member Name: %s", it)
            onMemberAddedListener(it)
        }

        onMemberDeleteIconClickListener()


        // btn clicks
        binding.btnUpdate.setOnClickListener {
            if (binding.etForGroupName.text.toString().isEmpty()){
                Snackbar.make(binding.root, "Please Write Group Name", Snackbar.LENGTH_SHORT).show()
            }else if(groupMembers.size < 2 || groupMembers.size > 15){
                Snackbar.make(binding.root, "Group members should be minimum 2 or maximum 15", Snackbar.LENGTH_SHORT).show()
            }else if(groupMembers.size == memberCount && selectedProfileColor == groupProfileColor){
                Snackbar.make(binding.root, "No Changes found!", Snackbar.LENGTH_SHORT).show()
            }else{

                val group = Groups(groupId, binding.etForGroupName.text.toString(), groupMembers, groupMembers.size, selectedProfileColor!!)

                CoroutineScope(Dispatchers.IO).launch {
                    groupsDb.groupsDao().updateGroup(group)
                }

                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()

                Toast.makeText(this, " Group Named: \"$groupName\" Updated", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDeleteGroup.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Are you sure want to Delete \"${binding.etForGroupName.text.toString()}\"?")
            builder.setMessage("Once you deleted the group name \"${binding.etForGroupName.text.toString()}\", you have to create new group?")

            // Set up the buttons
            builder.setPositiveButton("OK") { dialog, which ->
                // Do something when the user clicks the OK button
                // For example, you can call a function here

                val group = Groups(groupId, binding.etForGroupName.text.toString(), groupMembers, groupMembers.size, selectedProfileColor!!)

                CoroutineScope(Dispatchers.IO).launch {
                    groupsDb.groupsDao().delete(group)
                }

                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()

                Toast.makeText(this, " Group Named: \"$groupName\" Deleted", Toast.LENGTH_SHORT).show()

            }

            builder.setNegativeButton("Cancel") { dialog, which ->
                // Do something when the user clicks the Cancel button
                Snackbar.make(binding.root, "Operation Canceled!", Snackbar.LENGTH_SHORT).show()
            }

            // Create and show the AlertDialog
            val dialog = builder.create()
            dialog.show()
        }



    }

    private fun setBackgroundColorForGroupProfile(color: String) {
        when (color) {
            "Gray" -> {
                binding.colorSpinner.setBackgroundResource(R.drawable.profile_color_radius_gray)
            }
            "Green" -> {
                binding.colorSpinner.setBackgroundResource(R.drawable.profile_color_radius_green)
            }
            "Pink" -> {
                binding.colorSpinner.setBackgroundResource(R.drawable.profile_color_radius_pink)
            }
            "Red" -> {
                binding.colorSpinner.setBackgroundResource(R.drawable.profile_color_radius_red)
            }
            "Tulip" -> {
                binding.colorSpinner.setBackgroundResource(R.drawable.profile_color_radius_tulip)
            }
            "Yellow" -> {
                binding.colorSpinner.setBackgroundResource(R.drawable.profile_color_radius_yellow)
            }
        }
    }

    private fun onMemberAddedListener(name: String) {
        groupMembers.add(GroupMembers(name))

        recyclerView = findViewById(R.id.rvForGroupMembers)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerViewAdapterForGroupMembers = RecyclerViewAdapterForGroupMembers(groupMembers)
        recyclerView.adapter = recyclerViewAdapterForGroupMembers

        onMemberDeleteIconClickListener()
    }

    private fun onMemberDeleteIconClickListener() {

        recyclerViewAdapterForGroupMembers.onDeleteMemberClick = {
            if (groupMembers.isNotEmpty()){
                groupMembers.remove(it)
//                Log.i("msg", "{${it} deleted}")
                Snackbar.make(binding.root, "${it.name} Deleted Successfully!", Snackbar.LENGTH_SHORT).show()
                showRecyclerViewDataOnMemberDeleted()
            }
        }
    }

    private fun showRecyclerViewDataOnMemberDeleted() {
        recyclerView = findViewById(R.id.rvForGroupMembers)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerViewAdapterForGroupMembers = RecyclerViewAdapterForGroupMembers(groupMembers)
        recyclerView.adapter = recyclerViewAdapterForGroupMembers

        onMemberDeleteIconClickListener()
    }
}