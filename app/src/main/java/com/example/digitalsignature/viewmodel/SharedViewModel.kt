package com.example.digitalsignature.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.digitalsignature.generate.RSA
import java.math.BigInteger

class SharedViewModel : ViewModel() {
    private val _filePath = MutableLiveData<String>()
    val filePath: LiveData<String> get() = _filePath

    private val rsa = RSA()

    private val _cipherText = MutableLiveData<BigInteger>()
    val cipherText: LiveData<BigInteger> get() = _cipherText

    val pubKey: Pair<BigInteger, BigInteger> get() = rsa.pubKey
    val priKey: Pair<BigInteger, BigInteger> get() = rsa.priKey

    fun encrypt(msg: String) {
        val encrypted = rsa.encrypt(msg, pubKey)
        _cipherText.value = encrypted
    }

    fun decrypt(msg: BigInteger): String {
        return rsa.decrypt(msg, priKey)
    }

    fun setFilePaths(paths: String) {
        _filePath.value = paths
    }

}