package org.d3if3126.assessment2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if3126.assessment2.model.Danus

@Dao
interface DanusDao {
    @Insert
    suspend fun insert(danus: Danus)

    @Update
    suspend fun update(danus: Danus)

    @Query("SELECT * FROM danus ORDER BY jenisBarang, hargaBarang, namaBarang, namaToko ASC")
    fun getDanus(): Flow<List<Danus>>

    @Query("SELECT * FROM danus WHERE id = :id")
    suspend fun getDanusById(id: Long): Danus?

    @Query("DELETE FROM danus WHERE id = :id")
    suspend fun deleteById(id: Long)
}