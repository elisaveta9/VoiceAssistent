package com.example.voiceassistent.database.context

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.voiceassistent.database.dao.MessageDao
import com.example.voiceassistent.database.entity.MessageEntity

@Database(
    version = 1,
    entities = [
        MessageEntity::class
    ]
)
abstract class MessageDbContext : RoomDatabase() {

    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile
        private var INSTANCE: MessageDbContext? = null

        fun getDatabase(context: Context): MessageDbContext {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            return INSTANCE
                ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MessageDbContext::class.java,
                        "messages_data"
                    )
                        .createFromAsset("database/VoiceAssistantData.db")
                        .build()
                    INSTANCE = instance
                    return instance
                }
        }
    }
}