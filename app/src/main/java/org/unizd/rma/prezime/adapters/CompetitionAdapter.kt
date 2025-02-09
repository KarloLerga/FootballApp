package com.example.footballapp.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CompetitionAdapter(private val onClick: (String) -> Unit) : RecyclerView.Adapter<CompetitionAdapter.ViewHolder>() {
    private var competitions: List<String> = listOf()

    fun submitList(list: List<String>) {
        competitions = list
        Log.d("CompetitionAdapter", "Competitions updated, size: ${competitions.size}")
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val competition = competitions[position]
        Log.d("CompetitionAdapter", "Binding data for: $competition")
        holder.textView.text = competition
        holder.itemView.setOnClickListener { onClick(competition) }
    }

    override fun getItemCount(): Int = competitions.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(android.R.id.text1)
    }
}
