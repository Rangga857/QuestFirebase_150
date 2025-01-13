package com.example.firebase.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase.model.Mahasiswa
import com.example.firebase.navigation.DestinasiDetail
import com.example.firebase.repository.NetworkRepository
import com.example.firebase.repository.RepositoryMhs
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class DetailMhsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoryMhs: RepositoryMhs,
) : ViewModel() {
    private val _nim: String = checkNotNull(savedStateHandle[DestinasiDetail.NIM])

    val detailUiState: StateFlow<DetailUiState> = repositoryMhs.getMhs(_nim)
        .filterNotNull()
        .map {
            DetailUiState(
                detailUiEvent = it.toDetailUiEvent(),
                isLoading = false,
            )
        }
        .onStart {
            emit(DetailUiState(isLoading = true))
            delay(600)
        }
        .catch {
            emit(
                DetailUiState(
                    isLoading = false,
                    isError = true,
                    errorMessage = it.message ?: "Terjadi kesalahan",
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue =  DetailUiState(
                isLoading = true,
            ),
        )
}
//data class untuk menampung data yang akan ditampilkan di UI
data class DetailUiState(
    val detailUiEvent: MahasiswaEvent = MahasiswaEvent(),
    val isLoading : Boolean = false,
    val isError: Boolean = false,
    val errorMessage : String = ""
){
    val isUiEventEmpty : Boolean
        get() = detailUiEvent == MahasiswaEvent()
    val isUiEventNotEmpty : Boolean
        get() = detailUiEvent != MahasiswaEvent()
}

//memindahkan data dari entry ke ui
fun Mahasiswa.toDetailUiEvent() : MahasiswaEvent{
    return MahasiswaEvent(
        nim = nim,
        nama = nama,
        jenisKelamin = gender,
        alamat = alamat,
        kelas = kelas,
        angkatan = angkatan,
        judulskripsi = judulskripsi,
        dospem1 = dospem1,
        dospem2 = dospem2
    )
}