package com.example.employeeportal.operations.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.employeeportal.data.AppDataBase
import com.example.employeeportal.data.DatabaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OperatorViewModel( application: Application) : AndroidViewModel(application) {


    private val repository = DatabaseRepository(
        AppDataBase.getInstance(application).operatorRepoDao()
    )

    val getOperatorList =

        repository.getOperatorList().asLiveData()

    fun addOperator(repo: OperatorRepo) {

        CoroutineScope(Dispatchers.IO).launch {
            repository.addOperator(repo)
        }
    }

    fun removeOperator(repo: OperatorRepo) {

        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteOperator(repo)
        }
    }
}