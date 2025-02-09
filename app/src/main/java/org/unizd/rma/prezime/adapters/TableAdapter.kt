package com.example.footballapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapp.models.Team

class TableAdapter : RecyclerView.Adapter<TableAdapter.ViewHolder>() {
    private var teams: List<Team> = listOf()

    fun submitList(list: List<Team>) {
        teams = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val team = teams[position]
        holder.textView1.text = "${team.position}. ${team.name}"
        holder.textView2.text = "Points: ${team.points}"
    }

    override fun getItemCount(): Int = teams.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView1: TextView = view.findViewById(android.R.id.text1)
        val textView2: TextView = view.findViewById(android.R.id.text2)
    }
}
