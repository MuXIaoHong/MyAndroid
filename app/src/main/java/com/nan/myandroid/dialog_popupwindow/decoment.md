# PopupWindow
## 使用原因
可以方便的指定一个弹窗到某些View或者Parent的下方并且可以指定偏移。
# Dialog
## 使用原因
各种Dialog的基类，一般本直接使用。
# AlertDialog
## 使用原因
适合用来实现具有灰色背景（蒙版）的各种弹窗。
## 常用方法
### 修改宽高 
```
loadingDialog.window?.run {
    setBackgroundDrawableResource(android.R.color.transparent)
    val outRect = Rect()
    decorView.getWindowVisibleDisplayFrame(outRect)
    setLayout(outRect.width() / 5.toInt(), outRect.height() / 3)
}
```
# DialogFragment
## 使用原因
同时具有Dialog和Fragment的特性，方便处理Dialog随着界面生命周期的交互。
## 常用方法
### 全屏显示
```
<style name="DiaFragmentProjectEdit" parent="Theme.AppCompat.Dialog">
    <item name="android:background">@android:color/transparent</item>
    <item name="android:windowBackground">@android:color/transparent</item>
    <item name="android:windowNoTitle">true</item>
    <item name="android:backgroundDimEnabled">true</item>
    <item name="android:windowIsFloating">false</item>
    <item name="android:windowFullscreen">true</item>
</style>

override fun getTheme(): Int {
        return R.style.DiaFragmentProjectEdit
    }
```

### 设置进出动画
```
<style name="dialogWindowAnim" parent="android:Animation">
    <item name="android:windowEnterAnimation">@anim/dialog_fragment_open</item>
    <item name="android:windowExitAnimation">@anim/dialog_fragment_close</item>
</style>

 override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setWindowAnimations(animStyle)
        }
    }
```