package com.stephane.dua

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import android.widget.LinearLayout
import android.widget.TextView
import android.view.View

data class DuaSummary(val index: Int, val title: String)

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val container = findViewById<LinearLayout>(R.id.menuContainer)
        val inflater = LayoutInflater.from(this)

        val summaries = loadDuaSummaries()

        for (summary in summaries) {
            val rowView = inflater.inflate(R.layout.item_menu_row, container, false)
            val titleView = rowView.findViewById<TextView>(R.id.menuItemTitle)
            titleView.text = summary.title

            rowView.setOnClickListener {
                val intent = Intent(this, DuaDetailActivity::class.java)
                intent.putExtra(DuaDetailActivity.EXTRA_DUA_INDEX, summary.index)
                startActivity(intent)
            }

            container.addView(rowView)

            // hairline divider between rows
            val divider = View(this)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1
            )
            params.marginStart = 20
            params.marginEnd = 20
            divider.layoutParams = params
            divider.setBackgroundColor(resources.getColor(R.color.eink_divider, theme))
            container.addView(divider)
        }
    }

    private fun loadDuaSummaries(): List<DuaSummary> {
        val jsonText = assets.open("dua.json").bufferedReader().use { it.readText() }
        val root = JSONObject(jsonText)
        val duasArr = root.getJSONArray("duas")
        val list = mutableListOf<DuaSummary>()
        for (i in 0 until duasArr.length()) {
            val obj = duasArr.getJSONObject(i)
            list.add(DuaSummary(index = i, title = obj.getString("title")))
        }
        return list
    }
}
