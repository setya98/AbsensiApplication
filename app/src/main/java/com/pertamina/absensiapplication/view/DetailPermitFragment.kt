package com.pertamina.absensiapplication.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.pertamina.absensiapplication.databinding.FragmentDetailPermitBinding
import com.pertamina.absensiapplication.model.Permit
import com.pertamina.absensiapplication.viewmodel.PermitViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailPermitFragment : Fragment() {
    private lateinit var binding: FragmentDetailPermitBinding
    private val myViewModel: PermitViewModel by viewModel()
    private val auth by inject<FirebaseAuth>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailPermitBinding.inflate(inflater, container, false).apply {
            var permit = Permit()
            arguments?.let {
                permit = DetailPermitFragmentArgs.fromBundle(it).permit
            }
            permit.let {
                val isComplete = it.status.isComplete
                val userId = it.userId
                if (userId.equals(auth.currentUser?.uid)) {
                    buttonConfirmLayout.visibility = View.GONE
                    negotiateButton.visibility = View.GONE
                }
                if (isComplete || permit.senior.equals(auth.currentUser?.uid)) {
                    cancelButton.visibility = View.GONE
                }

                it.type.forEach {
                    when (it) {
                        pnDnCheckbox.text -> pnDnCheckbox.isChecked = true
                        pnLnCheckbox.text -> pnLnCheckbox.isChecked = true
                        sijCheckbox.text -> sijCheckbox.isChecked = true
                        mutasiCheckbox.text -> mutasiCheckbox.isChecked = true
                        cutiCheckbox.text -> cutiCheckbox.isChecked = true
                        tamuCheckbox.text -> tamuCheckbox.isChecked = true
                    }
                }
            }
            rejectButton.setOnClickListener {
                MaterialAlertDialogBuilder(context)
                    .setTitle("Apakah anda yakin?")
                    .setMessage("Apakah anda yakin ingin MENOLAK surat izin ini?")
                    .setNegativeButton("Tidak", null)
                    .setPositiveButton("Iya") { _, _ ->
                        //                            permit.let { it1 ->
//                                it1.status = mapOf(
//                                    "isReject" to true,
//                                    "isComplete" to true,
//                                    "isRequest" to false
//                                )
//                            }
                    }
                    .show()
            }
            acceptButton.setOnClickListener {
                MaterialAlertDialogBuilder(context)
                    .setTitle("Apakah anda yakin?")
                    .setMessage("Apakah anda yakin ingin MENYETUJUI surat izin ini?")
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
            permitArg = permit
        }
        return binding.root
    }
}