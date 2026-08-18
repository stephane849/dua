package com.stephane.dua

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import org.json.JSONObject
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Switch
import android.widget.TextView

data class DuaEntry(
    val arabic: String,
    val translation: String,
    val divider: Boolean
)

class DuaDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DUA_INDEX = "dua_index"
    }

    private val translationViews = mutableListOf<TextView>()
    private lateinit var scrollView: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val duaIndex = intent.getIntExtra(EXTRA_DUA_INDEX, 0)
        val arabicTypeface = ResourcesCompat.getFont(this, R.font.scheherazade_new)

        scrollView = findViewById(R.id.duaScrollView)
        val container = findViewById<LinearLayout>(R.id.duaContainer)
        val switch = findViewById<Switch>(R.id.translationSwitch)
        val titleView = findViewById<TextView>(R.id.titleText)

        val (title, entries) = loadDua(duaIndex)
        titleView.text = title

        val inflater = LayoutInflater.from(this)

        for (entry in entries) {
            val itemView = inflater.inflate(R.layout.item_dua, container, false)

            val arabicText = itemView.findViewById<TextView>(R.id.arabicText)
            val translationText = itemView.findViewById<TextView>(R.id.translationText)
            val dividerLine = itemView.findViewById<View>(R.id.dividerLine)

            arabicText.text = entry.arabic
            arabicText.typeface = arabicTypeface

            translationText.text = entry.translation
            dividerLine.visibility = if (entry.divider) View.VISIBLE else View.GONE

            translationViews.add(translationText)
            container.addView(itemView)
        }

        switch.setOnCheckedChangeListener { _, isChecked ->
            val visibility = if (isChecked) View.VISIBLE else View.GONE
            for (tv in translationViews) {
                tv.visibility = visibility
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                scrollView.smoothScrollBy(0, -scrollView.height)
                return true
            }
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                scrollView.smoothScrollBy(0, scrollView.height)
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun loadDua(index: Int): Pair<String, List<DuaEntry>> {
        val jsonText = assets.open("dua.json").bufferedReader().use { it.readText() }
        val root = JSONObject(jsonText)
        val duasArr = root.getJSONArray("duas")
        val duaObj = duasArr.getJSONObject(index)

        val title = duaObj.getString("title")
        val entriesArr = duaObj.getJSONArray("entries")
        val list = mutableListOf<DuaEntry>()
        for (i in 0 until entriesArr.length()) {
            val obj = entriesArr.getJSONObject(i)
            list.add(
                DuaEntry(
                    arabic = obj.getString("arabic"),
                    translation = obj.getString("translation"),
                    divider = obj.optBoolean("divider", false)
                )
            )
        }
        return Pair(title, list)
    }
}
