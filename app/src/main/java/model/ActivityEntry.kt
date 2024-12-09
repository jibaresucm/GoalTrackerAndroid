package model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "activities")
data class ActivityEntry (
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val date: String,
    val location: String,
    val description: String,
    val goalId: Long,
    val image: String
)