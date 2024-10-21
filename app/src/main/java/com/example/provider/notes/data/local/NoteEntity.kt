package com.example.provider.notes.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

const val TABLE_NAME = "Notes"

@Entity(tableName = TABLE_NAME)
data class NoteEntity(
    @PrimaryKey(autoGenerate = false)
    val id : Int?=null,

    val title : String,

    val content:String

)
