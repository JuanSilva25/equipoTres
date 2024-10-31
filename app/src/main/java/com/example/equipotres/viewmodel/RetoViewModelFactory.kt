package com.example.equipotres.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RetoViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RetoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RetoViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
