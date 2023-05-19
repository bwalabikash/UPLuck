package com.coderbinotechworld.upluck.RoomDB

import androidx.room.*


@Dao
interface GroupsDao {

    @Query("SELECT * FROM groups_table")
    fun getAllGroups(): List<Groups>?

    @Query("SELECT * FROM groups_table WHERE group_name LIKE :groupName LIMIT 1")
    suspend fun findByGroupName(groupName: String): Groups

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(groups: Groups)

    @Query("SELECT group_name FROM groups_table")
    fun getAllGroupNames(): List<String>

    @Delete
    suspend fun delete(groups: Groups)

    @Query("DELETE FROM groups_table")
    suspend fun deleteAll()

    @Update
    suspend fun updateGroup(groups: Groups)

    @Query("DELETE FROM groups_table WHERE group_name LIKE :groupName")
    suspend fun deleteCurrentGroup(groupName: String)

    @Query("UPDATE groups_table SET group_name = :groupName WHERE group_name LIKE :groupName")
    suspend fun updateGroupName(groupName: String)

//    @Query("SELECT group_members_count FROM groups_table")
//    suspend fun getMemberCount()

}