package com.pertamina.absensiapplication.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import com.pertamina.absensiapplication.R

class ItemClickSupport(private val recyclerView: RecyclerView) {
    private lateinit var onItemClickListener: OnItemClickListener
    private var onItemLongClickListener: OnItemLongClickListener? = null

    private val onClickListener = View.OnClickListener {
        onItemClickListener.let { it1 ->
            val holder = recyclerView.getChildViewHolder(it)
            it1.onItemClicked(recyclerView, holder.adapterPosition, it)
        }
    }

    private val onLongClickListener = View.OnLongClickListener {
        if (onItemLongClickListener != null) {
            val holder = recyclerView.getChildViewHolder(it)
            return@OnLongClickListener onItemLongClickListener!!.onItemLongClicked(
                recyclerView,
                holder.adapterPosition,
                it
            )!!
        }
        return@OnLongClickListener false
    }

    companion object {
        fun addTo(recyclerView: RecyclerView): ItemClickSupport {
            val support =
                recyclerView.getTag(R.id.item_click_support) as ItemClickSupport? ?: ItemClickSupport(
                    recyclerView
                )
            return support
        }

        fun removeFrom(recyclerView: RecyclerView): ItemClickSupport {
            val support = recyclerView.getTag(R.id.item_click_support) as ItemClickSupport
            support.let {
                support.detach(recyclerView)
            }
            return support
        }
    }

    private val attachListener =
        object : OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
            }

            override fun onChildViewAttachedToWindow(view: View) {
                onItemClickListener.let {
                    view.setOnClickListener(onClickListener)
                }
                onItemLongClickListener.let {
                    view.setOnLongClickListener(onLongClickListener)
                }
            }

        }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        onItemLongClickListener = listener
    }

    private fun detach(recyclerView: RecyclerView) {
        recyclerView.removeOnChildAttachStateChangeListener(attachListener)
        recyclerView.setTag(R.id.item_click_support, null)
    }

    interface OnItemClickListener {
        fun onItemClicked(recyclerView: RecyclerView, position: Int, view: View)
    }

    interface OnItemLongClickListener {
        fun onItemLongClicked(recyclerView: RecyclerView, position: Int, view: View): Boolean?
    }

    init {
        this.recyclerView.setTag(R.id.item_click_support)
        this.recyclerView.addOnChildAttachStateChangeListener(attachListener)
    }
}