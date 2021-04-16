package com.nan.myandroid.rengwuxian.view

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.nan.myandroid.R
import com.nan.myandroid.base.BaseVPFragmentActivity

class CustomViewActivity : BaseVPFragmentActivity() {

    override fun setLayoutId(): Int {
        return R.layout.activity_view
    }

    override val fragments: List<Fragment>
        get() = listOf(
            MultiTouchFragment.newInstance("", ""),
            ScalableImageFragment.newInstance("", ""),
            BitmapDrawableFragment.newInstance("", ""),
            AnimationFragment.newInstance("", ""),
            DashboardFragment.newInstance("", ""),
            SectorFragment01.newInstance("", ""),
            PathInOutRuleFragment.newInstance("", ""),
            CircleAvatarFragment.newInstance("", ""),
            TextFragment.newInstance("", ""),
            ClipTransformFragment.newInstance("", ""),
            XfermodeFragment.newInstance("", "")
        )
    override val titles: List<String>
        get() = listOf("多点触控","ScalableImage","Bitmap Drawable","属性动画","仪表盘", "饼图1", "path内外判断", "圆形头像","文字绘制及测量","范围裁切和几何变换","Xfermode")

    companion object {
        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, CustomViewActivity::class.java)
            context.startActivity(intent)
        }
    }

}