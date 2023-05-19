package com.coderbinotechworld.upluck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coderbinotechworld.upluck.adapters.RecyclerViewAdapterForGroupMembers
import com.coderbinotechworld.upluck.RoomDB.Groups
import com.coderbinotechworld.upluck.RoomDB.GroupsDatabase
import com.coderbinotechworld.upluck.ViewModels.CreateGroupViewModel
import com.coderbinotechworld.upluck.ViewModels.GlobalValues
import com.coderbinotechworld.upluck.ViewModels.GroupMembers
import com.coderbinotechworld.upluck.ViewModels.SelectProfileColorViewModel
import com.coderbinotechworld.upluck.databinding.ActivityCreateGroupBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CreateGroupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateGroupBinding
    private lateinit var createGroupViewModel: CreateGroupViewModel
    private lateinit var selectProfileColorViewModel: SelectProfileColorViewModel

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
        binding = ActivityCreateGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        groupsDb = GroupsDatabase.getGroupsDatabase(this@CreateGroupActivity)

        // recycler view for group members
        recyclerView = findViewById(R.id.rvForGroupMembers)
        recyclerView.layoutManager = LinearLayoutManager(this)

        groupMembers = ArrayList()
        GlobalValues.groupList = ArrayList()

        recyclerViewAdapterForGroupMembers = RecyclerViewAdapterForGroupMembers(groupMembers)
        recyclerView.adapter = recyclerViewAdapterForGroupMembers

        // trigger when delete member icon will be clicked
        onMemberDeleteIconClickListener()


        createGroupViewModel = ViewModelProvider(this)[CreateGroupViewModel::class.java]
        selectProfileColorViewModel = ViewModelProvider(this)[SelectProfileColorViewModel::class.java]


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

        // save the data on ROOM Database
        binding.btnSave.setOnClickListener {
            if (binding.etForGroupName.text.toString().isEmpty()){
                Snackbar.make(binding.root, "Please Enter Name of the Group!", Snackbar.LENGTH_SHORT).show()
                binding.etForGroupName.requestFocus()
            }
            else if (groupMembers.size < 2 || groupMembers.size > 15)
                Snackbar.make(binding.root, "Members should be minimum 2 and maximum 15", Snackbar.LENGTH_SHORT).show()
            else{

                saveGroupDataToRoomDatabase()

            }
        }

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }



    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun saveGroupDataToRoomDatabase() {
//        Snackbar.make(binding.root, "Work in Progress", Snackbar.LENGTH_SHORT).show()

        val localGroupName = binding.etForGroupName.text.toString()
        val localGroupMemberCount = groupMembers.size
        val localGroupProfileColor = selectedProfileColor


        val localGroup = Groups(null, localGroupName, groupMembers, localGroupMemberCount, localGroupProfileColor)

        GlobalScope.launch{
            groupsDb.groupsDao().insert(localGroup)
        }

        GlobalValues.groupList?.add(localGroupName)
        val intent = Intent(this, MainActivity::class.java)
//        intent.putExtra("dataSaved", "Saved")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()

        Toast.makeText(this, " Group Named: \"$localGroupName\" saved", Toast.LENGTH_SHORT).show()


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


    private fun onMemberAddedListener(name: String) {
        groupMembers.add(GroupMembers(name))

        recyclerView = findViewById(R.id.rvForGroupMembers)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerViewAdapterForGroupMembers = RecyclerViewAdapterForGroupMembers(groupMembers)
        recyclerView.adapter = recyclerViewAdapterForGroupMembers

        onMemberDeleteIconClickListener()
    }

}