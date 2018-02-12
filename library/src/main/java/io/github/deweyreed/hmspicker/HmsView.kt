package io.github.deweyreed.hmspicker

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.LinearLayout

@Suppress("unused")
/**
 * Shown in [R.layout.hms_picker_view].
 * Contains hours, minutes and seconds.
 */
internal class HmsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private lateinit var hoursOnes: ZeroTopPaddingTextView
    private lateinit var minutesTens: ZeroTopPaddingTextView
    private lateinit var minutesOnes: ZeroTopPaddingTextView
    private lateinit var secondsTens: ZeroTopPaddingTextView
    private lateinit var secondsOnes: ZeroTopPaddingTextView
    private val typeFaceAndroidClockMonoThin =
            Typeface.createFromAsset(context.assets, "fonts/AndroidClockMono-Thin.ttf")

    private var textColor: ColorStateList

    init {
        textColor = ContextCompat.getColorStateList(context, R.color.dialog_text_color_holo_dark)
                ?: throw IllegalStateException(
                "Cannot find color state list: R.color.dialog_text_color_holo_dark")
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        hoursOnes = findViewById(R.id.hours_ones)
        minutesTens = findViewById(R.id.minutes_tens)
        minutesOnes = findViewById(R.id.minutes_ones)
        secondsTens = findViewById(R.id.seconds_tens)
        secondsOnes = findViewById(R.id.seconds_ones)

        hoursOnes.updatePaddingForBoldDate()
        minutesTens.updatePaddingForBoldDate()
        minutesOnes.updatePaddingForBoldDate()
        // Set the lowest time unit with thin font (excluding hundredths)
        secondsTens.run {
            typeface = typeFaceAndroidClockMonoThin
            updatePadding()
        }
        secondsOnes.run {
            typeface = typeFaceAndroidClockMonoThin
            updatePadding()
        }
    }

    /**
     * Set a theme and restyle the views. This View will change its text color.
     *
     * @param themeResId the resource ID for its theme
     */
    fun setTheme(themeResId: Int) {
        if (themeResId != -1) {
            val a = context.obtainStyledAttributes(themeResId, R.styleable.HmsPicker)
            textColor = a.getColorStateList(R.styleable.HmsPicker_hms_text_color)
            a.recycle()
        }
        restyleViews()
    }

    private fun restyleViews() {
        hoursOnes.setTextColor(textColor)
        minutesOnes.setTextColor(textColor)
        minutesTens.setTextColor(textColor)
        secondsOnes.setTextColor(textColor)
        secondsTens.setTextColor(textColor)
    }

    fun setTime(hoursOnesDigit: Int,
                minutesTensDigit: Int, minutesOnesDigit: Int, 
                secondsTensDigit: Int, secondsOnesDigit: Int) {
        hoursOnes.text = hoursOnesDigit.toString()
        minutesTens.text = minutesTensDigit.toString()
        minutesOnes.text = minutesOnesDigit.toString()
        secondsTens.text = secondsTensDigit.toString()
        secondsOnes.text = secondsOnesDigit.toString()
    }
}