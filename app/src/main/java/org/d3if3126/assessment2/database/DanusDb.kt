package org.d3if3126.assessment2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3if3126.assessment2.model.Danus

@Database(entities = [Danus::class], version = 1, exportSchema = false)
abstract class DanusDb: RoomDatabase() {
    abstract val dao: DanusDao

    companion object{
        @Volatile
        private var INSTANCE: DanusDb? = null

        fun getInstance(context: Context): DanusDb{
            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DanusDb::class.java,
                        "danus.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}