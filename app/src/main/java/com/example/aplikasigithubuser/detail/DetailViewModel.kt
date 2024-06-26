package com.example.aplikasigithubuser.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aplikasigithubuser.data.local.DbModule
import com.example.aplikasigithubuser.data.model.ResponseDetailUser
import com.example.aplikasigithubuser.data.model.ResponseUserGithub
import com.example.aplikasigithubuser.data.remote.ApiClient
import com.example.aplikasigithubuser.utils.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailViewModel(private val db:DbModule) : ViewModel() {
    val resultDetailUser = MutableLiveData<Result<ResponseDetailUser>>()
    val resultFollowersUser = MutableLiveData<Result<MutableList<ResponseUserGithub.Item>>>()
    val resultFollowingUser = MutableLiveData<Result<MutableList<ResponseUserGithub.Item>>>()
    val resultSuksesFavorite = MutableLiveData<Boolean>()
    val resultDeleteFavorite = MutableLiveData<Boolean>()

    private var isFavorite = false
    fun setFavorite(item: ResponseUserGithub.Item?) {
        viewModelScope.launch {
            item?.let {
                if (isFavorite) {
                    db.userDao.delete(item)
                    resultDeleteFavorite.value = true

                } else{
                    db.userDao.insert(item)
                    resultSuksesFavorite.value = true
                }
            }
            isFavorite = !isFavorite
        }

    }

    fun findFavorite(id: Int, listenFavorite: () -> Unit) {
        viewModelScope.launch {
            val user = db.userDao.findById(id)
            if (user != null) {
                listenFavorite()
                isFavorite = true
            }
        }
    }

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

    class Factory(private val db: DbModule) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = DetailViewModel(db) as T
    }
}