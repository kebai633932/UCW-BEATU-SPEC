package com.ucw.beatu.shared.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ucw.beatu.shared.database.converter.Converters
import com.ucw.beatu.shared.database.dao.CommentDao
import com.ucw.beatu.shared.database.dao.InteractionStateDao
import com.ucw.beatu.shared.database.dao.UserDao
import com.ucw.beatu.shared.database.dao.UserVideoRelationDao
import com.ucw.beatu.shared.database.dao.VideoDao
import com.ucw.beatu.shared.database.entity.CommentEntity
import com.ucw.beatu.shared.database.entity.InteractionStateEntity
import com.ucw.beatu.shared.database.entity.UserEntity
import com.ucw.beatu.shared.database.entity.UserVideoRelationEntity
import com.ucw.beatu.shared.database.entity.VideoEntity

@Database(
    entities = [
        VideoEntity::class,
        CommentEntity::class,
        InteractionStateEntity::class,
        UserEntity::class,
        UserVideoRelationEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class BeatUDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao
    abstract fun commentDao(): CommentDao
    abstract fun interactionStateDao(): InteractionStateDao
    abstract fun userDao(): UserDao
    abstract fun userVideoRelationDao(): UserVideoRelationDao

    companion object {
        fun build(context: Context, dbName: String = "beatu-db"): BeatUDatabase =
            Room.databaseBuilder(context, BeatUDatabase::class.java, dbName)
                .fallbackToDestructiveMigration()
                .build()
    }
}

