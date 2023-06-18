package com.example.voiceassistent.database.context

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.voiceassistent.database.dao.MessageDao
import com.example.voiceassistent.database.entities.MessageEntity

@Database(
    entities = [MessageEntity::class],
    version = 1
)
abstract class MessageDatabase : RoomDatabase() {

    abstract fun getMessageDao(): MessageDao

    companion object {

        @Volatile
        private var instance : MessageDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            MessageDatabase::class.java,
            "message-database.db",
        ).build()
    }
}