package com.example.provider.notes.di

import android.content.Context
import androidx.room.Room
import com.example.provider.notes.data.local.NoteDatabase
import com.example.provider.notes.data.repoImpl.NoteRepoImpl
import com.example.provider.notes.domain.repo.NoteRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NoteModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ):NoteDatabase{
        return Room
            .databaseBuilder(context,NoteDatabase::class.java,"Note_DB")
            .build()
    }

    @Provides
    @Singleton
    fun provideNoteRepoImpl(
        noteDatabase: NoteDatabase
    ):NoteRepo{
        return NoteRepoImpl(noteDao = noteDatabase.noteDao())
    }

}