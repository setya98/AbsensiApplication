package com.pertamina.absensiapplication.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pertamina.absensiapplication.R
import com.pertamina.absensiapplication.databinding.FragmentAddUserBinding
import com.pertamina.absensiapplication.model.User
import com.pertamina.absensiapplication.viewmodel.PermitViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddUserFragment : Fragment() {
    private lateinit var binding: FragmentAddUserBinding
    private val myViewModel: PermitViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddUserBinding.inflate(inflater, container, false).apply {
            myViewModel.getNetwork().observe(viewLifecycleOwner, Observer {

            })

            var listOH = listOf<User>()
            var listSenior = listOf<User>()
            myViewModel.getOH().observe(viewLifecycleOwner, Observer {
                listOH = it
                operatoinheadDropdown.setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        R.layout.dropdown_item,
                        it.map {
                            it.name
                        }
                    )
                )
            })

            myViewModel.getSenior().observe(viewLifecycleOwner, Observer {
                listSenior = it
                seniorDropdown.setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        R.layout.dropdown_item,
                        it.map {
                            it.name
                        }
                    )
                )
            })

            submitButton.setOnClickListener {
                val email = emailInput.text.toString()
                val password = passwordInput.text.toString()
                val name = passwordInput.text.toString()
                val number = employeeNumberInput.text.toString()
                val position = positionInput.text.toString()
                var senior = ""
                var operationHead = ""
                listSenior.forEach {
                    if (seniorDropdown.text.toString().equals(it.name, false)){
                        senior = it.userId
                    }
                }
                listOH.forEach {
                    if (operatoinheadDropdown.text.toString().equals(it.name, false)){
                        operationHead = it.userId
                    }
                }

                MaterialAlertDialogBuilder(context)
                    .setTitle("Apakah anda yakin?")
                    .setMessage("Apakah anda yakin ingin MENAMBAHKAN pengguna baru ini?")
                    .setNegativeButton("Tidak", null)
                    .setPositiveButton("Iya") { _, _ ->
                        myViewModel.createUser(email, password).observe(requireActivity(), Observer {
                            val user = User(
                                userId = it.userId,
                                employeeNumber = number,
                                name = name,
                                position = position,
                                senior = senior,
                                operationHead = operationHead

                            )
                            myViewModel.createUserData(user.userId, user)
                        })
                    }
                    .show()
                val action = AddUserFragmentDirections.actionShowUserList()
                findNavController().navigate(action)
            }
        }
        return binding.root
    }

}