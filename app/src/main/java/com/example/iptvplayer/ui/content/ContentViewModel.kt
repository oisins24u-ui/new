package com.example.iptvplayer.ui.content

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.iptvplayer.data.model.Category
import com.example.iptvplayer.data.model.SeriesStream
import com.example.iptvplayer.data.model.Stream
import com.example.iptvplayer.data.model.VodStream
import com.example.iptvplayer.data.repository.Repository
import kotlinx.coroutines.launch

class ContentViewModel(private val repository: Repository) : ViewModel() {

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _streams = MutableLiveData<List<Stream>>()
    val streams: LiveData<List<Stream>> = _streams

    private val _vodStreams = MutableLiveData<List<VodStream>>()
    val vodStreams: LiveData<List<VodStream>> = _vodStreams

    private val _seriesStreams = MutableLiveData<List<SeriesStream>>()
    val seriesStreams: LiveData<List<SeriesStream>> = _seriesStreams

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadLiveCategories() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getLiveCategories()
                if (response != null && response.isSuccessful) {
                    _categories.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Failed to load categories"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadSeriesCategories() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getSeriesCategories()
                if (response != null && response.isSuccessful) {
                    _categories.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Failed to load categories"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadVodCategories() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getVodCategories()
                if (response != null && response.isSuccessful) {
                    _categories.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Failed to load categories"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadLiveStreams(categoryId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getLiveStreams(categoryId)
                if (response != null && response.isSuccessful) {
                    _streams.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Failed to load streams"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadSeries(categoryId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getSeries(categoryId)
                if (response != null && response.isSuccessful) {
                    _seriesStreams.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Failed to load series"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadVodStreams(categoryId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getVodStreams(categoryId)
                if (response != null && response.isSuccessful) {
                    _vodStreams.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Failed to load streams"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class ContentViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
