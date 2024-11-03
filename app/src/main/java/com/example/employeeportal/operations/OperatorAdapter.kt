package com.example.employeeportal.operations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.employeeportal.R
import com.example.employeeportal.operations.data.OperatorRepo

class OperatorAdapter(private val onOperatorClick:(OperatorRepo) -> Unit) : RecyclerView.Adapter<OperatorAdapter.OperatorViewHolder>() {

    private var operatorList = listOf<OperatorRepo>()

    fun updateList(newList: List<OperatorRepo>){
        notifyItemRangeRemoved(0,operatorList.size)
        operatorList = newList.toList()
        notifyItemRangeInserted(0,operatorList.size)
    }

    override fun getItemCount() = operatorList.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OperatorViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.operator_card, parent, false)
        return OperatorViewHolder(itemView,onOperatorClick)
    }

    override fun onBindViewHolder(holder: OperatorViewHolder, position: Int) {
        holder.bind(operatorList[position])
    }

    class OperatorViewHolder(itemView: View,private val onClick:(OperatorRepo)->Unit) : RecyclerView.ViewHolder(itemView) {
        private val operatorName: TextView = itemView.findViewById(R.id.operator_name)

        private var currentOperator: OperatorRepo?= null

        init {
            itemView.setOnClickListener{
                currentOperator?.let(onClick)
            }
        }

        fun bind(operatorRepo: OperatorRepo) {
            currentOperator = operatorRepo
            operatorName.text = operatorRepo.operatorName
        }

    }

}
