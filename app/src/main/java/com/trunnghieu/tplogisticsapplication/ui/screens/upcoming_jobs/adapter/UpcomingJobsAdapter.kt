package com.trunnghieu.tplogisticsapplication.ui.screens.upcoming_jobs.adapter

import androidx.recyclerview.widget.DiffUtil
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.databinding.ItemUpcomingJobBinding
import com.trunnghieu.tplogisticsapplication.ui.base.adapter.BaseBindingListAdapter
import com.trunnghieu.tplogisticsapplication.ui.base.adapter.BaseBindingViewHolder

class UpcomingJobsAdapter(private val listener: OnJobSelectedListener) :
    BaseBindingListAdapter<Job>(DiffCallback()) {

    private var jobSelectionAllowed = false
    private var lastSelectedItemPosition = -1

    private class DiffCallback : DiffUtil.ItemCallback<Job>() {
        override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean {
            return oldItem.bookingNo == newItem.bookingNo
        }

        override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_upcoming_job

    override fun onBindViewHolder(holder: BaseBindingViewHolder<Job>, position: Int) {
        val item = getItem(position)
        val binding = (holder.binding as ItemUpcomingJobBinding).apply {
            this.item = item
            this.jobSelectionAllowed = this@UpcomingJobsAdapter.jobSelectionAllowed
            this.selected = false
        }

        if (!jobSelectionAllowed) {
            if (position == 0) {
                binding.selected = true
                listener.selectedJob(item)
            }
        } else {
            binding.run {
                selected = lastSelectedItemPosition == position
                tvPickupLocation.text = item.pickUpLocation
                tvDeliveryLocation.text = item.deliveryLocation
                tvCustomerName.text = item.customerName
                tvProductType.text = item.product
                root.setOnClickListener {
                    if (lastSelectedItemPosition == holder.absoluteAdapterPosition) {
                        lastSelectedItemPosition = -1
                        listener.selectedJob(null)
                    } else {
                        lastSelectedItemPosition = holder.absoluteAdapterPosition
                        listener.selectedJob(getItem(position))
                    }
                    notifyItemRangeChanged(0, itemCount)
                }
            }
        }
    }

    interface OnJobSelectedListener {
        fun selectedJob(job: Job?)
    }

    fun setJobSelectionAllowed(jobSelectionAllowed: Boolean) {
        this.jobSelectionAllowed = jobSelectionAllowed
        notifyItemRangeChanged(0, itemCount)
    }

    fun resetLastCheckedItemPosition() {
        this.lastSelectedItemPosition = -1
        notifyItemRangeChanged(0, itemCount)
    }
}
