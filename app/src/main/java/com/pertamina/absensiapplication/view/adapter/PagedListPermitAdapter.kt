package com.pertamina.absensiapplication.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pertamina.absensiapplication.R
import com.pertamina.absensiapplication.api.NetworkState
import com.pertamina.absensiapplication.databinding.ItemPermitBinding
import com.pertamina.absensiapplication.model.Permit

class PagedListPermitAdapter(private val callback: OnClickListener) :
    PagedListAdapter<Permit, PagedListPermitAdapter.ViewHolder>(
        diffCallback
    ) {

    private var networkState: NetworkState? = null

    interface OnClickListener {
        fun whenListIsUpdated(size: Int, networkState: NetworkState?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemPermitBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_permit,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    override fun getItemCount(): Int {
        this.callback.whenListIsUpdated(super.getItemCount(), this.networkState)
        return super.getItemCount()
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.SUCCESS

    fun updateNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Permit>() {
            override fun areItemsTheSame(oldItem: Permit, newItem: Permit): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: Permit, newItem: Permit): Boolean = oldItem == newItem
        }
    }

    class ViewHolder(val binding: ItemPermitBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(permit: Permit?) {
            binding.permit = permit
            binding.root.setOnClickListener {
            }
        }
    }
}