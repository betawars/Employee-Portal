package com.example.employeeportal.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.employeeportal.operations.data.OperatorRepo
import com.example.employeeportal.operations.data.OperatorRepoDao

@Database(entities = [OperatorRepo::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract  fun operatorRepoDao():OperatorRepoDao

    companion object {
        @Volatile private var instance: AppDataBase? = null
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                AppDataBase::class.java,
                "operatorList"
            ).build()

        fun getInstance(context: Context): AppDataBase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }
    }
}