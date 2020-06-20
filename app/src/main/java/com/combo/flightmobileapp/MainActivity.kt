package com.combo.flightmobileapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.net.URL


class MainActivity : AppCompatActivity() {

    private lateinit var urlViewModel: UrlViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = UrlListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        urlViewModel = ViewModelProvider(this).get(UrlViewModel::class.java)
        urlViewModel.allUrls.observe(this, Observer { urls ->
            // Update the cached copy of the words in the adapter.
            urls?.let { adapter.setUrls(it) }
        })
//        urlViewModel.deleteAll()

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val urlTextEdit = findViewById<EditText>(R.id.typeURLbox).text.toString()
            val url = Url(urlTextEdit)
            urlViewModel.insert(url)


//            val intent = Intent(this@MainActivity, NewWordActivity::class.java)
//            startActivityForResult(intent, newWordActivityRequestCode)

        }


        val button = findViewById<Button>(R.id.connectButton)
        //val urlTextEdit = findViewById<EditText>(R.id.typeURLbox)
        // set on-click listener
        button.setOnClickListener {
            wasClicked(it)
        }

    }
    private fun wasClicked(view: View) {
        val dubugText = findViewById<TextView>(R.id.debugCons)
        val urlTextEdit = findViewById<EditText>(R.id.typeURLbox)
        dubugText.text = urlTextEdit.text.toString()
    }


}