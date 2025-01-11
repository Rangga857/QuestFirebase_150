package com.example.firebase.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.firebase.ui.viewmodel.FormErrorState
import com.example.firebase.ui.viewmodel.FormState
import com.example.firebase.ui.viewmodel.HomeUiState
import com.example.firebase.ui.viewmodel.InsertUiState
import com.example.firebase.ui.viewmodel.MahasiswaEvent

@Composable
fun InsertBodyMhs(
    modifier: Modifier = Modifier,
    onValueChange: (MahasiswaEvent) -> Unit,
    uiState : InsertUiState,
    onClick: () -> Unit,
    homeUiState: FormState
){
    Column (
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

    }
}

@Composable
fun FormMahasiswa(
    mahasiswaEvent: MahasiswaEvent = MahasiswaEvent(),
    onValueChange: (MahasiswaEvent) -> Unit,
    errorState: FormErrorState = FormErrorState(),
    modifier: Modifier = Modifier
){
    val jenisKelamin = listOf("Laki-Laki", "Perempuan")
    val kelas = listOf("A", "B","C","D","E")

    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        //Nama
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.nama,
            onValueChange = {
                onValueChange(mahasiswaEvent.copy(nama = it))
            },
            label = {Text(text = "Nama")},
            isError = errorState.nama !=null,
            placeholder = {Text(text = "Masukkan Nama")},
        )
        Text(
            text = errorState.nama?: "",
            color = Color.Red
        )

        //nim
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.nim,
            onValueChange = {
                onValueChange(mahasiswaEvent.copy(nim = it))
            },
            label = {Text(text = "NIM")},
            isError = errorState.nim !=null,
            placeholder = {Text(text = "Masukkan NIM")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text(
            text = errorState.nim?: "",
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(16.dp))

        //Jenis Kelamin
        Text(text = "Jenis Kelamin")
        Row (
            modifier = Modifier.fillMaxWidth()
        ){
            jenisKelamin.forEach { jk ->
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ){
                    RadioButton(
                        selected = mahasiswaEvent.jenisKelamin == jk,
                        onClick = {
                            onValueChange(mahasiswaEvent.copy(jenisKelamin = jk))
                        }
                    )
                    Text(
                        text = jk
                    )
                }
            }
        }
        Text(
            text = errorState.jenisKelamin?: "",
            color = Color.Red
        )

        //Alamat
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.alamat,
            onValueChange = {
                onValueChange(mahasiswaEvent.copy(alamat = it))
            },
            label = {Text(text = "Alamat")},
            isError = errorState.alamat !=null,
            placeholder = {Text(text = "Masukkan Alamat")},
        )
        Text(
            text = errorState.alamat?: "",
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(16.dp))

        //Kelas
        Text(text = "Kelas")
        Row (
            modifier = Modifier.fillMaxWidth()
        ){
            kelas.forEach { kelas ->
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ){
                    RadioButton(
                        selected = mahasiswaEvent.kelas == kelas,
                        onClick = {
                            onValueChange(mahasiswaEvent.copy(kelas = kelas))
                        }
                    )
                    Text(
                        text = kelas
                    )
                }
            }
        }
        Text(
            text = errorState.kelas?: "",
            color = Color.Red
        )

        //Angkatan
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.angkatan,
            onValueChange = {
                onValueChange(mahasiswaEvent.copy(angkatan = it))
            },
            label = {Text(text = "Angkatan")},
            isError = errorState.angkatan !=null,
            placeholder = {Text(text = "Masukkan Angkatan")},
        )
        Text(
            text = errorState.angkatan?: "",
            color = Color.Red
        )

    }
}

