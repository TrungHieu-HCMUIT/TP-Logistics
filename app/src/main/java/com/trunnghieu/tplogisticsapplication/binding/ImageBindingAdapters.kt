package com.trunnghieu.tplogisticsapplication.binding

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.trunnghieu.tplogisticsapplication.R

@BindingAdapter("imageUrl")
fun loadImageFromUrl(imageView: ImageView, imageUrl: String? = null) {
    val context = imageView.context
    Glide.with(context)
        .load(imageUrl)
        .placeholder(startLoadingProgress(context))
        .error(R.mipmap.ic_launcher_round)
        .centerCrop()
        .signature(ObjectKey(System.currentTimeMillis().toString()))
        .into(imageView)
}

@BindingAdapter("imageUrlWithoutCrop")
fun loadImageWithoutCropFromUrl(imageView: ImageView, imageUrl: String? = null) {
    if (imageUrl == null) return
    val context = imageView.context
    Glide.with(context)
        .load(imageUrl)
        .placeholder(startLoadingProgress(context))
        .error(R.mipmap.ic_launcher_round)
        .signature(ObjectKey(System.currentTimeMillis().toString()))
        .into(imageView)
}

@BindingAdapter("imageBitmap")
fun loadImageFromBitmap(imageView: ImageView, imageBitmap: Bitmap? = null) {
    imageBitmap ?: return
    imageView.setImageBitmap(imageBitmap)
}

private fun startLoadingProgress(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 4f
        centerRadius = 30f
        setColorSchemeColors(ContextCompat.getColor(context, R.color.secondary))
        start()
    }
}