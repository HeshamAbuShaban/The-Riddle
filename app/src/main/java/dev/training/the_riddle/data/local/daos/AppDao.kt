package dev.training.the_riddle.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.training.the_riddle.data.local.entities.Level
import dev.training.the_riddle.data.local.entities.Riddle

@Dao
interface AppDao {

    //..Level##########################################################################

    @Insert
    fun insertLevel(level: Level)

    @Query("UPDATE Level SET levelEvaluation =:newLevelEvaluation WHERE levelNum =:levelNum ")
    fun updateLevelEvaluation(levelNum: Int, newLevelEvaluation: Float)

    @Query("UPDATE Level SET levelEvaluation = 0.0")
    fun updateLevelEvaluationToDelete()

    @Query("UPDATE Level SET levelOpenStatus =:isOpen WHERE levelNum=:levelNum")
    fun updateLevelStatusOpen(isOpen: Boolean, levelNum: Int)

    //********************************************************

    @Query("select * from Level where levelNum=:subLevel and minPointToUnlock=:minPointInt")
    fun getLevelBySubLevelAndMinPoint(subLevel: Int, minPointInt: Int): LiveData<List<Level>>

    @Query("select * from Level where levelNum=:subLevel")
    fun getLevelBySubLevel(subLevel: Int): LiveData<List<Level>>

    @Query("select * from Level")
    fun getAllLevel(): LiveData<List<Level>>

    //..Riddle##########################################################################

    @Insert
    fun insertRiddle(riddle: Riddle)

    @Query("select * from Riddle where sub_level_num=:subLevel")
    fun getRiddleByLevelId(subLevel: Int): LiveData<List<Riddle>>

}