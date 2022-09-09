package com.trunnghieu.tplogisticsapplication.ui.base

interface BaseActions {
    /**
     * Set layout for Activity / Fragment
     */
    fun setLayout(): Int

    /**
     * Binding view
     */
    fun bindView()

    /**
     * Initialize data after screen created
     */
    fun initData()

    /**
     * Initialize actions after screen created
     */
    fun initActions()
}