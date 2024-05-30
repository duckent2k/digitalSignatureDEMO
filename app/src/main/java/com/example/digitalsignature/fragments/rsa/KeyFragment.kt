package com.example.digitalsignature.fragments.rsa

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.digitalsignature.R
import com.example.digitalsignature.databinding.FragmentKeyBinding
import com.example.digitalsignature.generate.RSAAlgorithm
import com.example.digitalsignature.getValueFromPref
import com.example.digitalsignature.saveValueToPref

class KeyFragment : Fragment() {

    private lateinit var binding: FragmentKeyBinding
    private var keySize: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKeyBinding.inflate(inflater, container, false)
        initSpinner()
        initActionCreateKey()
        return binding.root
    }

    private fun initSpinner() {
        val items = resources.getStringArray(R.array.size)
        val arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        binding.spinnerKey.adapter = arrayAdapter
        binding.spinnerKey.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                keySize = items[position].toInt()
                requireContext().saveValueToPref("keySize", keySize.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun initActionCreateKey() {
        val sizeKey = requireContext().getValueFromPref("keySize", "")
        if (sizeKey.isNotEmpty()) {
            keySize = sizeKey.toInt()
        }
        with(binding) {
            btnCreateKey.setOnClickListener {
                showPrivateKey.text = RSAAlgorithm(keySize).e.toString()
                showPublicKey.text = RSAAlgorithm(keySize).d.toString()
            }
        }
    }


}