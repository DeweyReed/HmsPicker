package io.github.deweyreed.hmspicker

import android.content.DialogInterface
import android.support.annotation.StyleRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View

@Suppress("unused", "MemberVisibilityCanBePrivate")
/**
 * Created on 2018/2/11.
 */

class HmsPickerBuilder(private val fragmentManager: FragmentManager) {
    private var reference: Int = -1
    @StyleRes
    private var styleResId: Int = R.style.HmsPickerThemeLight
    private var hours: Int = 0
    private var minutes: Int = 0
    private var seconds: Int = 0
    private var leftText: String = ""
    private var leftClickListener: HmsPicker.OnLeftRightClickHandler? = null
    private var rightText: String = ""
    private var rightClickListener: HmsPicker.OnLeftRightClickHandler? = null
    private var dismissListener: DialogInterface.OnDismissListener? = null
    private val pickListeners = mutableListOf<HmsPickerDialog.HmsPickHandler>()

    private var targetFragment: Fragment? = null

    fun setReference(r: Int): HmsPickerBuilder = apply { reference = r }
    fun setStyleResId(@StyleRes id: Int): HmsPickerBuilder = apply { styleResId = id }
    fun setTime(h: Int, m: Int, s: Int): HmsPickerBuilder = apply {
        hours = h
        minutes = m
        seconds = s
    }

    fun setTimeInSeconds(totalSeconds: Int): HmsPickerBuilder {
        val hours = totalSeconds / 3600
        val remaining = totalSeconds % 3600
        val minutes = remaining / 60
        val seconds = remaining % 60
        return setTime(hours, minutes, seconds)
    }

    fun setTimeInMilliseconds(timeInMilliseconds: Long): HmsPickerBuilder {
        return setTimeInSeconds((timeInMilliseconds / 1000L).toInt())
    }

    fun setLeftButton(text: String, listener: HmsPicker.OnLeftRightClickHandler? = null): HmsPickerBuilder = apply {
        leftText = text
        leftClickListener = listener
    }

    fun setRightButton(text: String, listener: HmsPicker.OnLeftRightClickHandler? = null): HmsPickerBuilder = apply {
        this.rightText = text
        rightClickListener = listener
    }

    fun setDismissListener(listener: DialogInterface.OnDismissListener): HmsPickerBuilder = apply {
        dismissListener = listener
    }

    fun addPickListener(listener: HmsPickerDialog.HmsPickHandler): HmsPickerBuilder = apply {
        pickListeners.add(listener)
    }

    /**
     * See [Fragment.setTargetFragment]
     */
    fun setTargetFragment(fragment: Fragment): HmsPickerBuilder = apply {
        targetFragment = fragment
    }

    fun show() {
        val dialogTag = "hms_dialog"
        fragmentManager.findFragmentByTag(dialogTag)?.let { fragment ->
            fragmentManager.beginTransaction().apply {
                remove(fragment)
            }.commit()
        }

        HmsPickerDialog().also { dialog ->
            dialog.reference = reference
            dialog.styleResId = styleResId
            dialog.hours = hours
            dialog.minutes = minutes
            dialog.seconds = seconds
            dialog.leftText = leftText
            dialog.leftClickListener = leftClickListener
            dialog.rightText = rightText
            dialog.rightClickListener = rightClickListener
            dialog.dismissListener = dismissListener
            dialog.pickListeners.addAll(pickListeners)
            if (targetFragment != null) dialog.setTargetFragment(targetFragment, 0)
        }.show(fragmentManager, dialogTag)
    }
}