package com.example.mystoryapp.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mystoryapp.local.dao.StoryDao
import com.example.mystoryapp.local.entity.StoryEntity

@Database(
    entities =[StoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StoryAppDatabase: RoomDatabase() {

    abstract fun getStoryDao(): StoryDao

}