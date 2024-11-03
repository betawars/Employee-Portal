package com.example.employeeportal.operations.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OperatorRepo (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    var operatorId: Int = 0,

    @ColumnInfo("operator_name")
    var operatorName: String
)