package com.daghovland.declinesquattracker.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.daghovland.declinesquattracker.data.AppDatabase
import com.daghovland.declinesquattracker.data.SetRepository
import com.daghovland.declinesquattracker.data.SquatSet
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// AndroidViewModel (vs plain ViewModel) gives us the Application context,
// which we need to build the database. The ViewModel survives Activity recreation
// (e.g. rotation), so the DB singleton is only created once per app process.
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = SetRepository(
        AppDatabase.getInstance(application).squatSetDao()
    )

    // stateIn converts the cold Flow from Room into a hot StateFlow the UI can read.
    // WhileSubscribed(5_000) keeps the upstream Flow alive for 5s after the last
    // subscriber disappears — avoids re-querying during brief config changes.
    val todaySets: StateFlow<List<SquatSet>> = repo.getTodaySets()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun logSet() {
        // launch runs the suspend function on a background coroutine;
        // viewModelScope cancels automatically when the ViewModel is cleared.
        viewModelScope.launch { repo.logSet() }
    }
}
