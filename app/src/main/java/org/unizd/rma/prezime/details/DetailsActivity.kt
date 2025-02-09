package org.unizd.rma.prezime.details

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapp.ui.adapters.TableAdapter
import com.example.footballapp.viewmodel.FootballViewModel
import org.unizd.rma.prezime.R

class DetailsActivity : AppCompatActivity() {
    private val viewModel: FootballViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layouta.activity_details)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewTable)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = TableAdapter()
        recyclerView.adapter = adapter

        val championship = intent.getStringExtra("championship") ?: return

        viewModel.fetchCompetitionTable(championship)

        viewModel.competitionTable.observe(this) { teams ->
            if (teams.isEmpty()) {
                Toast.makeText(this, "No teams found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.submitList(teams)
            }
        }
    }
}
