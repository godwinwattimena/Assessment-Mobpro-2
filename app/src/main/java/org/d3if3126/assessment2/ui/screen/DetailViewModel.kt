package org.d3if3126.assessment2.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3126.assessment2.database.DanusDao
import org.d3if3126.assessment2.model.Danus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailViewModel (private val dao: DanusDao) : ViewModel() {

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    fun insert(namaToko: String, namaBarang: String, hargaBarang: String, jenisBarang: String) {
        val danus = Danus(
            namaToko = namaToko,
            namaBarang = namaBarang,
            hargaBarang = hargaBarang,
            jenisBarang = jenisBarang,
            tanggal = formatter.format(Date())
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(danus)
        }
    }

    suspend fun getDanus(id: Long): Danus? {
        return dao.getDanusById(id)
    }

    fun update(id: Long, namaToko: String, namaBarang: String, hargaBarang: String, jenisBarang: String) {
        val danus = Danus(
            id = id,
            namaToko = namaToko,
            namaBarang = namaBarang,
            hargaBarang = hargaBarang,
            jenisBarang = jenisBarang,
            tanggal = formatter.format(Date())
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.update(danus)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}