package com.example.tp2.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tp2.R
import com.example.tp2.ShowListActivity
import com.example.tp2.data.model.ListeToDo

class RecyclerViewAdapter(
    val toDoLists: MutableList<ListeToDo>,
    private val mContext: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val parentLayout: RelativeLayout
        init {
            textView = view.findViewById(R.id.textliste)
            parentLayout = view.findViewById(R.id.parent_layout)
        }

        fun bind(s: String) {
            textView.text = s

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.layout_liste, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.parentLayout.setOnClickListener {
                    val toShowListAct: Intent
                    toShowListAct = Intent(mContext, ShowListActivity::class.java)
                    toShowListAct.putExtra("id", toDoLists[position].id)
                    toShowListAct.putExtra("position", position)
                    mContext.startActivity(toShowListAct)
                }
                holder.bind(toDoLists[position].titreListToDo)
            }

        }
    }

    override fun getItemCount(): Int {
        return toDoLists.size
    }
}