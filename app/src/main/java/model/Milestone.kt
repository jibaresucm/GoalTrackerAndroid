package model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity (tableName = "milestones")
data class Milestone (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val description: String,
    val goalId: Long
)