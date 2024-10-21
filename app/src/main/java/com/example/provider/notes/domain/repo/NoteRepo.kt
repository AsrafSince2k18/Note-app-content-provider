package com.example.provider.notes.domain.repo

import com.example.provider.notes.data.local.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NoteRepo {

    suspend fun insertNote(noteEntity: NoteEntity)
    suspend fun updateNote(noteEntity: NoteEntity)
    suspend fun deleteNote(noteEntity: NoteEntity)
    suspend fun getNote(id: Int): NoteEntity
    fun getAllNote(): Flow<List<NoteEntity>>

}