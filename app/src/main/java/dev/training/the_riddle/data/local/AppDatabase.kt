package dev.training.the_riddle.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.training.the_riddle.data.local.daos.AppDao
import dev.training.the_riddle.data.local.entities.Level
import dev.training.the_riddle.data.local.entities.Riddle
import dev.training.the_riddle.data.source_reader.GameAssetsReader
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = [Riddle::class, Level::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

    private class RoomCallbackImpl : Callback() {
        //        @OptIn(DelicateCoroutinesApi::class)
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
//            GlobalScope.launch(Dispatchers.IO) {
            GameAssetsReader.getInstance().readRiddles()
//            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private const val NUMBER_OF_THREADS = 4
        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(AppDatabase::class.java) {
                return buildDatabase(context).also {
                    INSTANCE = it
                }
            }

        private fun buildDatabase(context: Context): AppDatabase {
            return databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "riddle_database"
            )
                .addCallback(RoomCallbackImpl())
                .build()
        }

    }
}
