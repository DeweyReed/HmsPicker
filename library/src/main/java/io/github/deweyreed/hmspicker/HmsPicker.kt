package io.github.deweyreed.hmspicker

import android.content.Context
import android.content.res.ColorStateList
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.StyleRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView


@Suppress("unused", "MemberVisibilityCanBePrivate")
class HmsPicker @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener, View.OnLongClickListener {

    companion object {
        private const val SIGN_POSITIVE = 0
        private const val SIGN_NEGATIVE = 1
    }

    interface OnLeftRightClickHandler {
        fun onClick(view: HmsPicker)
    }

    private val inputSize = 5
    private val numberButtons = arrayOfNulls<Button>(10)
    private var userInput = IntArray(inputSize)
    private var inputPointer = -1

    private val enteredHms: HmsView
    private val hoursLabel: TextView
    private val minutesLabel: TextView
    private val secondsLabel: TextView
    private val deleteButton: ImageButton
    private val divider: View

    // These two should be val but IDE complains
    private lateinit var leftButton: Button
    private lateinit var rightButton: Button

    private var setButton: Button? = null

    private var textColor: ColorStateList? = null
    private var keyBackgroundResId: Int = 0
    private var deleteBackgroundResId: Int = 0
    private var deleteIconResId: Int = 0
    private var dividerColor: Int = 0
    @StyleRes
    private var theme = -1

    /**
     * @return the hours as currently inputted by the user.
     */
    val hours: Int get() = userInput[4]

    /**
     * @return the minutes as currently inputted by the user.
     */
    val minutes: Int get() = userInput[3] * 10 + userInput[2]

    /**
     * @return the seconds as currently inputted by the user.
     */
    val seconds: Int get() = userInput[1] * 10 + userInput[0]

    /**
     * @return the time in seconds
     */
    val time: Int
        get() = userInput[4] * 3600 + userInput[3] * 600 +
                userInput[2] * 60 + userInput[1] * 10 + userInput[0]

    init {
        LayoutInflater.from(context).inflate(R.layout.hms_picker_view, this)

        // Init defaults
        val ta = context.obtainStyledAttributes(attrs, R.styleable.HmsPicker)
        textColor = ta.getColorStateList(R.styleable.HmsPicker_hms_text_color)
                ?: ContextCompat.getColorStateList(context,
                R.color.dialog_text_color_holo_dark)
                ?: throw IllegalStateException(
                "Cannot find color state list: R.color.dialog_text_color_holo_dark")
        keyBackgroundResId = ta.getResourceId(R.styleable.HmsPicker_hms_key_background,
                R.drawable.key_background_dark)
        deleteBackgroundResId = ta.getResourceId(R.styleable.HmsPicker_hms_delete_background,
                R.drawable.button_background_dark)
        deleteIconResId = ta.getResourceId(R.styleable.HmsPicker_hms_delete_icon,
                R.drawable.ic_backspace_dark)
        dividerColor = ta.getColor(R.styleable.HmsPicker_hms_divider_color,
                ContextCompat.getColor(context, R.color.default_divider_color_dark))
        val leftText = ta.getString(R.styleable.HmsPicker_hms_left_text) ?: ""
        val rightText = ta.getString(R.styleable.HmsPicker_hms_right_text) ?: ""
        ta.recycle()

        enteredHms = findViewById(R.id.hms_text)
        hoursLabel = findViewById(R.id.hours_label)
        minutesLabel = findViewById(R.id.minutes_label)
        secondsLabel = findViewById(R.id.seconds_label)
        deleteButton = findViewById<ImageButton>(R.id.delete).also { btn ->
            btn.setOnClickListener(this@HmsPicker)
            btn.setOnLongClickListener(this@HmsPicker)
        }
        divider = findViewById(R.id.divider)

        findViewById<View>(R.id.first).run {
            numberButtons[1] = findViewById(R.id.key_left)
            numberButtons[2] = findViewById(R.id.key_middle)
            numberButtons[3] = findViewById(R.id.key_right)
        }

        findViewById<View>(R.id.second).run {
            numberButtons[4] = findViewById(R.id.key_left)
            numberButtons[5] = findViewById(R.id.key_middle)
            numberButtons[6] = findViewById(R.id.key_right)
        }

        findViewById<View>(R.id.third).run {
            numberButtons[7] = findViewById(R.id.key_left)
            numberButtons[8] = findViewById(R.id.key_middle)
            numberButtons[9] = findViewById(R.id.key_right)
        }

        findViewById<View>(R.id.fourth).run {
            leftButton = findViewById<Button>(R.id.key_left).apply { text = leftText }
            numberButtons[0] = findViewById(R.id.key_middle)
            rightButton = findViewById<Button>(R.id.key_right).apply { text = rightText }
        }

        for (i in 0..9) {
            numberButtons[i]?.run {
                setOnClickListener(this@HmsPicker)
                text = i.toString()
                setTag(R.id.numbers_key, i)
            }
        }
        restyleViews()
        updateKeypad()
    }

    fun setTheme(@StyleRes themeResId: Int) {
        theme = themeResId
        if (theme != -1) {
            context.obtainStyledAttributes(themeResId, R.styleable.HmsPicker).apply {
                textColor = getColorStateList(R.styleable.HmsPicker_hms_text_color) ?: textColor
                keyBackgroundResId = getResourceId(R.styleable.HmsPicker_hms_key_background,
                        keyBackgroundResId)
                deleteBackgroundResId = getResourceId(R.styleable.HmsPicker_hms_delete_background,
                        deleteBackgroundResId)
                deleteIconResId = getResourceId(R.styleable.HmsPicker_hms_delete_icon,
                        deleteIconResId)
                dividerColor = getColor(R.styleable.HmsPicker_hms_divider_color, dividerColor)
            }.recycle()
        }
        restyleViews()
    }

    private fun restyleViews() {
        numberButtons.filter { it != null }.forEach {
            it?.setTextColor(textColor)
            it?.setBackgroundResource(keyBackgroundResId)
        }
        enteredHms.setTheme(theme)
        arrayOf(hoursLabel, minutesLabel, secondsLabel).forEach {
            it.setTextColor(textColor)
            it.setBackgroundResource(keyBackgroundResId)
        }
        deleteButton.run {
            setBackgroundResource(deleteBackgroundResId)
            setImageResource(deleteIconResId)
        }
        divider.setBackgroundColor(dividerColor)
        arrayOf(leftButton, rightButton).forEach {
            it.setTextColor(textColor)
            it.setBackgroundResource(keyBackgroundResId)
        }
    }

    override fun onClick(view: View) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        doOnClick(view)
    }

    private fun doOnClick(v: View) {
        val value = v.getTag(R.id.numbers_key) as? Int
        // A number was pressed
        if (value != null) {
            addClickedNumber(value)
        } else if (v === deleteButton) {
            if (inputPointer >= 0) {
                for (i in 0 until inputPointer) {
                    userInput[i] = userInput[i + 1]
                }
                userInput[inputPointer] = 0
                --inputPointer
            }
        }
        updateKeypad()
    }

    override fun onLongClick(v: View): Boolean {
        v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        return if (v === deleteButton) {
            deleteButton.isPressed = false
            reset()
            updateKeypad()
            true
        } else {
            false
        }
    }

    /**
     * Reset all inputs and the hours:minutes:seconds.
     */
    fun reset() {
        for (i in 0 until inputSize) {
            userInput[i] = 0
        }
        inputPointer = -1
        updateHms()
    }

    private fun updateKeypad() {
        updateHms()
        refreshSetButtonEnable()
        updateDeleteButton()
    }

    /**
     * Update the time displayed in the picker:
     * Put "-" in digits that was not entered by passing -1
     * Hide digit by passing -2 (for highest hours digit only);
     */
    private fun updateHms() {
        enteredHms.setTime(userInput[4], userInput[3], userInput[2], userInput[1], userInput[0])
    }

    private fun addClickedNumber(value: Int) {
        if (inputPointer < inputSize - 1 && !(inputPointer == -1 && value == 0)) {
            for (i in inputPointer downTo 0) {
                userInput[i + 1] = userInput[i]
            }
            ++inputPointer
            userInput[0] = value
        }
    }

    /**
     * Enable/disable the "Set" button
     */
    private fun refreshSetButtonEnable() {
        if (setButton == null) return
        if (inputPointer == -1) {
            setButton?.isEnabled = false
        } else {
            setButton?.isEnabled = inputPointer >= 0
        }
    }

    private fun updateDeleteButton() {
        deleteButton.isEnabled = inputPointer != -1
    }

    /**
     * Expose the set button to allow communication with the parent Fragment.
     */
    fun setSetButton(b: Button) {
        setButton = b
        refreshSetButtonEnable()
    }

    fun setLeftButton(text: String, clickListener: OnLeftRightClickHandler? = null) {
        setButton(leftButton, text, clickListener)
    }

    fun setRightButton(text: String, clickListener: OnLeftRightClickHandler? = null) {
        setButton(rightButton, text, clickListener)
    }

    private fun setButton(btn: Button, text: String, clickListener: OnLeftRightClickHandler? = null) {
        if (text.isNotEmpty() && clickListener != null) {
            btn.run {
                isEnabled = true
                setText(text)
                setOnClickListener { clickListener.onClick(this@HmsPicker) }
            }
        }
    }

    fun setTime(hours: Int, minutes: Int, seconds: Int) {
        userInput[4] = hours
        userInput[3] = minutes / 10
        userInput[2] = minutes % 10
        userInput[1] = seconds / 10
        userInput[0] = seconds % 10

        for (i in 4 downTo 0) {
            if (userInput[i] > 0) {
                inputPointer = i
                break
            }
        }

        updateKeypad()
    }

    override fun onSaveInstanceState(): Parcelable? =
            SavedState(super.onSaveInstanceState()).also { state ->
                state.input = userInput
                state.inputPointer = inputPointer
                state.theme = theme
            }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)

            inputPointer = state.inputPointer
            val input = state.input
            if (input == null) {
                userInput = IntArray(inputSize)
                inputPointer = -1
            } else {
                userInput = input
            }
            val newTheme = state.theme
            if (newTheme != -1) {
                setTheme(newTheme)
            }
            updateKeypad()
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    private class SavedState : View.BaseSavedState {

        var input: IntArray? = null
        var inputPointer: Int = 0
        var theme: Int = -1

        constructor(superState: Parcelable) : super(superState)

        private constructor(i: Parcel) : super(i) {
            input = i.createIntArray()
            inputPointer = i.readInt()
            theme = i.readInt()
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeInt(inputPointer)
            dest.writeIntArray(input)
            dest.writeInt(theme)
        }

        companion object {
            @JvmStatic
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState = SavedState(`in`)

                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
            }
        }
    }
}