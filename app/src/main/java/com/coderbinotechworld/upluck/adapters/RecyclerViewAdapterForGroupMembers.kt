package com.coderbinotechworld.upluck.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.coderbinotechworld.upluck.R
import com.coderbinotechworld.upluck.ViewModels.GroupMembers

class RecyclerViewAdapterForGroupMembers(private val memberList: ArrayList<GroupMembers>): RecyclerView.Adapter<RecyclerViewAdapterForGroupMembers.MyViewHolder>() {

    var onItemClick: ((GroupMembers) -> Unit)? = null
    var onDeleteMemberClick: ((GroupMembers) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.members_layout, parent, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val member = memberList[position]
        holder.memberName.text = member.name

//        holder.itemView.setOnClickListener {
//
//        }

        holder.deleteMember.setOnClickListener {
            onDeleteMemberClick?.invoke(member)
        }

    }

    override fun getItemCount(): Int {
        return memberList.size
    }

    class MyViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView){

        val memberName: TextView = itemView.findViewById(R.id.memberName)
        val deleteMember: ImageView = itemView.findViewById(R.id.imgDelete)

    }
}

