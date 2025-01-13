package com.example.firebase.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firebase.ui.viewmodel.FormErrorState
import com.example.firebase.ui.viewmodel.FormState
import com.example.firebase.ui.viewmodel.InsertUiState
import com.example.firebase.ui.viewmodel.InsertViewModel
import com.example.firebase.ui.viewmodel.MahasiswaEvent
import com.example.firebase.ui.viewmodel.PenyediaViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertMhsView(
    onBack: () ->Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertViewModel = viewModel(factory = PenyediaViewModel.Factory)
){
    val uiState = viewModel.uiState
    val uiEvent = viewModel.uiEvent
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    //Observasi perubahan state untuk snackbar dan navigate
    LaunchedEffect(uiState) {
        when(uiState){
            is FormState.Success ->{
                println(
                    "InsertMhsView: uiState is FormState.Success, navigate to home"
                    + uiState.message
                )

                coroutineScope.launch {
                    snackbarHostState.showSnackbar(uiState.message)
                }
                delay(700)
                onNavigate()
                viewModel.resetSnackBarMessage()
            }

            is FormState.Error ->{
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(uiState.message)
                }
            }

            else -> Unit
        }
    }
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Tambah Mahasiswa") },
                navigationIcon = {
                    Button(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier =Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            InsertBodyMhs(
                uiState = uiEvent,
                homeUiState = uiState,
                onValueChange = {updateEvent ->
                    viewModel.updateState(updateEvent)
                },
                onClick = {
                    if (viewModel.validateFields()){
                        viewModel.insertMhs()
                    }
                }
            )
        }
    }
}
@Composable
fun InsertBodyMhs(
    modifier: Modifier = Modifier,
    onValueChange: (MahasiswaEvent) -> Unit,
    uiState : InsertUiState,
    onClick: () -> Unit,
    homeUiState: FormState
){
    /*Column (
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        FormMahasiswa(
            mahasiswaEvent = uiState.insertUiEvent,
            onValueChange = onValueChange,
            errorState = uiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = homeUiState !is FormState.Loading
        ) {
            if (homeUiState is FormState.Loading){
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 8.dp)
                )
                Text("Loading...")
            }else{
                Text("Add")
            }
        }
    }*/
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(2.dp, Color.Gray, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                FormMahasiswa(
                    mahasiswaEvent = uiState.insertUiEvent,
                    onValueChange = onValueChange,
                    errorState = uiState.isEntryValid,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = homeUiState !is FormState.Loading
            ) {
                if (homeUiState is FormState.Loading){
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 8.dp)
                    )
                    Text("Loading...")
                }else{
                    Text("Add")
                }
            }
        }
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
            .fillMaxSize()
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
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.judulskripsi,
            onValueChange = {
                onValueChange(mahasiswaEvent.copy(judulskripsi = it))
            },
            label = {Text(text = "Judul Skripsi")},
            isError = errorState.judulskripsi !=null,
            placeholder = {Text(text = "Masukkan Judul Skripsi")},
        )
        Text(
            text = errorState.judulskripsi?: "",
            color = Color.Red,
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.dospem1,
            onValueChange = {
                onValueChange(mahasiswaEvent.copy(dospem1 = it))
            },
            label = {Text(text = "Dosen Pembimbing 1")},
            isError = errorState.dospem1 !=null,
            placeholder = {Text(text = "Masukkan Dosen Pembimbing 1")},
        )
        Text(
            text = errorState.dospem1?: "",
            color = Color.Red
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.dospem2,
            onValueChange = {
                onValueChange(mahasiswaEvent.copy(dospem2 = it))
            },
            label = {Text(text = "Dosen Pembimbing 2")},
            isError = errorState.dospem2 !=null,
            placeholder = {Text(text = "Masukkan Dosen Pembimbing 2")},
        )
        Text(
            text = errorState.dospem2?: "",
            color = Color.Red
        )

    }
}

