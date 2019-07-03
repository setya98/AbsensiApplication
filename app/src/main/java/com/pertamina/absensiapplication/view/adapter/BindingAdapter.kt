package com.pertamina.absensiapplication.view.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.pertamina.absensiapplication.R
import com.pertamina.absensiapplication.model.StatusPermit
import com.pertamina.absensiapplication.model.User

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    Glide.with(view.getContext()).load(url).into(view);
}

@BindingAdapter("setVisibility")
fun setVisibility(view: MaterialCardView, user: User) {
    if (user.senior.isNotEmpty() && user.operationHead.isNotEmpty()) {
        view.visibility = View.GONE
    } else {
        view.visibility = View.VISIBLE
    }
}

@BindingAdapter("setCircleIcon")
fun setCircleIcon(view: ImageView, status: StatusPermit) {
    val isRequest = status.isRequest
    val isComplete = status.isComplete
    if (isComplete) {
        view.setBackgroundResource(R.drawable.ic_circle_green)
    } else if (isRequest) {
        view.setBackgroundResource(R.drawable.ic_circle_yellow)
    }
}

@BindingAdapter("setStatus")
fun setStatus(view: TextView, status: StatusPermit) {
    val isRequest = status.isRequest
    val isComplete = status.isComplete
    if (isComplete) {
        view.text = "complete"
    } else if (isRequest) {
        view.text = "request"
    }
}

@BindingAdapter("setTitle")
fun setTitle(view: TextView, title: String) {
    view.text = if (title.length > 42) title.substring(0, 25)+"..." else title
}
