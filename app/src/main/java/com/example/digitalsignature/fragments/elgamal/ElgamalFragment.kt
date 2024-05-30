package com.example.digitalsignature.fragments.elgamal

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.digitalsignature.databinding.FragmentElgamalBinding
import com.example.digitalsignature.generate.ElgamalAlgorithm
import com.example.digitalsignature.getFileName

@RequiresApi(Build.VERSION_CODES.O)
class ElgamalFragment : Fragment() {

    private lateinit var binding: FragmentElgamalBinding
    private var message = ""
    private var cipherText: ElgamalAlgorithm.CipherText? = null
    private var filePath = ""

    private val pickFileLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                filePath = getFileName(requireContext(), it)
                (binding.etMessage as TextView).text = filePath
            }
        }

    private val inputLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                filePath = getFileName(requireContext(), it)
                binding.tvShowInput.text = filePath
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentElgamalBinding.inflate(inflater, container, false)
        initAction()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun initAction() {
        val elgamal = ElgamalAlgorithm(256)
        with(binding) {
            btnCreateKeyElgamal.setOnClickListener {
                numberP.text = elgamal.p.toString()
                numberG.text = elgamal.g.toString()
                numberY.text = elgamal.y.toString()
                numberX.text = elgamal.x.toString()
            }

            btnLoadUri.setOnClickListener {
                pickFileLauncher.launch("*/*")
            }

            btnLoadFile.setOnClickListener {
                inputLauncher.launch("*/*")
            }

            btnEncrypt.setOnClickListener {
                message = etMessage.text.toString()
                cipherText = elgamal.encrypt(message)
                tvEncrypted.text = cipherText.toString()
            }

            btnDecrypt.setOnClickListener {
                tvDecryptedReceived.text = elgamal.decrypt(cipherText!!)
                tvEncryptedReceived.text = cipherText.toString()
            }
            btnCheck.setOnClickListener {
                if (elgamal.decrypt(cipherText!!) == filePath) {
                    Toast.makeText(requireContext(), "Chữ ký nguyên vẹn", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Chữ ký bị thay đổi ", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
    }


}