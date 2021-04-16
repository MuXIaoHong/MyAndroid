package com.nan.myandroid.base

import android.os.Bundle
import android.widget.TableLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.nan.myandroid.R
import com.nan.myandroid.common.FragmentAdapter
import kotlinx.android.synthetic.main.activity_view.*

/**
 *author：93289
 *date:2020/8/10
 *dsc:
 */
abstract class BaseVPFragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setLayoutId())

        val fragmentAdapter = FragmentAdapter(supportFragmentManager).apply {
            fragments = this@BaseVPFragmentActivity.fragments
            titles = this@BaseVPFragmentActivity.titles
        }

        findViewById<TabLayout>(R.id.tablayout).run {
            setupWithViewPager(viewpager, false)
            tabMode=TabLayout.MODE_SCROLLABLE
        }
        findViewById<ViewPager>(R.id.viewpager).adapter = fragmentAdapter
    }

    abstract fun setLayoutId(): Int
    abstract val fragments: List<Fragment>
    abstract val titles: List<String>
}