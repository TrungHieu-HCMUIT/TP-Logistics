package com.trunnghieu.tplogisticsapplication.ui.screens.cso

import androidx.recyclerview.widget.DiffUtil
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.data.repository.remote.account.cso.CsoPhoneNumber
import com.trunnghieu.tplogisticsapplication.databinding.ItemCsoPhoneBinding
import com.trunnghieu.tplogisticsapplication.ui.base.adapter.BaseBindingListAdapter
import com.trunnghieu.tplogisticsapplication.ui.base.adapter.BaseBindingViewHolder
import com.trunnghieu.tplogisticsapplication.ui.base.adapter.BaseItemClickListener

class CsoPhoneAdapter(listener: BaseItemClickListener<CsoPhoneNumber>) :
    BaseBindingListAdapter<CsoPhoneNumber>(DiffCallback(), listener) {

    private class DiffCallback : DiffUtil.ItemCallback<CsoPhoneNumber>() {
        override fun areItemsTheSame(oldItem: CsoPhoneNumber, newItem: CsoPhoneNumber): Boolean {
            return oldItem.phoneNo == newItem.phoneNo
        }

        override fun areContentsTheSame(oldItem: CsoPhoneNumber, newItem: CsoPhoneNumber): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_cso_phone

    override fun onBindViewHolder(holder: BaseBindingViewHolder<CsoPhoneNumber>, position: Int) {
        super.onBindViewHolder(holder, position)

        (holder.binding as ItemCsoPhoneBinding).firstItem = position == 0
    }
}