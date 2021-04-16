package com.nan.myandroid.rengwuxian.view

import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.nan.myandroid.R
import com.nan.myandroid.Utils.dp

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * canvas绘制两个图形的时候，绘制完一个之后改变共同使用的paint的xfermode
 * 为PorterDuffXfermode中的类型，会使前后两个图形最后绘制的结果的融合
 * 规则发生相应的变化。
 * 示例：刮刮乐 圆形头像
 * PorterDuffXfermode各种模式说明：https://developer.android.google.cn/reference/android/graphics/PorterDuff.Mode?hl=en
 * 融合说明：融合只针对下层图片与上层图片的公共部分以及上层图片的全部（如何理解？融合其实是针对后画上去的图形（source）为主的，表示的是source整体与下层已经
 * 绘制好的图形的重叠部分的融合规则，进一步理解参考着个优点错误的博客连接：
 * https://www.jianshu.com/p/4cbe0737238e?utm_campaign=hugo&utm_medium=reader_share&utm_content=note&utm_source=weixin-friends
 * 这个博客中说GOOGLE官方的PorterDuffXfermode示例有问题，并给出了'真正的'效果图，其实官方的示例并没有问题，他所给出的方形和圆形除了叠加的部分并没有官方示例图形
 * 的透明背景,下面会写一个例子看一下官方示例和博客中的区别）。
 *
 */
class XfermodeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_xfermode, container, false)
    }

    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment XfermodeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            XfermodeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

class XfermodeView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    //圆形半径 方形边长一半
    private val RADIUS = 100.dp

    //背景正方形Bitmap的边长
    private val backBitmapSide = 400.dp

    //另一组例子下移的偏移量
    private val OFFSET=300.dp

    private val circleBitmap =
        Bitmap.createBitmap(backBitmapSide.toInt(), backBitmapSide.toInt(), Bitmap.Config.ARGB_8888)
    private val rectBitmap =
        Bitmap.createBitmap(backBitmapSide.toInt(), backBitmapSide.toInt(), Bitmap.Config.ARGB_8888)

    private val bounds1 by lazy { RectF(0f, 0f, backBitmapSide, backBitmapSide) }

    private val bounds2 by lazy { RectF(0f, 0f+OFFSET, backBitmapSide, backBitmapSide+OFFSET) }
    private val xfermode by lazy {
        PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }


    init {
        val canvas = Canvas(circleBitmap)
        //circle的位置是性对于bitmap
        paint.color = Color.parseColor("#4323ff")
        canvas.drawCircle(RADIUS + 20, RADIUS + 20, RADIUS, paint)
        canvas.setBitmap(rectBitmap)
        paint.color = Color.parseColor("#f2392f")
        canvas.drawRect(RADIUS + 20, RADIUS + 20, RADIUS * 3 + 20, RADIUS * 3 + 20, paint)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {

        //官方示例
        //重点1 需要使用离屏缓冲，只对需要起作用的部分做融合，不然会将底部的其他View也算上
        val saveLayer1 = canvas.saveLayer(bounds1, paint)
        canvas.drawBitmap(circleBitmap, 0f, 0f, paint)
        paint.xfermode = xfermode
        canvas.drawBitmap(rectBitmap, 0f, 0f, paint)
        //重点2 使用xfermode之后记得还原
        paint.xfermode = null
        canvas.restoreToCount(saveLayer1)

        //博客中只需要图形不要背景的情况
        val saveLayer2 = canvas.saveLayer(bounds2, paint)
        paint.color = Color.parseColor("#4323ff")
        canvas.drawCircle(RADIUS + 20, RADIUS + 20+OFFSET, RADIUS, paint)
        paint.xfermode = xfermode
        paint.color = Color.parseColor("#f2392f")
        canvas.drawRect(RADIUS + 20, RADIUS + 20+OFFSET, RADIUS * 3 + 20, RADIUS * 3+OFFSET + 20, paint)
        paint.xfermode = null
        canvas.restoreToCount(saveLayer2)

    }

}

