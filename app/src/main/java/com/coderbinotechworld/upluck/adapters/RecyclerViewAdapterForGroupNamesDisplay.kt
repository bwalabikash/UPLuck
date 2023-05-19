package com.coderbinotechworld.upluck.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.coderbinotechworld.upluck.R
import com.coderbinotechworld.upluck.ViewModels.GroupNamesDisplay
import com.google.android.material.snackbar.Snackbar

class RecyclerViewAdapterForGroupNamesDisplay(private val groupList: ArrayList<GroupNamesDisplay>): RecyclerView.Adapter<RecyclerViewAdapterForGroupNamesDisplay.MyViewHolder>() {

    var onItemClick: ((GroupNamesDisplay) -> Unit)? = null
    var onEditIconClick: ((GroupNamesDisplay) -> Unit)? = null

    private var selectedPosition: Int = -1
    private var selectedGroup: String = ""

    private var showSnackBar = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.groups_layout, parent, false)
        return MyViewHolder(view)

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val group = groupList[position]

        if (showSnackBar == 1){
            val snackBar = Snackbar.make(holder.itemView.rootView, "Groups Loading!", Snackbar.LENGTH_SHORT)
            snackBar.duration = 3
            snackBar.show()

            showSnackBar = 0
        }

        holder.gColor.setBackgroundResource(R.drawable.profile_color_circle_gray)
        holder.gName.text = group.name
        holder.gMember.text = group.membersCount.toString()

        holder.itemView.setOnClickListener {

            selectedGroup = group.name
            selectedPosition = if (position == selectedPosition){
                -1
            }else{
                position
            }
            notifyDataSetChanged()

            onItemClick?.invoke(group)
        }

        holder.gEditGroup.setOnClickListener {
            onEditIconClick?.invoke(group)
        }

        if (selectedPosition == position){
            if (group.name == "Example")
                holder.gEditGroup.visibility = View.GONE
            else
                holder.gEditGroup.visibility = View.VISIBLE

            setBackgroundColorForGroupProfile(group.color, holder)
        }else{
            if (selectedGroup == groupList[position].name){
                if (group.name == "Example")
                    holder.gEditGroup.visibility = View.GONE
                else
                    holder.gEditGroup.visibility = View.VISIBLE

                setBackgroundColorForGroupProfile(group.color, holder)
            }else
                holder.gEditGroup.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val gColor: View = itemView.findViewById(R.id.vGroupColor)
        val gName: TextView = itemView.findViewById(R.id.tvGroupName)
        val gMember: TextView = itemView.findViewById(R.id.tvGroupMembers)
        val gEditGroup: ImageView = itemView.findViewById(R.id.imgEdit)
    }

    private fun setBackgroundColorForGroupProfile(color: String, holder: MyViewHolder){
        when(color){
            "Gray" ->{
                holder.gColor.setBackgroundResource(R.drawable.profile_color_circle_gray)
            }
            "Green" ->{
                holder.gColor.setBackgroundResource(R.drawable.profile_color_circle_green)
            }
            "Pink" ->{
                holder.gColor.setBackgroundResource(R.drawable.profile_color_circle_pink)
            }
            "Red" ->{
                holder.gColor.setBackgroundResource(R.drawable.profile_color_circle_red)
            }
            "Tulip" ->{
                holder.gColor.setBackgroundResource(R.drawable.profile_color_circle_tulip)
            }
            "Yellow" ->{
                holder.gColor.setBackgroundResource(R.drawable.profile_color_circle_yellow)
            }
        }
    }

}