package net.thebookofcode.www.list_panelattempt.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "bookmark")
data class BookmarkWord(
    @PrimaryKey()
    var key: String,
    var value: String,
    var syntax: String?,
    var code: Int,
    var date: Date

)