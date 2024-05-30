package com.example.digitalsignature.fragments.rsa

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.digitalsignature.databinding.FragmentSenderBinding
import com.example.digitalsignature.generate.RSAAlgorithm
import com.example.digitalsignature.getFileName
import com.example.digitalsignature.viewmodel.SenderViewModel
import com.example.digitalsignature.viewmodel.SharedViewModel

@RequiresApi(Build.VERSION_CODES.O)
class SenderFragment : Fragment() {

    private lateinit var binding: FragmentSenderBinding
    private val senderViewModel: SenderViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val generate = RSAAlgorithm(1024)

    private val pickFileLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val filePath = getFileName(requireContext(), it)
                senderViewModel.setFilePath(filePath)
                senderViewModel.calculateSha1Hash(requireContext(), it)
                sharedViewModel.setFilePaths(filePath)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSenderBinding.inflate(inflater, container, false)
        initAction()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun initAction() {

        with(binding) {
            senderViewModel.filePath.observe(viewLifecycleOwner) {
                inputUri.text = it
            }

            btnLoadUri.setOnClickListener {
                pickFileLauncher.launch("*/*")
            }
            btnCreateSignature.setOnClickListener {
                senderViewModel.sha1Hash.value?.let {
                    showHashSha1.text = senderViewModel.sha1ToBigInteger(it).toString()
                }

                val filePath = senderViewModel.filePath.value
                if (!filePath.isNullOrEmpty()) {
                    val signature = generate.encryptSignature(filePath, generate.d, generate.n)
                    showSignature.text = signature.toString()
                } else {
                    showSignature.text = "File path is empty"
                }
            }
        }
    }

}