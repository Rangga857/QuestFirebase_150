package com.example.firebase.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.firebase.model.Mahasiswa
import com.example.firebase.repository.RepositoryMhs

class InsertViewModel (
    private val mhs: RepositoryMhs
): ViewModel(){
    var uiEvent: InsertUiState by mutableStateOf(InsertUiState())

    var uiState: FormState by mutableStateOf(FormState.Idle)

    fun updateState(mahasiswaEvent: MahasiswaEvent){
        uiEvent = uiEvent.copy(
            insertUiEvent = mahasiswaEvent
        )
    }
}
sealed class FormState{
    object Idle : FormState()
    object Loading : FormState()
    data class Success(val message : String) : FormState()
    data class Error(val message: String): FormState()
}

data class InsertUiState(
    val insertUiEvent: MahasiswaEvent = MahasiswaEvent(),
    val isEntryValid : FormErrorState = FormErrorState()
)
data class FormErrorState(
    val nim: String? = null,
    val nama: String? = null,
    val jenisKelamin: String? = null,
    val alamat: String? = null,
    val kelas: String? = null,
    val angkatan: String? = null
){
    fun isValid(): Boolean{
        return nim == null && nama == null && jenisKelamin == null
                && alamat == null && kelas == null && angkatan == null
    }
}
fun MahasiswaEvent.toMhsModel():Mahasiswa = Mahasiswa(
    nim = nim,
    nama = nama,
    gender = jenisKelamin,
    alamat = alamat,
    kelas = kelas,
    angkatan = angkatan
)

data class MahasiswaEvent(
    val nim: String = "",
    val nama: String = "",
    val jenisKelamin: String = "",
    val alamat: String = "",
    val kelas: String = "",
    val angkatan: String = "",
)

