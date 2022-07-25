package com.example.quizacademytask

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SimpleAdapter(
    private val list: ArrayList<String>,
    private val listener: OnItemClickListener,
) :
    RecyclerView.Adapter<SimpleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.row_item,
            parent, false
        )
        return ViewHolder(itemView)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        holder.row.text = currentItem
        if (position % 2 != 0)
            holder.itemView.setBackgroundColor((Color.parseColor("#1E1E1E")))
    }

    fun deleteItem(position: Int) {
        list.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val row: TextView = itemView.findViewById<TextView>(R.id.rowText)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position, v)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, v: View?)
    }
}
