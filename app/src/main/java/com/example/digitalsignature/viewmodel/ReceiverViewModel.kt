package com.example.digitalsignature.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitalsignature.generate.RSAAlgorithm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.math.BigInteger
import java.security.MessageDigest

class ReceiverViewModel : ViewModel() {
    private val _filePath = MutableLiveData<String>()
    val filePath: MutableLiveData<String> = _filePath

    private val _sha1Hash = MutableLiveData<ByteArray?>()
    val sha1Hash: MutableLiveData<ByteArray?> = _sha1Hash

    private val generate = RSAAlgorithm(1024)
    private val _decrypt = MutableLiveData<String>()
    val decrypt: MutableLiveData<String> = _decrypt

    fun setFilePath(path: String) {
        _filePath.value = path
    }

    fun calculateSha1Hash(context: Context, uri: Uri) {
        viewModelScope.launch {
            val sha1 = getSha1FromUri(context, uri)
            _sha1Hash.value = sha1
        }
    }

    fun decryptSignature(filePath: String) {
        viewModelScope.launch {
            val signature = generate.encryptSignature(filePath, generate.d, generate.n)
            val decrypted = generate.decryptSignature(signature, generate.e, generate.n)
            _decrypt.value = decrypted
        }
    }

    private suspend fun getSha1FromUri(context: Context, uri: Uri): ByteArray? {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val sha1 = MessageDigest.getInstance("SHA-1")
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (inputStream?.read(buffer).also { bytesRead = it ?: -1 } != -1) {
                    sha1.update(buffer, 0, bytesRead)
                }
                inputStream?.close()
                sha1.digest()
            } catch (e: Exception) {
                null
            }
        }
    }

    fun sha1ToBigInteger(sha1: ByteArray?): BigInteger {
        return sha1?.let { BigInteger(1, it) } ?: BigInteger.ZERO
    }
}