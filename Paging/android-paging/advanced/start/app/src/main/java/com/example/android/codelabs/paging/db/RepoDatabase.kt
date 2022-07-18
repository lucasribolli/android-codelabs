package com.example.android.codelabs.paging.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android.codelabs.paging.model.Repo

@Database(entities = [Repo::class], version = 1, exportSchema = false)
abstract class RepoDatabase : RoomDatabase() {
    abstract fun getDao(): RepoDao

    companion object {
        @Volatile
        private var databaseInstance: RepoDatabase? = null

        fun getInstance(context: Context): RepoDatabase {
            return databaseInstance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    RepoDatabase::class.java,
                    "github.db"
                ).build()
            }
        }
    }
}