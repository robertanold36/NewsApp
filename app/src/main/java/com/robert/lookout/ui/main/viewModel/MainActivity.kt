package com.robert.lookout.ui.main.viewModel

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.robert.lookout.R
import com.robert.lookout.ui.main.MyApplication
import com.robert.lookout.ui.main.adapter.SectionsPagerAdapter
import com.robert.lookout.ui.main.repository.ApiRepository
import com.robert.lookout.ui.main.viewModel.main.MainActivityComponent
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: PageViewModel

    @Inject
    lateinit var repository: ApiRepository

    lateinit var mainActivityComponent: MainActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {

        mainActivityComponent=(application as MyApplication).appComponent.mainActivityComponent().create()

        mainActivityComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        repository=ApiRepository()
        val factory=PageViewModelFactory(repository)

        viewModel=ViewModelProvider(this,factory).get(PageViewModel::class.java)

        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )

        val viewPager: ViewPager = findViewById(R.id.view_pager)

        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)


    }
}