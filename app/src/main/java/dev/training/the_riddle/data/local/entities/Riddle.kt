package dev.training.the_riddle.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Level::class,
        parentColumns = ["level_num"],
        childColumns = ["sub_level_num"],
        onDelete = CASCADE,
        onUpdate = CASCADE
    )]
)
data class Riddle(
    val riddleNum: Int,
    //the Question text
    val riddleQText: String,
    val riddleGivenPoint: Int,
    // the Time for the RiddleQuestion
    val riddleTimeBySec: Long,
    //due the chose constructor
    val answer1: String? = null,
    val answer2: String? = null,
    val answer3: String? = null,
    val answer4: String? = null,
    // answers dependees on the Question Type
    // chose and complete
    val theRightInTextAnswer: String? = null,
    // helper text
    val hintAnswer: String? = null,
    //from enum
    //ech type have a different constructor
    // notes that u need the number value out of the Enum
    val riddleType: Int = 0,
    //for DataBase Relate
    //attach with levels class
    val sub_level_num: Int = 0,
) {
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
}