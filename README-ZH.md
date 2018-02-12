# HmsPicker
一个简单的 小时-分钟-秒 的时间选择控件。可用于XML或者Dialog中。

从[android-betterpickers hmspicker](https://github.com/code-troopers/android-betterpickers)中分离并优化，以缩小APK大小并增加新功能。

## 截图
| 默认主题 | 自定义主题 | XML中 |
|:-:|:-:|:-:|
| ![默认主题](https://github.com/DeweyReed/HmsPicker/blob/master/art/light.png?raw=true) | ![自定义主题](https://github.com/DeweyReed/HmsPicker/blob/master/art/custom.png?raw=true) | ![XML中](https://github.com/DeweyReed/HmsPicker/blob/master/art/view.png?raw=true) |

## 安装
1. 在根build.gradle添加jitpack.io:
```
allprojects {
	repositories {
        ...
		maven { url 'https://jitpack.io' }
	}
}
```
2. 
```
dependencies {
	implementation 'com.github.DeweyReed:HmsPicker:1.0.0'
}
```
[![](https://jitpack.io/v/DeweyReed/HmsPicker.svg)](https://jitpack.io/#DeweyReed/HmsPicker)
## 使用
### XML
```
<io.github.deweyreed.hmspicker.HmsPicker
    android:id="@+id/hmsPicker"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```
用```hmsPicker.getHours()```获取输入。
### 显示Dialog
为Activity或其他实现```HmsPickerDialog.HmsPickHandler```。
```
class MainActivity : AppCompatActivity(), HmsPickerDialog.HmsPickHandler {
```
```
override fun onHmsPick(reference: Int, hours: Int, minutes: Int, seconds: Int) {
    longToast("reference: $reference, hours: $hours, minutes: $minutes, seconds: $seconds")
}
```
Build
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
### 主题
默认是亮色主题(```R.style.HmsPickerThemeLight```)还有一个暗色主题(```R.style.HmsPickerThemeDark```)。

也可以创建自定义主题，例子[在这](https://github.com/DeweyReed/HmsPicker/blob/master/app/src/main/res/values/styles.xml#L12).

别忘了你使用的Drawables应该是selector或者ripple。

## License
[Apache License 2.0](https://github.com/code-troopers/android-betterpickers#license) for android-betterpickers.

[MIT License](https://github.com/DeweyReed/HmsPicker/blob/master/LICENSE) for this project.
