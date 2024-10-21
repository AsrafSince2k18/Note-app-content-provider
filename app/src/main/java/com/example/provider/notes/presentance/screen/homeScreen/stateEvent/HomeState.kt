package com.example.provider.notes.presentance.screen.homeScreen.stateEvent

import com.example.provider.notes.data.local.NoteEntity

data class HomeState(

    val title:String="",

    val content : String="",

    var noteEntity: NoteEntity?=null,

    val id : String?=null,

    val listNote : List<NoteEntity> = emptyList()

)
