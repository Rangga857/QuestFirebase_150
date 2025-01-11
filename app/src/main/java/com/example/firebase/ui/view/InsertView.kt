package com.example.firebase.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.firebase.ui.viewmodel.FormErrorState
import com.example.firebase.ui.viewmodel.MahasiswaEvent

@Composable
fun FormMahasiswa(
    mahasiswaEvent: MahasiswaEvent = MahasiswaEvent(),
    onValueChange: (MahasiswaEvent) -> Unit,
    errorState: FormErrorState = FormErrorState(),
    modifier: Modifier = Modifier
){
    val jenisKelamin = listOf("Laki-Laki", "Perempuan")
    val kelas = listOf("A", "B","C","D","E")
}
