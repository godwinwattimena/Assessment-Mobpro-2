package org.d3if3126.assessment2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "danus")
data class Danus(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val namaToko: String,
    val namaBarang: String,
    val hargaBarang: String,
    val jenisBarang: String,
    val tanggal: String
)
