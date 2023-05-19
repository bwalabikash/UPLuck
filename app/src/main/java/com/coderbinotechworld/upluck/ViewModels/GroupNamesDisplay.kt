package com.coderbinotechworld.upluck.ViewModels

data class GroupNamesDisplay(
    val id: Int,
    val name: String,
    val members: List<GroupMembers>,
    val color: String,
    val membersCount: Int

    )
