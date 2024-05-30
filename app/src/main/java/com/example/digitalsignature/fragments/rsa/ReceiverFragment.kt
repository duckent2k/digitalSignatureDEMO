package com.example.digitalsignature.fragments.rsa

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.digitalsignature.copyToClipboard
import com.example.digitalsignature.databinding.FragmentReceiverBinding
import com.example.digitalsignature.getFileName
import com.example.digitalsignature.viewmodel.ReceiverViewModel
import com.example.digitalsignature.viewmodel.SharedViewModel

@RequiresApi(Build.VERSION_CODES.O)
class ReceiverFragment : Fragment() {

    private lateinit var binding: FragmentReceiverBinding
    private val receiverViewModel: ReceiverViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val uploadFileURI =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri!!.let {
                val filePath = getFileName(requireContext(), it)
                receiverViewModel.setFilePath(filePath)
                receiverViewModel.calculateSha1Hash(requireContext(), it)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReceiverBinding.inflate(inflater, container, false)
        initAction()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun initAction() {
        with(binding) {
            receiverViewModel.decrypt.observe(viewLifecycleOwner) {
                showDecryptSignature.text = it
            }

            receiverViewModel.filePath.observe(viewLifecycleOwner) {
                inputUri.text = it
            }

            btnLoadUri.setOnClickListener {
                uploadFileURI.launch("*/*")
            }

            btnDecryptSignature.setOnClickListener {
                receiverViewModel.sha1Hash.value?.let {
                    showHashSha1.text = receiverViewModel.sha1ToBigInteger(it).toString()
                }
                receiverViewModel.filePath.value?.let {
                    receiverViewModel.decryptSignature(it)
                }
            }

            showHashSha1.setOnClickListener{
                copyToClipboard(requireContext(), showHashSha1.text.toString())
            }

            btnCheck.setOnClickListener {
                val decrypt = receiverViewModel.decrypt.value ?: ""
                val senderFilePath = sharedViewModel.filePath.value ?: ""

                if (decrypt == senderFilePath) {
                    Toast.makeText(requireContext(), "Chữ ký nguyên vẹn", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(requireContext(), "Chữ ký bị thay đổi", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}