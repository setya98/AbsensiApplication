package com.pertamina.absensiapplication.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.pertamina.absensiapplication.R
import com.pertamina.absensiapplication.api.NetworkState
import com.pertamina.absensiapplication.databinding.FragmentDashboardBinding
import com.pertamina.absensiapplication.model.User
import com.pertamina.absensiapplication.viewmodel.PermitViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashbordFragment : Fragment() {
    private val myViewModel: PermitViewModel by viewModel()
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false).apply {
            myViewModel.getNetwork().observe(viewLifecycleOwner, Observer {
                dashboardRefresh.isRefreshing = it == NetworkState.RUNNING
                dashboardRefresh.setOnRefreshListener {
                    myViewModel.refreshUser()
                }
                when (it) {
                    NetworkState.SUCCESS -> {
                        dashboardRefresh.isEnabled = false
                        rootLayout.visibility = View.VISIBLE
                        emptyListImage.visibility = View.GONE
                        emptyListText.visibility = View.GONE
                        emptyListButton.visibility = View.GONE
                    }
                    NetworkState.FAILED -> {
                        dashboardRefresh.isEnabled = true
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
            user = User()
            myViewModel.getUser().observe(viewLifecycleOwner, Observer {
                user = it ?: User()
                updateUI(it)

            })
        }
        return binding.root
    }

    private fun updateUI(user: User) {
        binding.user = user
        with(binding) {
            //            initList("request", permitProcessList, user)
//            initList("complete", permitFinishList, user)
//            initList("confirm", permitConfirmList, user)
            processCard.setOnClickListener {
                processlistLayout.visibility = View.VISIBLE
                finishlistLayout.visibility = View.GONE
                confirmlistLayout.visibility = View.GONE
            }
            finishCard.setOnClickListener {
                processlistLayout.visibility = View.GONE
                finishlistLayout.visibility = View.VISIBLE
                confirmlistLayout.visibility = View.GONE
            }
            confirmCard.setOnClickListener {
                processlistLayout.visibility = View.GONE
                finishlistLayout.visibility = View.GONE
                confirmlistLayout.visibility = View.VISIBLE
            }
            floatingActionButton.setOnClickListener {
                val action = DashbordFragmentDirections.actionCreatePermit(user)
                findNavController().navigate(action)
            }

        }

    }

//    private fun initList(
//        status: String,
//        recyclerView: RecyclerView,
//        user: User
//    ) {
//        with(recyclerView) {
//            setHasFixedSize(true)
//            layoutManager = LinearLayoutManager(context)
//            viewModel.getPermitListWithStatus(status, user).observe(this@DashbordFragment, Observer {
//                if (it != null) {
//                    if (it.data != null) {
//                        adapter = PermitRecyclerViewAdapter(it.data.asReversed())
//                        when(status) {
//                            "request" -> binding.requestSize = it.data.size
//                            "complete" -> binding.completeSize = it.data.size
//                            "confirm" -> binding.confirmSize = it.data.size
//                        }
//                    } else if (it.exception != null) {
//                        Log.e("DashbordFragment", "Observed unexpected exception", it.exception)
//                    }
//                }
//            })
//        }
//    }
}