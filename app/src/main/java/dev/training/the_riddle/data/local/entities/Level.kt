package dev.training.the_riddle.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Level(
    @PrimaryKey
    val levelNum: Int,
    val minPointToUnlock: Int,

    var levelOpenStatus: Boolean = false,
    val levelEvaluation: Float = 0.0f,
)