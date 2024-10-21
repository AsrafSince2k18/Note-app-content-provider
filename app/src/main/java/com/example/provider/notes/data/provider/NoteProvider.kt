package com.example.provider.notes.data.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import com.example.provider.notes.data.local.NoteDao
import com.example.provider.notes.data.local.NoteDatabase
import com.example.provider.notes.data.local.NoteEntity
import com.example.provider.notes.data.local.TABLE_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class NoteProvider : ContentProvider() {

    lateinit var noteDatabase: NoteDatabase
    lateinit var noteDao: NoteDao

    companion object {
        const val AUTHORITY = "com.example.provider.notes.data.provider"
        val CONTENT_URI = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
    }

    val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, TABLE_NAME, 1)
        addURI(AUTHORITY, TABLE_NAME.plus("#"), 2)
    }

    override fun onCreate(): Boolean {
        val context = context ?: return false
        noteDatabase = Room
            .databaseBuilder(context, NoteDatabase::class.java, "Note_DB")
            .build()
        noteDao = noteDatabase.noteDao()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        order: String?
    ): Cursor {
        return when (uriMatcher.match(uri)) {
            1 -> {
                val query = SupportSQLiteQueryBuilder.builder(TABLE_NAME)
                    .columns(projection)
                    .selection(selection, selectionArgs)
                    .orderBy(order)
                    .create()
                runBlocking(Dispatchers.IO) { noteDatabase.query(query) }
            }

            2 -> {
                val noteId = ContentUris.parseId(uri)
                val query = SupportSQLiteQueryBuilder.builder(TABLE_NAME)
                    .columns(projection)
                    .selection("${NoteEntity::id.name} = ?", arrayOf(noteId))
                    .orderBy(order)
                    .create()
                runBlocking(Dispatchers.IO) { noteDatabase.query(query) }
            }

            else -> throw IllegalArgumentException("No data found")
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            1 -> "vnd.android.cursor.dir/$AUTHORITY.$TABLE_NAME"
            2 -> "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
            else -> null
        }
    }

    override fun insert(uri: Uri, value: ContentValues?): Uri? {

        val note = NoteEntity(
            title = value?.getAsString("title").orEmpty(),
            content = value?.getAsString("content").orEmpty(),
        )

        val id = runBlocking(Dispatchers.IO) { noteDao.insertData(noteEntity = note) }
        context?.contentResolver?.notifyChange(CONTENT_URI,null)
        return ContentUris.withAppendedId(CONTENT_URI, id)
    }

    override fun delete(uri: Uri, p1: String?, p2: Array<out String>?): Int {

        val noteID = ContentUris.parseId(uri)

        val noteEntity = runBlocking(Dispatchers.IO) { noteDao.getNote(noteID.toInt()) }

        val deleteNote = runBlocking(Dispatchers.IO) { noteDao.deleteNote(noteEntity = noteEntity) }
        context?.contentResolver?.notifyChange(CONTENT_URI,null)
        return deleteNote

    }

    override fun update(uri: Uri, value: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        val noteId = ContentUris.parseId(uri)
        val noteEntity = runBlocking(Dispatchers.IO) { noteDao.getNote(noteId.toInt()) }

        val updateNote = runBlocking(Dispatchers.IO) {
            noteDao.updateNote(
                noteEntity = noteEntity.copy(
                    id = noteId.toInt(),
                    title = value?.getAsString("title") ?: noteEntity.title,
                    content = value?.getAsString("content") ?: noteEntity.content,
                )
            )
        }
        context?.contentResolver?.notifyChange(CONTENT_URI,null)
        return updateNote
    }
}