package com.combo.flightmobileapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
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

            // TODO- connect
            goToSimActivity(urlTextEdit)




        }

//        val urlButton = findViewById<TextView>(R.id.textView)
//        urlButton.setOnClickListener {
//            val urlTextBoxEdit = findViewById<EditText>(R.id.typeURLbox)
////            urlButton.text = urlTextBoxEdit.text.toString()
//            val urlButton = findViewById<TextView>(R.id.textView)
//            urlTextBoxEdit.text = urlButton.editableText
//        }


//        val button = findViewById<Button>(R.id.connectButton)
//        //val urlTextEdit = findViewById<EditText>(R.id.typeURLbox)
//        // set on-click listener
//        button.setOnClickListener {
//            wasClicked(it)
//        }

    }

    fun goToSimActivity(url: String) {
        val intent = Intent(this, SimulatorActivity::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
    }

    fun urlWasClicked(view: View) {
//        val str = (view as TextView).editableText
//        val urlTextEdit = findViewById<EditText>(R.id.typeURLbox)
//        urlTextEdit.text = str
        val str = (view as TextView).text.toString()
        val urlTextEdit = findViewById<EditText>(R.id.typeURLbox)
        urlTextEdit.text = Editable.Factory.getInstance().newEditable(str)

        println("good job")

    }


}