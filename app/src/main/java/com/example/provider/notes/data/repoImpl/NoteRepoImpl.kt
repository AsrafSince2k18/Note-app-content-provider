package com.example.provider.notes.data.repoImpl

import com.example.provider.notes.data.local.NoteDao
import com.example.provider.notes.data.local.NoteEntity
import com.example.provider.notes.domain.repo.NoteRepo
import kotlinx.coroutines.flow.Flow

class NoteRepoImpl (
    private val noteDao: NoteDao
) : NoteRepo{
    override suspend fun insertNote(noteEntity: NoteEntity) {
        noteDao.insertData(noteEntity)
    }

    override suspend fun updateNote(noteEntity: NoteEntity) {
        noteDao.updateNote(noteEntity)
    }

    override suspend fun deleteNote(noteEntity: NoteEntity) {
        noteDao.deleteNote(noteEntity = noteEntity)
    }

    override suspend fun getNote(id: Int): NoteEntity {
        return noteDao.getNote(id=id)
    }

    override fun getAllNote(): Flow<List<NoteEntity>> {
        return noteDao.getAllNote()
    }
}