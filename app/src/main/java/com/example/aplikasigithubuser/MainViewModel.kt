package com.example.aplikasigithubuser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasigithubuser.data.model.ResponseUserGithub
import com.example.aplikasigithubuser.data.remote.ApiClient
import com.example.aplikasigithubuser.utils.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val resultUser = MutableLiveData<Result<List<ResponseUserGithub.Item>>>()

    fun getUser() {
        viewModelScope.launch {
            flow {
                    val response = ApiClient
                        .GithubService
                        .getUserGithub()

                    emit(response)
                }.onStart {
                    resultUser.value = Result.Loading(true)
                }.onCompletion {
                    resultUser.value = Result.Loading(false)
                }.catch {
                    it.printStackTrace()
                    resultUser.value = Result.Error(it)
                }.collect {
                    resultUser.value = Result.Success(it)
                }
        }
    }
}