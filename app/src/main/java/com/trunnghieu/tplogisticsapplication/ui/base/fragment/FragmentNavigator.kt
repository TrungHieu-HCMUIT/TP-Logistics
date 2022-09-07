package com.trunnghieu.tplogisticsapplication.ui.base.fragment

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import java.util.*

class FragmentNavigator(
    private val mFragmentManager: FragmentManager,
    @field:IdRes @param:IdRes private val mDefaultContainer: Int
) {

    private var onStackChanged: OnFragmentStackChanged? = null
    private var mRootFragmentTag: String? = null
    private var currentState: Lifecycle.State? = null

    /**
     * This constructor should be only called once per
     */
    init {
        mFragmentManager.addOnBackStackChangedListener {
            activeFragment?.let { fragment ->
                onStackChanged?.onChanged(fragment)
            }
        }
    }

    fun setOnStackChanged(onStackChanged: OnFragmentStackChanged) {
        this.onStackChanged = onStackChanged
    }

    /**
     * @return the current active fragment. If no fragment is active it return null.
     */
    val activeFragment: Fragment?
        get() {
            val tag: String? = if (mFragmentManager.backStackEntryCount == 0) {
                mRootFragmentTag
            } else {
                mFragmentManager
                    .getBackStackEntryAt(mFragmentManager.backStackEntryCount - 1).name
            }
            return mFragmentManager.findFragmentByTag(tag)
        }

    /**
     * Pushes the fragment, and add it to the history (BackStack) if you have set a default animation
     * it will be added to the transaction.
     *
     * @param fragment The fragment which will be added
     */
    fun goTo(fragment: Fragment) {
        currentState = fragment.lifecycle.currentState
        mFragmentManager.beginTransaction()
            .apply {
                addToBackStack(getTag(fragment))
                add(mDefaultContainer, fragment, getTag(fragment))
            }
            .also {
                if (currentState!!.isAtLeast(Lifecycle.State.CREATED)) {
                    it.setMaxLifecycle(fragment, currentState!!)
                }
                it.commit()
            }
        mFragmentManager.executePendingTransactions()
    }

    /**
     * This is just a helper method which returns the simple name of the fragment.
     *
     * @param fragment That get added to the history (BackStack)
     * @return The simple name of the given fragment
     */
    private fun getTag(fragment: Fragment?): String {
        return fragment!!.javaClass.simpleName
    }
    /**
     * Get root fragment
     *
     * @return
     */
    /**
     * Set the new root fragment. If there is any entry in the history (BackStack) it will be
     * deleted.
     *
     * @param rootFragment The new root fragment
     */
    var rootFragment: Fragment?
        get() = mFragmentManager.findFragmentByTag(mRootFragmentTag)
        set(rootFragment) {
            if (rootFragment == null) return
            if (size > 0) {
                clearHistory()
            }
            mRootFragmentTag = getTag(rootFragment)
            replaceFragment(rootFragment)
        }

    /**
     * Replace the current fragment with the given one, without to add it to backstack. So when the
     * users navigates away from the given fragment it will not appaer in the history.
     *
     * @param fragment The new fragment
     */
    private fun replaceFragment(fragment: Fragment) {
        currentState = fragment.lifecycle.currentState
        mFragmentManager.beginTransaction()
            .apply {
                replace(mDefaultContainer, fragment, getTag(fragment))
            }
            .also {
                if (currentState!!.isAtLeast(Lifecycle.State.CREATED)) {
                    it.setMaxLifecycle(fragment, currentState!!)
                }
                it.commitAllowingStateLoss()
            }
        mFragmentManager.executePendingTransactions()
    }

    /**
     * Goes one entry back in the history
     */
    fun goOneBack() {
        mFragmentManager.popBackStackImmediate()
    }

    /**
     * Goes one entry back in the history
     */
    fun goOneBackTo(tagFragment: String) {
        var i = size - 1
        while (size >= 1) {
            if (Objects.requireNonNull(mFragmentManager.getBackStackEntryAt(i).name) != tagFragment) {
                goOneBack()
                i--
            } else {
                return
            }
        }
    }

    /**
     * @return The current size of the history (BackStack)
     */
    val size: Int
        get() = mFragmentManager.backStackEntryCount

    /**
     * Goes the whole history back until to the first fragment in the history. It would be the same if
     * the user would click so many times the back button until he reach the first fragment of the
     * app.
     */
    fun goToRoot() {
        while (size >= 1) {
            goOneBack()
        }
    }

    fun goToRoot(listener: RootFragmentListener) {
        while (size >= 1) {
            if (size <= 1) {
                listener.onAlreadyAtRootFragment(rootFragment)
                break
            }
            goOneBack()
        }
    }

    /**
     * Clears the whole history so it will no BackStack entry there any more.
     */
    private fun clearHistory() {
        while (mFragmentManager.popBackStackImmediate()) {
        }
    }

    interface OnFragmentStackChanged {
        fun onChanged(fragment: Fragment)
    }

    interface RootFragmentListener {
        fun onAlreadyAtRootFragment(rootFragment: Fragment?)
    }
}