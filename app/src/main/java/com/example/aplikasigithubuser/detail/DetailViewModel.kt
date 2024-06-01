package com.example.aplikasigithubuser.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasigithubuser.data.model.ResponseUserGithub
import com.example.aplikasigithubuser.data.remote.ApiClient
import com.example.aplikasigithubuser.utils.Result
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class DetailViewModel: ViewModel() {
    val resulDetailtUser = MutableLiveData<Result<List<ResponseUserGithub.Item>>>()

    fun getDetailUser(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .GithubService
                    .getDetailUserGithub(username)

                emit(response)
            }.onStart {
                resulDetailtUser.value = Result.Loading(true)
            }.onCompletion {
                resulDetailtUser.value = Result.Loading(false)
            }.catch {
                it.printStackTrace()
                resulDetailtUser.value = Result.Error(it)
            }.collect {
                resulDetailtUser.value = Result.Success(it)
            }
        }
    }
}