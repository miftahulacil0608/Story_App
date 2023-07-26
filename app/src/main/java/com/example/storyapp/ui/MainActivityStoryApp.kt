package com.example.storyapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.adapter.StoryAdapter
import com.example.storyapp.data.SharedPreferences
import com.example.storyapp.data.dataClass.StoryItem
import com.example.storyapp.databinding.ActivityMainStoryAppBinding
import com.example.storyapp.ui.viewModel.MainActivityStoryAppViewModel


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token_setting")

class MainActivityStoryApp : AppCompatActivity() {
    private lateinit var binding: ActivityMainStoryAppBinding
    private lateinit var mainActivityStoryAppViewModel: MainActivityStoryAppViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainStoryAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showRecyclerview()

        setupViewModel()

        binding.btnAddStory.setOnClickListener {
            val intent = Intent(this@MainActivityStoryApp,StoryActivity::class.java)
            startActivity(intent)
            onStart()
        }
    }

    private fun showRecyclerview() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setStoryData(storyData: ArrayList<StoryItem>) {
        val listStory = ArrayList<StoryItem>()
        for (data in storyData) {
            listStory.add(data)
        }
        val adapter = StoryAdapter(listStory)
        adapter.notifyDataSetChanged()
        binding.rvStory.adapter = adapter
    }


    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun setupViewModel() {
        mainActivityStoryAppViewModel = ViewModelProvider(
            this,
            ViewModelFactory(SharedPreferences.instance(dataStore))
        )[MainActivityStoryAppViewModel::class.java]

        setupAction()
    }

    private fun setupAction() {
        mainActivityStoryAppViewModel.apply {
            isLoading.observe(this@MainActivityStoryApp) {
                showLoading(it)
            }
            getUser().observe(this@MainActivityStoryApp) {
                if (!it.isLogin) {
                    startActivity(Intent(this@MainActivityStoryApp, WelcomeActivity::class.java))
                    finish()
                }
            }
            getListStory(this@MainActivityStoryApp)
            userStory.observe(this@MainActivityStoryApp){
                setStoryData(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                mainActivityStoryAppViewModel.logout()
                startActivity(Intent(this@MainActivityStoryApp, LoginActivity::class.java))
                finish()
                return true
            }
            R.id.menu_upload -> {
                startActivity(Intent(this@MainActivityStoryApp, StoryActivity::class.java))
                return true
            }
            R.id.menu_setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                return true
            }
            else -> return true
        }
    }
}