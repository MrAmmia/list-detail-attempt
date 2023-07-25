package net.thebookofcode.www.list_panelattempt.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cpp")
data class CppWord(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var key: String,
    var value: String,
    var syntax: String?,
    var code: Int
)

