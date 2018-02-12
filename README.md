# HmsPicker
A simple Hours-Minutes-Seconds picker. Available for a custom view or a dialog.

Separated and optimized from [android-betterpickers hmspicker](https://github.com/code-troopers/android-betterpickers)

## Screenshots
| Light Theme | Custom Theme | In the View |
|:-:|:-:|:-:|
| ![Light Theme](https://github.com/DeweyReed/HmsPicker/blob/master/art/light.png?raw=true) | ![Custom Theme](https://github.com/DeweyReed/HmsPicker/blob/master/art/custom.png?raw=true) | ![In the View](https://github.com/DeweyReed/HmsPicker/blob/master/art/view.png?raw=true) |

## Install

## Usage
### Xml
```
<io.github.deweyreed.hmspicker.HmsPicker
    android:id="@+id/hmsPicker"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```
Then, use ```hmsPicker.getHours()``` to get users' input.
### Show a dialog fragment
Implement ```HmsPickerDialog.HmsPickHandler``` for your activity or whatever.
```
class MainActivity : AppCompatActivity(), HmsPickerDialog.HmsPickHandler {
```
```
override fun onHmsPick(reference: Int, hours: Int, minutes: Int, seconds: Int) {
    longToast("reference: $reference, hours: $hours, minutes: $minutes, seconds: $seconds")
}
```
Then, build it.
```
HmsPickerBuilder(supportFragmentManager, this)
    .setStyleResId(R.style.CustomHmsPickerTheme)
    .setReference(255)
    .setTime(1, 23, 45)
    .setLeftButton("×", object : HmsPicker.OnLeftRightClickHandler {
        override fun onClick(view: HmsPicker) {
        }
    })
    .setRightButton("√", object : HmsPicker.OnLeftRightClickHandler {
        override fun onClick(view: HmsPicker) {
        }
    })
    .setDismissListener(DialogInterface.OnDismissListener {
    })
    .show()
```
### Theme
Default is a light one(```R.style.HmsPickerThemeLight```) and a dark one(```R.style.HmsPickerThemeDark```) is also available.

You can create your own theme. Example can be found [here](https://github.com/DeweyReed/HmsPicker/blob/master/app/src/main/res/values/styles.xml#L12).

Remember that drawables you use should be selector or ripple.

## License
[Apache License 2.0](https://github.com/code-troopers/android-betterpickers#license) for android-betterpickers.

[MIT License](https://github.com/DeweyReed/HmsPicker/blob/master/LICENSE) for this project.
