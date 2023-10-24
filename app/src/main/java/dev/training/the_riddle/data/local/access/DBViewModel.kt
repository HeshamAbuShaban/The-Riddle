package dev.training.the_riddle.data.local.access

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import dev.training.the_riddle.app_system.AppInstance
import dev.training.the_riddle.data.local.entities.Level
import dev.training.the_riddle.data.local.entities.Riddle

class DBViewModel : AndroidViewModel(AppInstance.getInstance()) {

    private val repository: Repository = Repository

    //..Level##########################################################################

    fun insert(level: Level) = repository.insert(level)

    fun updateLevelEvaluation(levelNum: Int, newLevelEvaluation: Float) =
        repository.updateLevelEvaluation(levelNum, newLevelEvaluation)


    fun updateLevelEvaluationToDelete() = repository.updateLevelEvaluationToDelete()

    fun updateLevelStatusOpen(isOpen: Boolean, levelNum: Int) =
        repository.updateLevelStatusOpen(isOpen, levelNum)

    //..LiveDataResult
    fun levelBySubLevelAndMinPoint(subLevel: Int, minPointInt: Int): LiveData<List<Level>> =
        repository.levelBySubLevelAndMinPoint(subLevel, minPointInt)

    fun levelBySubLevel(subLevel: Int): LiveData<List<Level>> =
        repository.levelBySubLevel(subLevel)

    val allLevel: LiveData<List<Level>> = repository.allLevel

    //..Riddle##########################################################################

    fun insert(riddle: Riddle) =
        repository.insert(riddle)

    //..LiveDataResult
    fun getRiddleByLevelId(subLevel: Int): LiveData<List<Riddle>> =
        repository.getRiddleByLevelId(subLevel)

}