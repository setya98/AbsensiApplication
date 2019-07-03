package com.pertamina.absensiapplication.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.pertamina.absensiapplication.R
import com.pertamina.absensiapplication.api.NetworkState
import com.pertamina.absensiapplication.databinding.FragmentProfileBinding
import com.pertamina.absensiapplication.model.User
import com.pertamina.absensiapplication.viewmodel.PermitViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {
    private val myViewModel: PermitViewModel by viewModel()
    private val auth by inject<FirebaseAuth>()
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false).apply {
            myViewModel.getNetwork().observe(viewLifecycleOwner, Observer {
                profileRefresh.isRefreshing = it == NetworkState.RUNNING
                profileRefresh.setOnRefreshListener {
                    myViewModel.refreshUser()
                }
                when (it) {
                    NetworkState.SUCCESS -> {
                        profileRefresh.isEnabled = false
                        rootLayout.visibility = View.VISIBLE
                        emptyListImage.visibility = View.GONE
                        emptyListText.visibility = View.GONE
                        emptyListButton.visibility = View.GONE
                    }
                    NetworkState.FAILED -> {
                        profileRefresh.isEnabled = true
                        com.bumptech.glide.Glide.with(requireContext()).load(R.drawable.ic_healing_black_24dp)
                            .into(emptyListImage)
                        rootLayout.visibility = View.GONE
                        emptyListText.text = getString(R.string.technical_error)
                        emptyListImage.visibility = View.VISIBLE
                        emptyListText.visibility = View.VISIBLE
                        emptyListButton.visibility = View.VISIBLE
                        emptyListButton.setOnClickListener {
                            myViewModel.refreshUser()
                        }
                    }
                    else -> {
                        rootLayout.visibility = View.GONE
                    }
                }
            })
            myViewModel.getUser().observe(viewLifecycleOwner, Observer {
                user = it ?: User()
            })
            manageUserMenu.setOnClickListener {
                val action = ProfileFragmentDirections.actionManageUsers()
                findNavController().navigate(action)
            }
            editPasswordMenu.setOnClickListener {
                MaterialAlertDialogBuilder(context)
                    .setView(inflater.inflate(R.layout.change_password_layout, null))
                    .setCancelable(false)
                    .setNegativeButton("Tidak", null)
                    .setPositiveButton("Ganti") {dialog, _ ->
                        MaterialAlertDialogBuilder(context)
                            .setTitle("Apakah anda yakin?")
                            .setMessage("Apakah anda suda yakin dengan kata sandi yang dimasukkan?")
                            .setNegativeButton("Tidak") { dialog, _ ->
                                dialog.cancel()

                            }
                            .setPositiveButton("Iya") {dialog2, _ ->
                                dialog2.dismiss()
                            }.show()
                    }.show()
            }
            signOutMenu.setOnClickListener {
                auth.signOut()
            }
        }
        return binding.root
    }


}
