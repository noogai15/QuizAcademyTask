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

    private val selectedItems = ArrayList<TextView>()

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
        val view = holder.itemView
        holder.row.text = currentItem
        if (position % 2 != 0)
            view.setBackgroundColor((Color.parseColor("#1E1E1E")))

        //On click
        view.setOnClickListener {
            listener.onItemClick(position, view)
        }
        //On long click
        view.setOnLongClickListener {
            listener.onItemLongClick(position, view)
            true
        }
    }

    fun deleteItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun deleteSelected() {
        list.removeAll(selectedItems.map { it.text as String }.toSet())
        notifyDataSetChanged()
    }

    fun process(view: TextView) {
        if (!selectedItems.contains(view)) {
            selectedItems.add(view)
            paintSelected(view)
        } else {
            selectedItems.remove(view)
            paintUnselected(view)
        }
    }

    @SuppressLint("ResourceAsColor")
    fun paintSelected(view: View) {
        view.findViewById<TextView>(R.id.rowText).setTextColor(0xFFFF0000.toInt())
    }

    fun emptyList() {
        for (view in selectedItems) paintUnselected(view)
        selectedItems.clear()
    }

    @SuppressLint("ResourceAsColor")
    fun paintUnselected(view: TextView) {
        view.setTextColor(0xFFFDFDFD.toInt())
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val row: TextView = itemView.findViewById(R.id.rowText)

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position, v)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, v: View?)
        fun onItemLongClick(position: Int, v: View?)
    }
}
