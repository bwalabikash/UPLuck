package com.coderbinotechworld.upluck

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coderbinotechworld.upluck.adapters.RecyclerViewAdapterForGroupNamesDisplay
import com.coderbinotechworld.upluck.RoomDB.Groups
import com.coderbinotechworld.upluck.RoomDB.GroupsDatabase
import com.coderbinotechworld.upluck.ViewModels.GroupMembers
import com.coderbinotechworld.upluck.ViewModels.GroupNamesDisplay
import com.coderbinotechworld.upluck.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    // for group recycler view
    private lateinit var recyclerView: RecyclerView
    private lateinit var groupList: ArrayList<GroupNamesDisplay>
    private lateinit var recyclerViewAdapterForGroupNamesDisplay: RecyclerViewAdapterForGroupNamesDisplay

    // database reference
    private lateinit var groupsDb: GroupsDatabase

    // this will pass to the spin activity
    private var selectedGroupMembers: ArrayList<String>? = null

    // this will also be paas on Update Activity
    private var selectedGroupId: Int? = null
    private var selectedGroupProfileColor: String? = null
    private var selectedGroupMemberCount: Int? = null

    // for groups
    private var groups: List<Groups>? = null

    private var isGroupListAvailable = true


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        groupsDb = GroupsDatabase.getGroupsDatabase(requireContext())

        // recycler view for group names
        recyclerView = binding.rvForGroupNameDisplay
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        groupList = ArrayList()

        groupList.add(
            GroupNamesDisplay(
                1,
                "Example",
                listOf(GroupMembers("Name 1"), GroupMembers("Name 2"), GroupMembers("Name 3"), GroupMembers("Name 4"), GroupMembers("Name 5"), GroupMembers("Name 6")),
                "Pink",
                6
            )
        )


        recyclerViewAdapterForGroupNamesDisplay = RecyclerViewAdapterForGroupNamesDisplay(groupList)
        recyclerView.adapter = recyclerViewAdapterForGroupNamesDisplay


        CoroutineScope(Dispatchers.Main).launch {
            coroutineScope {
                launch(Dispatchers.IO) {
                    groups = groupsDb.groupsDao().getAllGroups()
                    groups?.let { getGroupsData(it) }
                }
            }
        }


        recyclerViewAdapterForGroupNamesDisplay.onItemClick = {
            setDataOnItemClick(it)
        }

        recyclerViewAdapterForGroupNamesDisplay.onEditIconClick = {

            val bundle = Bundle()
            bundle.putStringArrayList("members", selectedGroupMembers)
            bundle.putString("groupName", binding.tvForGroupName.text.toString())
            bundle.putInt("groupId", selectedGroupId!!)
            bundle.putString("groupProfileColor", selectedGroupProfileColor)
            bundle.putInt("groupMembersCount", selectedGroupMemberCount!!)

            val editGroupActivityIntent = Intent(context, EditGroupActivity::class.java)
            editGroupActivityIntent.putExtras(bundle)
            startActivity(editGroupActivityIntent)
        }


        binding.btnCreateGroup.setOnClickListener {
            val intent = Intent(context, CreateGroupActivity::class.java)
            startActivity(intent)
        }

        binding.btnSpin.setOnClickListener {

            if (binding.tvForGroupName.text.toString() == "No Group" || binding.tvForGroupMembers.text.toString() == "0 Member")
                Snackbar.make(binding.root, "Please Select any Group From Below List", Snackbar.LENGTH_SHORT).show()
            else{
                // store selectedGroupMembers as bundle and then paas into activity
                val bundle = Bundle()
                bundle.putStringArrayList("members", selectedGroupMembers)
                bundle.putString("groupName", binding.tvForGroupName.text.toString())

                // start spin activity
                val spinActivityIntent = Intent(context, SpinActivity::class.java)
                spinActivityIntent.putExtras(bundle)
                startActivity(spinActivityIntent)
            }

        }

    }

    private fun getGroupsData(groups: List<Groups>) {

        groups.forEach {
            groupList.add(
                GroupNamesDisplay(
                    it.groupId!!,
                    it.groupName,
                    it.groupMembers,
                    it.groupProfileColor,
                    it.groupMembersCount
                )
            )

        }


    }

    private fun setDataOnItemClick(it: GroupNamesDisplay) {

        setBackgroundColorForGroupProfile(it.color)

        binding.tvForGroupName.text = it.name
        binding.tvForGroupMembers.text = it.membersCount.toString()

        // change ArrayList<GroupMembers> into ArrayList<String>
        val groupMemberList = ArrayList(it.members)
        selectedGroupMembers = groupMemberList.map { it.name } as ArrayList<String>
        selectedGroupId = it.id
        selectedGroupProfileColor = it.color
        selectedGroupMemberCount = it.membersCount
    }

    private fun setBackgroundColorForGroupProfile(color: String) {
        when (color) {
            "Gray" -> {
                binding.vForGroupColor.setBackgroundResource(R.drawable.profile_color_circle_gray)
            }
            "Green" -> {
                binding.vForGroupColor.setBackgroundResource(R.drawable.profile_color_circle_green)
            }
            "Pink" -> {
                binding.vForGroupColor.setBackgroundResource(R.drawable.profile_color_circle_pink)
            }
            "Red" -> {
                binding.vForGroupColor.setBackgroundResource(R.drawable.profile_color_circle_red)
            }
            "Tulip" -> {
                binding.vForGroupColor.setBackgroundResource(R.drawable.profile_color_circle_tulip)
            }
            "Yellow" -> {
                binding.vForGroupColor.setBackgroundResource(R.drawable.profile_color_circle_yellow)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

}