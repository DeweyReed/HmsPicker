package io.github.deweyreed.hmspicker.sample

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.github.deweyreed.hmspicker.HmsPicker
import io.github.deweyreed.hmspicker.HmsPickerBuilder
import io.github.deweyreed.hmspicker.HmsPickerDialog
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity(), HmsPickerDialog.HmsPickHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLight.setOnClickListener {
            HmsPickerBuilder(supportFragmentManager)
                    // light is default
//                    .setStyleResId(R.style.HmsPickerThemeLight)
                    .show()
        }

        btnNight.setOnClickListener {
            HmsPickerBuilder(supportFragmentManager)
                    .setStyleResId(R.style.HmsPickerThemeDark)
                    .show()
        }

        btnCustom.setOnClickListener {
            HmsPickerBuilder(supportFragmentManager)
                    .setStyleResId(R.style.CustomHmsPickerTheme)
                    .setReference(255)
                    .setTime(1, 23, 45)
                    .setLeftButton("×", object : HmsPicker.OnLeftRightClickHandler {
                        override fun onClick(view: HmsPicker) {
                            view.reset()
                        }
                    })
                    .setRightButton("√", object : HmsPicker.OnLeftRightClickHandler {
                        override fun onClick(view: HmsPicker) {
                            toast("Yes")
                        }
                    })
                    .setDismissListener(DialogInterface.OnDismissListener {
                        toast("Dismiss")
                    })
                    .show()
        }

        var isShown = false
        btnView.setOnClickListener {
            hmsPicker.visibility = if (isShown) View.GONE else View.VISIBLE
            isShown = !isShown
        }
        hmsPicker.setRightButton("√", object : HmsPicker.OnLeftRightClickHandler {
            override fun onClick(view: HmsPicker) {
                onHmsPick(-1, view.hours, view.minutes, view.seconds)
            }
        })
    }

    override fun onHmsPick(reference: Int, hours: Int, minutes: Int, seconds: Int) {
        longToast("reference: $reference, hours: $hours, minutes: $minutes, seconds: $seconds")
    }
}

