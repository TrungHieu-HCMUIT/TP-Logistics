package com.trunnghieu.tplogisticsapplication.ui.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseBindingListAdapter<T>(
    diffCallback: DiffUtil.ItemCallback<T>,
    private val itemClickListener: BaseItemClickListener<T>? = null
) : ListAdapter<T, BaseBindingViewHolder<T>>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<T> {
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType, parent, false
        ).also {
            return BaseBindingViewHolder(it, itemClickListener)
        }
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<T>, position: Int) {
        holder.bindItem(getItem(position))
    }
}