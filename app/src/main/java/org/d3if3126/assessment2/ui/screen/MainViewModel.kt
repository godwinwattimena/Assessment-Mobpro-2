package org.d3if3126.assessment2.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.d3if3126.assessment2.database.DanusDao
import org.d3if3126.assessment2.model.Danus

class MainViewModel(dao: DanusDao) : ViewModel() {

    val data: StateFlow<List<Danus>> = dao.getDanus().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}