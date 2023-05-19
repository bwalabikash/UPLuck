package com.coderbinotechworld.upluck.RoomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Groups :: class], version = 1)
@TypeConverters(MemberTypeConverter::class)
abstract class GroupsDatabase: RoomDatabase() {

    abstract fun groupsDao(): GroupsDao

    companion object{

        @Volatile
        private var INSTANCE: GroupsDatabase? = null

        fun getGroupsDatabase(context: Context): GroupsDatabase{

            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }



            synchronized(this){

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GroupsDatabase::class.java,
                    "groups_database"
                ).build()

                INSTANCE = instance
                return instance

            }

        }

    }

}