package com.example.aplikasigithubuser.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasigithubuser.data.model.ResponseDetailUser
import com.example.aplikasigithubuser.data.remote.ApiClient
import com.example.aplikasigithubuser.utils.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailViewModel: ViewModel() {
    val resultDetailUser = MutableLiveData<Result<ResponseDetailUser>>()
    val resultFollowersUser = MutableLiveData<Result>()
    val resultFollowingUser = MutableLiveData<Result>()

    fun getDetailUser(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .GithubService
                    .getDetailUserGithub(username)

                emit(response)
            }.onStart {
                resultDetailUser.value = Result.Loading(true)
            }.onCompletion {
                resultDetailUser.value = Result.Loading(false)
            }.catch {
                it.printStackTrace()
                resultDetailUser.value = Result.Error(it)
            }.collect {
                resultDetailUser.value = Result.Success(it)
            }
        }
    }

    fun getFollowers(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .GithubService
                    .getFollowersUserGithub(username)

                emit(response)
            }.onStart {
                resultFollowersUser.value = Result.Loading(true)
            }.onCompletion {
                resultFollowersUser.value = Result.Loading(false)
            }.catch {
                it.printStackTrace()
                resultFollowersUser.value = Result.Error(it)
            }.collect {
                resultFollowersUser.value = Result.Success(it)
            }
        }
    }

    fun getFollowing(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .GithubService
                    .getFollowingUserGithub(username)

                emit(response)
            }.onStart {
                resultFollowingUser.value = Result.Loading(true)
            }.onCompletion {
                resultFollowingUser.value = Result.Loading(false)
            }.catch {
                it.printStackTrace()
                resultFollowingUser.value = Result.Error(it)
            }.collect {
                resultFollowingUser.value = Result.Success(it)
            }
        }
    }

}