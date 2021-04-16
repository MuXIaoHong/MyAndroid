package com.nan.myandroid.rengwuxian.view

import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.text.StaticLayout
import android.text.TextPaint
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
 * 文字绘制及测量
 * 多行文字绘制
 * 图文混排
 */
class TextFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_text, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TextFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TextFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

class CustomTextView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val CIRCLE_COLOR = Color.parseColor("#4f4f44")
    private val HIGHT_LIGHT_COLOR = Color.parseColor("#223d92")
    private val RING_WIDTH = 20.dp
    private val RADIUS = 150.dp
    private val bounds = Rect()
    private val fontMetrics = Paint.FontMetrics()

    init {
        paint.textSize = 100.dp
        paint.textAlign = Paint.Align.CENTER
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        //绘制圆环
        paint.style = Paint.Style.STROKE
        paint.color = CIRCLE_COLOR
        paint.strokeWidth = RING_WIDTH
        canvas.drawCircle(width / 2f, height / 2f, RADIUS, paint)

        //绘制进度条
        paint.color = HIGHT_LIGHT_COLOR
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawArc(
            width / 2f - RADIUS,
            height / 2f - RADIUS,
            width / 2f + RADIUS,
            height / 2f + RADIUS,
            -90f,
            225f,
            false,
            paint
        )

        //绘制文字
        paint.style = Paint.Style.FILL

        //方法1：适合绘制静态不变的文字
        //getTextBounds获取文字实际所占的区域,这个bounds以baseline左边为原点
        //四个端点大致坐标为b:5 l:12 r:500 t:-190
        paint.getTextBounds("aabb", 0, "aabb".length, bounds)
        //计算居中的y坐标的时候就使用期望的值减去文字实际区域的高的一半（(bounds.top+bounds.bottom)/2）
        canvas.drawText("aabb", width / 2f, height / 2f - (bounds.top + bounds.bottom) / 2, paint)

        //方法2：适合绘制动态变化的文字
        paint.getFontMetrics(fontMetrics)
        canvas.drawText(
            "aabb",
            width / 2f,
            height / 2f - (fontMetrics.ascent + fontMetrics.descent) / 2,
            paint
        )

        //贴边（左上）
        paint.getFontMetrics(fontMetrics)
        paint.getTextBounds("aabb", 0, "aabb".length, bounds)
        canvas.drawText("aabb", 0f - bounds.left, 0f - fontMetrics.ascent, paint)

    }
}

@RequiresApi(Build.VERSION_CODES.M)
class MultilineTextView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val text =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam faucibus ut lorem at fermentum. In porttitor quam a dui maximus, a egestas felis ullamcorper. Aliquam quis porta enim, id egestas tellus. Donec vitae commodo est, et dictum mauris. Fusce elementum posuere sem, sed sollicitudin mi. Nunc fermentum odio ut lacus porttitor, quis tristique ex gravida. In consequat magna ac dolor rutrum, vitae vehicula augue tincidunt. Maecenas eget ligula sem. Aenean pellentesque lacus vel porta blandit. Phasellus auctor enim tellus, et tristique est fringilla vitae. Vivamus viverra quis nisl eu interdum. Sed ac sollicitudin ipsum. Etiam cursus rhoncus nisl."
    private val paint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 16.dp
    }
    private val BITMAP_WIDTH = 150.dp

    //简单多行绘制使用staticLayout
    private val staticLayout by lazy {
        StaticLayout.Builder.obtain(text, 0, text.length, paint, width).build()
    }
    private val bitmap = getAvatar(BITMAP_WIDTH.toInt())
    private val fontMetrics = Paint.FontMetrics()
    private val measuredWidth= floatArrayOf(0f)
    override fun onDraw(canvas: Canvas) {
//        staticLayout.draw(canvas)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        //paint初始化好了 textpain的fontMetrics的值就定好了
        paint.getFontMetrics(fontMetrics)

        var startIndex = 0
        //绘制文字使用的x y就是baseline的原点
        //为了第一行不被遮盖，y向下偏移-fontMetrics.top,这是top是负值
        var baselineY=-fontMetrics.top

        while (startIndex < text.length) {
            var maxWidth = if (baselineY+fontMetrics.top < BITMAP_WIDTH) {
                (width - BITMAP_WIDTH).toInt()
            } else {
                width
            }
            var baselineX = if (baselineY+fontMetrics.top < BITMAP_WIDTH) {
                BITMAP_WIDTH
            } else {
                0f
            }

            //paint.breakText用来算出给出的长度内能绘制的最后一个文字的个数
            val count = paint.breakText(text, startIndex, text.length, true, maxWidth.toFloat(), measuredWidth)
            canvas.drawText(text,startIndex,startIndex+count,baselineX,baselineY,paint)
            startIndex+=count
            //fontSpacing行距
            baselineY+=paint.fontSpacing
        }

    }


    private fun getAvatar(width: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.drawable.avatar, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        return BitmapFactory.decodeResource(resources, R.drawable.avatar, options)

    }
}