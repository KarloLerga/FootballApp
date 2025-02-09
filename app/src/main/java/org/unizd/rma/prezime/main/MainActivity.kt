package org.unizd.rma.prezime.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.footballapp.ui.adapters.CompetitionAdapter
import com.example.footballapp.viewmodel.FootballViewModel
import org.unizd.rma.prezime.R
import org.unizd.rma.prezime.details.DetailsActivity
import org.unizd.rma.prezime.utils.NetworkUtils

class MainActivity : AppCompatActivity() {
    private val viewModel: FootballViewModel by viewModels()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            fetchCompetitions()
        }

        val adapter = CompetitionAdapter { championship ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("championship", championship)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        viewModel.competitions.observe(this) { competitions ->
            swipeRefreshLayout.isRefreshing = false
            if (competitions.isEmpty()) {
                Log.e("MainActivity", "No competitions found!")
                Toast.makeText(this, "No competitions available", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("MainActivity", "Competitions received: ${competitions.joinToString { it.name }}")
                adapter.submitList(competitions.map { it.name })
            }
        }


        fetchCompetitions()
    }

    private fun fetchCompetitions() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show()
            swipeRefreshLayout.isRefreshing = false
            return
        }

        swipeRefreshLayout.isRefreshing = true
        viewModel.fetchCompetitions()
    }
}
