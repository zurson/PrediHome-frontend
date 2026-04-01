package com.betoniarze.predihome.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.betoniarze.predihome.R
import com.betoniarze.predihome.core.communication.CommunicationManager
import com.betoniarze.predihome.presentation.ui.lists.history.HistoryRecord
import com.betoniarze.predihome.utilities.showToast

class HistoryViewModel : ViewModel() {

    private var page = 1
    private var pageSize = 10

    private val _history = mutableStateListOf<HistoryRecord>()
    val history: List<HistoryRecord> get() = _history

    private val _historyLoading = mutableStateOf(false)
    val historyLoading: Boolean get() = _historyLoading.value

    private val _historyLoaded = mutableStateOf(false)
    val historyLoaded: Boolean get() = _historyLoaded.value

    private val _showHistoryDetails = mutableStateOf(false)
    val showHistoryDetails: Boolean get() = _showHistoryDetails.value

    private val _clickedHistoryDetails = mutableStateOf(HistoryRecord())
    var clickedHistoryDetails = _clickedHistoryDetails.value

    private val _historyEmpty = mutableStateOf(true)
    val historyEmpty: Boolean get() = _historyEmpty.value

    private val _noMoreDataToFetch = mutableStateOf(false)
    val noMoreDataToFetch: Boolean get() = _noMoreDataToFetch.value


    fun toggleShowHistoryDetails() {
        _showHistoryDetails.value = !_showHistoryDetails.value
    }


    suspend fun fetchUserHistory() {
        _historyLoading.value = true

        val records = CommunicationManager().fetchHistoryRecords(page, pageSize)

        Log.e("HistoryViewModel", "Loading data from page $page size $pageSize")

        if (records != null) {
            Log.e("HistoryViewModel", "Records: ${records.size}")
            _noMoreDataToFetch.value = records.isEmpty()
            Log.e("HistoryViewModel", "No more data: $noMoreDataToFetch")

            _history.addAll(records)
            _historyEmpty.value = _history.isEmpty()

            _historyLoaded.value = true

            if (!_noMoreDataToFetch.value)
                page++
        } else {
            Log.e("HistoryViewModel", "Failed to fetch user history.")
        }


        _historyLoading.value = false
    }


    suspend fun loadMoreHistory(context: Context) {
        if (_noMoreDataToFetch.value)
            return

        val historySize = _history.size
        fetchUserHistory()
        val currentHistorySize = _history.size


        if (historySize == currentHistorySize)
            showToast(
                context = context,
                text = context.getString(R.string.history_no_more_data)
            )
        else
            showToast(
                context = context,
                text = context.getString(
                    R.string.history_data_fetched,
                    (currentHistorySize - historySize)
                )
            )
    }


    init {
        _history.clear()
    }
}