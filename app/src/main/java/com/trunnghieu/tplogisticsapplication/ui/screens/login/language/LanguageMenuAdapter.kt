package com.trunnghieu.tplogisticsapplication.ui.screens.login.language

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.databinding.ItemLanguageMenuBinding
import com.trunnghieu.tplogisticsapplication.ui.base.adapter.BaseBindingListAdapter
import com.trunnghieu.tplogisticsapplication.ui.base.adapter.BaseBindingViewHolder
import com.trunnghieu.tplogisticsapplication.ui.base.adapter.BaseItemClickListener
import com.trunnghieu.tplogisticsapplication.utils.constants.TPLogisticsConst

class LanguageMenuAdapter(
    context: Context,
    private val listener: BaseItemClickListener<LanguageMenu>
) : BaseBindingListAdapter<LanguageMenu>(DiffCallback()) {

    init {
        val languages = listOf(
            LanguageMenu(
                TPLogisticsConst.AppLanguage.ENGLISH,
                context.getString(R.string.menu_lang_en),
                ContextCompat.getDrawable(context, R.drawable.img_lang_en)!!
            ),
            LanguageMenu(
                TPLogisticsConst.AppLanguage.VIETNAMESE,
                context.getString(R.string.menu_lang_vn),
                ContextCompat.getDrawable(context, R.drawable.img_lang_vn)!!
            ),
        )
        submitList(languages)
    }

    private var lastSelectedItemPosition = -1

    private class DiffCallback : DiffUtil.ItemCallback<LanguageMenu>() {
        override fun areItemsTheSame(oldItem: LanguageMenu, newItem: LanguageMenu): Boolean {
            return oldItem.language == newItem.language
        }

        override fun areContentsTheSame(oldItem: LanguageMenu, newItem: LanguageMenu): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_language_menu

    override fun onBindViewHolder(holder: BaseBindingViewHolder<LanguageMenu>, position: Int) {
        val item = getItem(position)
        val binding = holder.binding as ItemLanguageMenuBinding
        binding.apply {
            this.item = item
            selected = lastSelectedItemPosition == holder.absoluteAdapterPosition
            root.setOnClickListener {
                lastSelectedItemPosition = holder.absoluteAdapterPosition
                notifyItemRangeChanged(0, itemCount)
                listener.onItemClick(item)
            }
        }
    }

    /**
     * Set selected item at position
     */
    fun setSelectedItem(selectedLanguage: String) {
        val selectedToPosition = currentList.indexOf(
            currentList.find { it.languageEnum.code == selectedLanguage } ?: 0
        )
        lastSelectedItemPosition = selectedToPosition
        notifyItemRangeChanged(0, itemCount)
    }
}

data class LanguageMenu(
    val languageEnum: TPLogisticsConst.AppLanguage,
    val language: String,
    val languageIcon: Drawable
)