package com.example.quizacademytask

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView


class StackListAdapter(
    private val list: ArrayList<String>,
    private val listener: OnItemClickListener,
) :
    RecyclerView.Adapter<StackListAdapter.ViewHolder>() {

    var selectedItems = HashMap<Int, String>()
    private lateinit var holder: ViewHolder
    private lateinit var recyclerView: RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.row_item,
            parent, false
        )
        holder = ViewHolder(itemView)
        return holder
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        val view = holder.itemView
        holder.row.text = currentItem
        if (position % 2 != 0)
            view.setBackgroundColor((Color.parseColor("#1E1E1E")))
        if (holder.row.text in selectedItems.values)
            paintSelected(holder.row)
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

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {

        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    fun deleteItem(position: Int) {
        list.removeAt(position)
        notifyDataSetChanged()
    }

    fun deleteSelected() {
        list.removeAll(selectedItems.values)
        notifyDataSetChanged()
    }

    fun processSelect(position: Int, view: TextView) {
        val text = view.text as String
        if (!selectedItems.values.contains(text)) {
            selectedItems[position] = text
            paintSelected(view)
        } else {
            selectedItems.remove(position)
            paintUnselected(view)
        }
    }

    @SuppressLint("ResourceAsColor")
    fun paintSelected(view: TextView) {
        view.setTextColor(0xFF87cefa.toInt())
    }

    @SuppressLint("ResourceAsColor")
    fun paintUnselected(view: TextView) {
        view.setTextColor(0xFFFDFDFD.toInt())
    }

    fun emptyList() {
        val x = recyclerView.children.count()
        recyclerView.children.forEach {
            paintUnselected(it.findViewById(R.id.rowText))
        }
        selectedItems.clear()
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val row: TextView = itemView.findViewById(R.id.rowText)
        val rowLayout: View = itemView.findViewById(R.id.row_layout)

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
