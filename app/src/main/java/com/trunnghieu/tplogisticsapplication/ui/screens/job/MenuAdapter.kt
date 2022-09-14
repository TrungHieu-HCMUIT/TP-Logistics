package com.trunnghieu.tplogisticsapplication.ui.screens.job

import androidx.recyclerview.widget.DiffUtil
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.data.preferences.AppPrefs
import com.trunnghieu.tplogisticsapplication.data.repository.local.menu.HamburgerMenu
import com.trunnghieu.tplogisticsapplication.data.repository.local.menu.MenuType
import com.trunnghieu.tplogisticsapplication.databinding.ItemMenuBinding
import com.trunnghieu.tplogisticsapplication.ui.base.adapter.BaseBindingListAdapter
import com.trunnghieu.tplogisticsapplication.ui.base.adapter.BaseBindingViewHolder
import com.trunnghieu.tplogisticsapplication.ui.base.adapter.BaseItemClickListener
import com.trunnghieu.tplogisticsapplication.utils.helper.AppPreferences

class MenuAdapter(
    private val isDriverAccount: Boolean,
    listener: BaseItemClickListener<HamburgerMenu>,
    private val subMenuListener: BaseItemClickListener<HamburgerMenu.SubMenu>
) : BaseBindingListAdapter<HamburgerMenu>(DiffCallback(), listener) {

    private val loginPermission = AppPreferences.get().getBoolean(AppPrefs.LOGIN.PERMISSION)

    private class DiffCallback : DiffUtil.ItemCallback<HamburgerMenu>() {
        override fun areItemsTheSame(oldItem: HamburgerMenu, newItem: HamburgerMenu): Boolean {
            return oldItem.type_id == newItem.type_id
        }

        override fun areContentsTheSame(oldItem: HamburgerMenu, newItem: HamburgerMenu): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_menu

    override fun onBindViewHolder(holder: BaseBindingViewHolder<HamburgerMenu>, position: Int) {
        super.onBindViewHolder(holder, position)

        val menuItem = getItem(position)
        (holder.binding as ItemMenuBinding).run {
            // Divider
            showDivider = false
            if (!isDriverAccount) {
                if (loginPermission) {
                    if (menuItem.type_id == MenuType.JOB_HISTORY.typeId) {
                        showDivider = true
                    }
                } else {
                    if (menuItem.type_id == MenuType.SCAN_QR.typeId) {
                        showDivider = true
                    }
                }
            }

            if (menuItem.sub_menu?.isNotEmpty() == true) {
                rdoLangEn.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        subMenuListener.onItemClick(menuItem.sub_menu?.first()!!)
                    }
                }
                rdoLangVn.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        subMenuListener.onItemClick(menuItem.sub_menu?.get(1)!!)
                    }
                }
            }
        }
    }
}