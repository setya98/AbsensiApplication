package com.pertamina.absensiapplication.view


import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pertamina.absensiapplication.R
import com.pertamina.absensiapplication.databinding.FragmentCreatePermitBinding
import com.pertamina.absensiapplication.model.Permit
import com.pertamina.absensiapplication.model.StatusPermit
import com.pertamina.absensiapplication.model.User
import com.pertamina.absensiapplication.viewmodel.PermitViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class CreatePermitFragment : Fragment(), AdapterView.OnItemClickListener {
    private lateinit var binding: FragmentCreatePermitBinding
    private val myViewModel: PermitViewModel by viewModel()
    private lateinit var descriptions: Array<String>
    private lateinit var title: String
    private var cal = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePermitBinding.inflate(inflater, container, false).apply {
            val user = arguments?.let { CreatePermitFragmentArgs.fromBundle(it).user }
            descriptions = resources.getStringArray(R.array.description)
            descriptionDropdown.setAdapter(configureAdapter(descriptions))
            costDropdown.setAdapter(configureAdapter(resources.getStringArray(R.array.cost)))
            driveDropdown.setAdapter(configureAdapter(resources.getStringArray(R.array.drive)))
            descriptionDropdown.onItemClickListener = this@CreatePermitFragment
            val dateToListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                updateDateInView("to", year, monthOfYear, dayOfMonth)
            }
            val dateBackListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                updateDateInView("back", year, monthOfYear, dayOfMonth)
            }
            val dateInListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                updateDateInView("in", year, monthOfYear, dayOfMonth)
            }
            dateToInput.setOnClickListener { showCalendar(dateToListener) }
            dateBackInput.setOnClickListener { showCalendar(dateBackListener) }
            dateInInput.setOnClickListener { showCalendar(dateInListener) }

            submitButton.setOnClickListener {
                MaterialAlertDialogBuilder(context)
                    .setTitle("Apakah anda yakin?")
                    .setMessage("Apakah anda yakin ingin MENGAJUKAN surat izin ini?")
                    .setNegativeButton("Tidak", null)
                    .setPositiveButton("Iya") { _, _ ->
                        user?.let { it1 -> submitPermit(it1) }
                        val action = CreatePermitFragmentDirections.actionShowDashboard()
                        findNavController().navigate(action)
                    }
                    .show()
            }
        }
        return binding.root
    }

    private fun configureAdapter(stringArray: Array<String>) =
        ArrayAdapter(
            requireContext(),
            R.layout.dropdown_item,
            stringArray
        )

    private fun submitPermit(user: User) {
        with(binding) {
            if (anotherInputlayout.visibility == View.VISIBLE) {
                title = anotherInput.text.toString()
            }
            val cost = costDropdown.text.toString()
            val drive = driveDropdown.text.toString()
            val from = fromInput.text.toString()
            val to = toInput.text.toString()
            val duration = durasiInput.text.toString().toLong()
            val dateTo = dateToInput.text.toString()
            val dateBack = dateBackInput.text.toString()
            val dateIn = dateInInput.text.toString()

            val typePermit = ArrayList<String>()
            if (pnDnCheckbox.isChecked) {
                typePermit.add(pnDnCheckbox.text.toString())
            }
            if (pnLnCheckbox.isChecked) {
                typePermit.add(pnLnCheckbox.text.toString())
            }
            if (sijCheckbox.isChecked) {
                typePermit.add(sijCheckbox.text.toString())
            }
            if (mutasiCheckbox.isChecked) {
                typePermit.add(mutasiCheckbox.text.toString())
            }
            if (cutiCheckbox.isChecked) {
                typePermit.add(cutiCheckbox.text.toString())
            }
            if (tamuCheckbox.isChecked) {
                typePermit.add(tamuCheckbox.text.toString())
            }

            val status = StatusPermit(
                false,
                false,
                false,
                false,
                true
            )
            // Get User First

            myViewModel.createPermit(
                Permit(
                    title,
                    user.employeeNumber,
                    from,
                    to,
                    duration,
                    dateTo,
                    dateBack,
                    dateIn,
                    cost,
                    drive,
                    status,
                    user.userId,
                    user.senior,
                    user.operationHead,
                    user.profileImage,
                    user.name,
                    typePermit
                )
            )
        }
    }

    private fun updateDateInView(type: String, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, monthOfYear)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        when (type) {
            "to" -> binding.dateToInput.setText(sdf.format(cal.time))
            "back" -> binding.dateBackInput.setText(sdf.format(cal.time))
            "in" -> binding.dateInInput.setText(sdf.format(cal.time))
        }
    }

    private fun showCalendar(dateListener: DatePickerDialog.OnDateSetListener) {
        DatePickerDialog(
            requireContext(),
            dateListener,
            // set DatePickerDialog to point to today's date when it loads up
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (descriptions[position].equals(resources.getString(R.string.keterangan_lainnya), false)) {
            binding.anotherInputlayout.visibility = View.VISIBLE
        } else {
            binding.anotherInputlayout.visibility = View.GONE
            title = descriptions[position]
        }
    }
}
