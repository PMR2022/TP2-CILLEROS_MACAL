package com.example.tp2.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tp2.data.model.ItemToDo
import com.example.tp2.R


class ShowListRecyclerViewAdapter(
    var itemList: MutableList<ItemToDo> = mutableListOf(ItemToDo()),
    private val mContext: Context) :
    RecyclerView.Adapter<ShowListRecyclerViewAdapter.ItemViewHolder>() {
    var onChange: ((ItemToDo, Boolean) -> Unit)? = null

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var checkBox: CheckBox
        val description: TextView
        val parentLayout: ConstraintLayout
        init {
            checkBox = itemView.findViewById(R.id.checkBox)
            description = itemView.findViewById(R.id.textitem)
            parentLayout = itemView.findViewById(R.id.showlist_parent_layout)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.layout_item, parent, false)

        val holder = ItemViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList.get(position)
        when(holder){
            is ItemViewHolder ->{
                holder.description.setText(item.description)
                holder.checkBox.isChecked = item.getFait().equals(1)
                holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
                    onChange?.invoke(item, isChecked)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}