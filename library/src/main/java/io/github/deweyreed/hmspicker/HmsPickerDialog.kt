package io.github.deweyreed.hmspicker

import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.StyleRes
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

/**
 * Created on 2018/2/11.
 */

class HmsPickerDialog : DialogFragment() {

    interface HmsPickHandler {
        fun onHmsPick(reference: Int, hours: Int, minutes: Int, seconds: Int)
    }

    var reference: Int = -1
    @StyleRes
    var styleResId: Int = R.style.HmsPickerThemeLight
    var hours: Int = 0
    var minutes: Int = 0
    var seconds: Int = 0
    var leftText: String = ""
    var leftClickListener: HmsPicker.OnLeftRightClickHandler? = null
    var rightText: String = ""
    var rightClickListener: HmsPicker.OnLeftRightClickHandler? = null
    var dismissListener: DialogInterface.OnDismissListener? = null
    var pickListener : HmsPickerDialog.HmsPickHandler? = null

    private lateinit var hmsPicker: HmsPicker
    private var textColor: ColorStateList? = null
    @DrawableRes
    private var dialogBackgroundResId: Int = R.drawable.dialog_full_holo_light

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)

        if (styleResId != -1) {
            activity?.application?.obtainStyledAttributes(styleResId, R.styleable.HmsPicker)?.apply {
                textColor = getColorStateList(R.styleable.HmsPicker_hms_text_color)
                dialogBackgroundResId = getResourceId(R.styleable.HmsPicker_hms_dialog_background,
                        R.drawable.dialog_full_holo_light)
            }?.recycle()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_hms_picker_dialog, container, false)
        hmsPicker = view.findViewById<HmsPicker>(R.id.hms_picker).apply {
            setTheme(styleResId)
            setTime(this@HmsPickerDialog.hours, this@HmsPickerDialog.minutes, this@HmsPickerDialog.seconds)
            setLeftButton(leftText, leftClickListener)
            setRightButton(rightText, rightClickListener)
        }
        view.findViewById<Button>(R.id.button_cancel).apply {
            setTextColor(textColor)
            setOnClickListener { dismiss() }
        }
        view.findViewById<Button>(R.id.button_ok).apply {
            setTextColor(textColor)
            setOnClickListener {
                pickListener?.onHmsPick(reference,
                        hmsPicker.hours, hmsPicker.minutes, hmsPicker.seconds)
                dismiss()
            }
        }

        dialog?.window?.setBackgroundDrawableResource(dialogBackgroundResId)

        return view
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        dismissListener?.onDismiss(dialog)
    }
}