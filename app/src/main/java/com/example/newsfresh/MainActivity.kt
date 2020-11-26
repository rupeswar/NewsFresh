package com.example.newsfresh

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAdapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = NewsListAdapter()
//        fetchData()
        recyclerView.adapter = mAdapter
    }

    private fun fetchData() {
        val url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=59a6d39ca7704143a25fc0b03fe35ec1"
        val jsonObjectRequest = object: JsonObjectRequest(Request.Method.GET, url, null, {
            val newsJsonArray = it.getJSONArray("articles")
            val newsArray = ArrayList<News>()
            for(i in 0 until newsJsonArray.length()) {
                val newsJsonObject = newsJsonArray.getJSONObject(i)
                val news = News(newsJsonObject.getString("title"), newsJsonObject.getString("author"), newsJsonObject.getString("url"), newsJsonObject.getString("urlToImage"))
                newsArray.add(news)
            }

            mAdapter.updateNews(newsArray)
        }, {
            Log.d("myApp", "Connection error -> ${it.stackTraceToString()}")
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}