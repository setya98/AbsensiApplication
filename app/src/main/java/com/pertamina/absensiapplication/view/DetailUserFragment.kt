package com.pertamina.absensiapplication.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pertamina.absensiapplication.databinding.FragmentDetailUserBinding
import com.pertamina.absensiapplication.model.User

class DetailUserFragment : Fragment() {
    private lateinit var binding: FragmentDetailUserBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailUserBinding.inflate(inflater, container, false).apply {
            var user = User()
            arguments?.let {
                user = DetailUserFragmentArgs.fromBundle(it).user
            }
            userArg = user
            changeButton.setOnClickListener {

            }

            deleteButton.setOnClickListener {
                MaterialAlertDialogBuilder(context)
                    .setTitle("Apakah anda yakin?")
                    .setMessage("Apakah anda yakin ingin MENGHAPUS pengguna ini?")
                    .setNegativeButton("Tidak", null)
                    .setPositiveButton("Iya") { _, _ ->
                        //                            permit?.let { it1 ->
//                                it1.status = mapOf(
//                                        "isComplete" to true,
//                                        "isRequest" to false
//                                )
//                            }
                    }
                    .show()
            }
        }
        return binding.root

    }


}
