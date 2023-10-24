package dev.training.the_riddle.data.local.access

import androidx.lifecycle.LiveData
import dev.training.the_riddle.app_system.AppInstance
import dev.training.the_riddle.data.local.AppDatabase
import dev.training.the_riddle.data.local.daos.AppDao
import dev.training.the_riddle.data.local.entities.Level
import dev.training.the_riddle.data.local.entities.Riddle
import java.util.concurrent.ExecutorService

object Repository {

    private val appDao: AppDao = AppDatabase.getDatabase(AppInstance.getInstance()).appDao()
    private val dbWriteExecutor: ExecutorService = AppDatabase.databaseWriteExecutor

    //..Level##########################################################################

    fun insert(level: Level) = dbWriteExecutor.execute { appDao.insertLevel(level) }

    fun updateLevelEvaluation(levelNum: Int, newLevelEvaluation: Float) =
        dbWriteExecutor.execute { appDao.updateLevelEvaluation(levelNum, newLevelEvaluation) }

    fun updateLevelEvaluationToDelete() =
        dbWriteExecutor.execute { appDao.updateLevelEvaluationToDelete() }

    fun updateLevelStatusOpen(isOpen: Boolean, levelNum: Int) =
        dbWriteExecutor.execute { appDao.updateLevelStatusOpen(isOpen, levelNum) }

    //..LiveDataResult
    fun levelBySubLevelAndMinPoint(subLevel: Int, minPointInt: Int): LiveData<List<Level>> =
        appDao.getLevelBySubLevelAndMinPoint(subLevel, minPointInt)

    fun levelBySubLevel(subLevel: Int): LiveData<List<Level>> = appDao.getLevelBySubLevel(subLevel)

    val allLevel: LiveData<List<Level>> = appDao.getAllLevel()

    //..Riddle##########################################################################

    fun insert(riddle: Riddle) = dbWriteExecutor.execute { appDao.insertRiddle(riddle) }

    //..LiveDataResult

    fun getRiddleByLevelId(subLevel: Int): LiveData<List<Riddle>> =
        appDao.getRiddleByLevelId(subLevel)

}