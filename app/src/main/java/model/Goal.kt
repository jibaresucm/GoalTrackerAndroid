package model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "goals")
data class Goal (
    @PrimaryKey(autoGenerate = true) var id:Long = 0,
    @ColumnInfo(name =
    "title")
    val title: String,
    val startedDate: String,
    val deadLine: String,
    val description: String,
    val details: String
)