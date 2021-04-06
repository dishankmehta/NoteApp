package com.example.noteapp.database.constants


class Queries {

    companion object {
        const val SELECT_ALL_NOTES_QUERY: String = "SELECT * FROM note"
        const val DELETE_ALL_QUERY: String = "DELETE FROM note"
        const val SELECT_NOTE_BY_ID: String = "SELECT * FROM note WHERE nid = :id"
    }
}