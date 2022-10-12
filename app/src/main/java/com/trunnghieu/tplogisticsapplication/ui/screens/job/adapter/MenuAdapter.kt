package com.trunnghieu.tplogisticsapplication.ui.screens.job.adapter

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.data.preferences.AppPrefs
import com.trunnghieu.tplogisticsapplication.data.repository.local.menu.HamburgerMenu
import com.trunnghieu.tplogisticsapplication.data.repository.local.menu.MenuType
import com.trunnghieu.tplogisticsapplication.databinding.ItemMenuBinding
import com.trunnghieu.tplogisticsapplication.ui.base.adapter.BaseBindingListAdapter
import com.trunnghieu.tplogisticsapplication.ui.base.adapter.BaseBindingViewHolder
import com.trunnghieu.tplogisticsapplication.ui.base.adapter.BaseItemClickListener
import com.trunnghieu.tplogisticsapplication.utils.constants.TPLogisticsConst
import com.trunnghieu.tplogisticsapplication.utils.helper.AppPreferences
import com.trunnghieu.tplogisticsapplication.utils.helper.LocaleHelper

class MenuAdapter(
    listener: BaseItemClickListener<HamburgerMenu>,
    private val subMenuListener: BaseItemClickListener<HamburgerMenu.SubMenu>
) : BaseBindingListAdapter<HamburgerMenu>(DiffCallback(), listener) {

    private val loginPermission = AppPreferences.get().getBoolean(AppPrefs.LOGIN.PERMISSION)

    override fun getItemViewType(position: Int) = R.layout.item_menu

    override fun onBindViewHolder(holder: BaseBindingViewHolder<HamburgerMenu>, position: Int) {
        super.onBindViewHolder(holder, position)

        val menuItem = getItem(position)
        (holder.binding as ItemMenuBinding).run {
            // Divider
            showDivider = false

            if (loginPermission) {
                if (menuItem.type_id == MenuType.JOB_HISTORY.typeId) {
                    showDivider = true
                }
            }

            if (menuItem.sub_menu?.isNotEmpty() == true) {
                rdoLangEn.isChecked = LocaleHelper.getLanguageFromLocale() == TPLogisticsConst.AppLanguage.ENGLISH.code
                rdoLangVn.isChecked = LocaleHelper.getLanguageFromLocale() == TPLogisticsConst.AppLanguage.VIETNAMESE.code

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

    private class DiffCallback : DiffUtil.ItemCallback<HamburgerMenu>() {
        override fun areItemsTheSame(oldItem: HamburgerMenu, newItem: HamburgerMenu): Boolean {
            return oldItem.type_id == newItem.type_id
        }

        override fun areContentsTheSame(oldItem: HamburgerMenu, newItem: HamburgerMenu): Boolean {
            return oldItem == newItem
        }
    }
}