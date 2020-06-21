package com.combo.flightmobileapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import java.net.URL


class MainActivity : AppCompatActivity() {

    private lateinit var urlViewModel: UrlViewModel
    private var client = Client()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //list of URLs define
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

        //init connect button
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val urlTextEdit = findViewById<EditText>(R.id.typeURLbox).text.toString()
            //if url is not empty
            if (urlTextEdit != "") {
                val url = Url(urlTextEdit)
                urlViewModel.insert(url)
                val c = client.connect(urlTextEdit)
                //if connected successfully
                if (c == 1) {
                    var c2 = client.getOneImage()
                    //println(client.gotImage.toString())

                    GlobalScope.launch(Dispatchers.Main) {
                        delay(500)
                        if (c2 == 1 || client.gotImage) {
                            client.gotImage = false
                            c2 = 0
                            goToSimActivity(urlTextEdit)
                        } else {
                            Toast.makeText(
                                applicationContext, "Connection problem (no Image received)",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                } else {
                    Toast.makeText(
                        applicationContext, "Connection problem",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else { // url=""
                Toast.makeText(
                    applicationContext, "Empty URL",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // go to simulatorActivity and pass the url
    private fun goToSimActivity(url: String) {
        val intent = Intent(this, SimulatorActivity::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
    }

    //event for clicking url from list
    fun urlWasClicked(view: View) {
        val str = (view as TextView).text.toString()
        val urlTextEdit = findViewById<EditText>(R.id.typeURLbox)
        urlTextEdit.text = Editable.Factory.getInstance().newEditable(str)
    }
}