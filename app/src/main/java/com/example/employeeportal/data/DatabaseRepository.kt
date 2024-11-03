package com.example.employeeportal.data

import com.example.employeeportal.operations.data.OperatorRepo
import com.example.employeeportal.operations.data.OperatorRepoDao
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val dao: OperatorRepoDao){

    fun getOperatorList() =
        dao.getAllRepo()
    suspend fun addOperator(repo: OperatorRepo) =
        dao.insert(repo)

    suspend fun deleteOperator(repo: OperatorRepo) =
        dao.delete(repo)
}