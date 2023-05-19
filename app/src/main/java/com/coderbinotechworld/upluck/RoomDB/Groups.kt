package com.coderbinotechworld.upluck.RoomDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.coderbinotechworld.upluck.ViewModels.GroupMembers
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Entity(tableName = "groups_table")
data class Groups(
    @PrimaryKey(autoGenerate = true) val groupId:Int?,
    @ColumnInfo(name = "group_name") val groupName: String,
    @ColumnInfo(name = "group_members") val groupMembers: List<GroupMembers>,
    @ColumnInfo(name = "group_members_count") val groupMembersCount: Int,
    @ColumnInfo(name = "group_profile_color") val groupProfileColor: String
)

class MemberTypeConverter(){

    @TypeConverter
    fun listToJson(value: List<GroupMembers>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<GroupMembers>::class.java).toList()


    @TypeConverter
    fun fromString(value: String?): ArrayList<GroupMembers>{

        val listType = object: TypeToken<ArrayList<String>>(){}.type
        return Gson().fromJson(value, listType)

    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<GroupMembers?>): String{
        return Gson().toJson(list)
    }

}