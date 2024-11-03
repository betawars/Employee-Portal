package com.example.employeeportal.operations.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OperatorRepoDao {


    @Query("SELECT * FROM operatorrepo")
    fun getAllRepo(): Flow<List<OperatorRepo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insert(repo: OperatorRepo)
    @Delete
    suspend fun delete(repo: OperatorRepo)
}