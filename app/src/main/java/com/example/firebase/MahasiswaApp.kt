package com.example.firebase

import android.app.Application
import com.example.firebase.di.MahasiswaContainer

class MahasiswaApp: Application() {
    //Fungsinya untuk menyimpan interface ContainerApp
    lateinit var containerApp: MahasiswaContainer

    override fun onCreate() {
        super.onCreate()
        //membuat instance ContainerApp
        containerApp = MahasiswaContainer(this)
        //instance adalah object yang dibuat dari class
    }
}