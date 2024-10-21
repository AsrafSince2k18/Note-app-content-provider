package com.example.provider.notes.presentance.screen.homeScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.provider.notes.data.local.NoteEntity
import com.example.provider.notes.domain.repo.NoteRepo
import com.example.provider.notes.presentance.screen.homeScreen.stateEvent.HomeEvent
import com.example.provider.notes.presentance.screen.homeScreen.stateEvent.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteRepo: NoteRepo
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    init {
        getAllNote()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.Title -> {
                _homeState.update {
                    it.copy(title = event.title)
                }
            }

            is HomeEvent.Content -> {
                _homeState.update {
                    it.copy(content = event.content)
                }
            }

            is HomeEvent.GetNote -> {
                fetchData(id = event.id?.toInt())
            }

            is HomeEvent.DeleteNote -> {
                deleteNote(noteEntity = event.noteEntity)
            }

            HomeEvent.SaveBtn -> {
                insertOrUpdate()
            }

        }
    }

    private fun fetchData(id: Int?) {
        viewModelScope.launch {

            if (id != null) {
                withContext(Dispatchers.IO) {
                    noteRepo.getNote(id).apply {
                        _homeState.update {
                            it.copy(
                                title = this.title,
                                content = this.content,
                                noteEntity = this,
                                id = this.id.toString()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun insertOrUpdate() {
        viewModelScope.launch {
            if (homeState.value.noteEntity != null) {
                noteRepo.updateNote(
                    noteEntity = NoteEntity(
                        id = homeState.value.id!!.toInt(),
                        title = homeState.value.title,
                        content = homeState.value.content
                    )
                )
                _homeState.update {
                    it.copy(title = "",
                        content = "",
                        noteEntity = null)
                }
            } else {
                noteRepo.insertNote(
                    noteEntity = NoteEntity(
                        title = homeState.value.title,
                        content = homeState.value.content
                    )
                )
                _homeState.update {
                    it.copy(title = "",
                        content = "",
                        noteEntity = null)
                }
            }
        }
    }

    private fun deleteNote(noteEntity: NoteEntity?) {
        viewModelScope.launch {
            if (noteEntity != null) {
                withContext(Dispatchers.IO) {
                    noteRepo.deleteNote(noteEntity)
                }
            }
        }
    }

    private fun getAllNote(){
        viewModelScope.launch {
            noteRepo.getAllNote().collect{noteList->
                _homeState.update {
                    it.copy(listNote = noteList)
                }
            }
        }
    }

}