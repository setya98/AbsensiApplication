package com.pertamina.absensiapplication.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pertamina.absensiapplication.R
import com.pertamina.absensiapplication.api.NetworkState
import com.pertamina.absensiapplication.databinding.FragmentHistoryBinding
import com.pertamina.absensiapplication.util.ItemClickSupport
import com.pertamina.absensiapplication.view.adapter.PagedListPermitAdapter
import com.pertamina.absensiapplication.viewmodel.PermitViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment(), PagedListPermitAdapter.OnClickListener{
    private val myViewModel: PermitViewModel by viewModel()

    private lateinit var adapter: PagedListPermitAdapter
    private lateinit var binding: FragmentHistoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false).apply {
        adapter = PagedListPermitAdapter(this@HistoryFragment)
            historyList.layoutManager = LinearLayoutManager(requireContext())
            historyList.adapter = adapter
            myViewModel.getNetworkHistory()?.observe(viewLifecycleOwner, Observer {
                adapter.updateNetworkState(it)
                permitRefresh.isRefreshing = it == NetworkState.RUNNING
                permitRefresh.setOnRefreshListener {
                    myViewModel.refresh()
                }
            })

            myViewModel.permits.observe(viewLifecycleOwner, Observer {
                adapter.submitList(it)
                ItemClickSupport.addTo(historyList).setOnItemClickListener(object : ItemClickSupport.OnItemClickListener{
                    override fun onItemClicked(recyclerView: RecyclerView, position: Int, view: View) {
                        val action = it[position]?.let { it1 -> HistoryFragmentDirections.actionShowDetailPermit(it1) }
                        action?.let { it1 -> findNavController().navigate(it1) }
                    }
                })
            })
        }
        return binding.root
    }

    override fun whenListIsUpdated(size: Int, networkState: NetworkState?) {
        updateUIWhenEmptyList(size, networkState)
    }

    private fun updateUIWhenEmptyList(size: Int, networkState: NetworkState?) {
        with(binding) {
            emptyListImage.visibility = View.GONE
            emptyListButton.visibility = View.GONE
            emptyListText.visibility = View.GONE
            if (size == 0) {
                when (networkState) {
                    NetworkState.SUCCESS -> {
                        com.bumptech.glide.Glide.with(requireContext()).load(R.drawable.ic_directions_run_black_24dp)
                            .into(emptyListImage)
                        emptyListText.text = getString(R.string.no_result_found)
                        emptyListImage.visibility = View.VISIBLE
                        emptyListText.visibility = View.VISIBLE
                        emptyListButton.visibility = View.VISIBLE
                    }
                    NetworkState.FAILED -> {
                        com.bumptech.glide.Glide.with(requireContext()).load(R.drawable.ic_healing_black_24dp)
                            .into(emptyListImage)
                        emptyListText.text = getString(R.string.technical_error)
                        emptyListImage.visibility = View.VISIBLE
                        emptyListText.visibility = View.VISIBLE
                        emptyListButton.visibility = View.VISIBLE
                        emptyListButton.setOnClickListener {
                            myViewModel.refreshFailedRequest()
                        }
                    }
                }
            }
        }
    }
}