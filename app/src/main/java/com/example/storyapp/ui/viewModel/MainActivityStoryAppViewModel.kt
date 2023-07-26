package com.example.storyapp.ui.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.storyapp.data.SharedPreferences
import com.example.storyapp.data.dataClass.DataSharedPreferences
import com.example.storyapp.data.dataClass.StoryItem
import com.example.storyapp.data.dataClass.StoryResponse
import com.example.storyapp.data.remote.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityStoryAppViewModel(private val preferences: SharedPreferences) : ViewModel() {

    private var _userStory = MutableLiveData<ArrayList<StoryItem>>()
    val userStory: LiveData<ArrayList<StoryItem>> = _userStory
    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUser() : LiveData<DataSharedPreferences> {
        return preferences.getUser().asLiveData()
    }

    fun getToken() : LiveData<String>{
        return preferences.getToken().asLiveData()
    }
    fun logout(){
        viewModelScope.launch {
            preferences.logout()
        }
    }

    fun getListStory(context: Context){
        _isLoading.value = true
        val client = ApiConfig.getApiService(context).getAllStory()
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _userStory.postValue(response.body()?.listStory)
                    Log.d("MainActivity", "onResponse: ${response.body()?.listStory}")
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _isLoading.value = true
                Log.d("MainActivity", "onFailure: ${t.message}")
            }

        })
    }
}