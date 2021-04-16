package com.nan.myandroid.rengwuxian.view

import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.nan.myandroid.R
import com.nan.myandroid.Utils.dp
import kotlin.math.cos
import kotlin.math.sin

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 *仪表盘
 *刻度
 *时钟
 */
class DashboardFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

/**
 *author：93289
 *date:2020/8/7
 *dsc:
 */

/**
 * 仪表盘
 */
class DashboardView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    //仪表圆弧
    private val path = Path()

    //刻度信息
    private val dash_path = Path()
    private val DASH_WIDTH = 2.dp
    private val DASH_HEIGHT = 5.dp

    //指针信息
    private val POINTER_WIDTH = 2.dp
    private val POINTER_HEIGHT = 80.dp

    init {
        //init中初始化一开始需要的东西
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2.dp
    }

    //View的上下左右距离中心的偏移量
    private val POINT_OFFSET = 100.dp

    //弧形的角度 300°
    private val DASHBOARD_ANGLE = 270f

    //有多少个刻度
    private val POINTER_COUNT = 20

    private val pathMeasure = PathMeasure()
    private lateinit var pathDashPathEffect: PathDashPathEffect


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //再View尺寸改变的时候，初始化path
        path.reset()
        path.addArc(
            width / 2 - POINT_OFFSET,
            height / 2 - POINT_OFFSET,
            width / 2 + POINT_OFFSET,
            height / 2 + POINT_OFFSET,
            90 + (360 - DASHBOARD_ANGLE) / 2,
            DASHBOARD_ANGLE
        )

        dash_path.reset()
        dash_path.addRect(0F, 0F, DASH_WIDTH, DASH_HEIGHT, Path.Direction.CCW)
        pathMeasure.setPath(path, false)
        pathDashPathEffect = PathDashPathEffect(
            dash_path, (pathMeasure.length - DASH_WIDTH) / (POINTER_COUNT - 1), 0f,
            PathDashPathEffect.Style.ROTATE
        )
    }

    override fun onDraw(canvas: Canvas) {
        //画仪表盘圆弧
        canvas.drawPath(path, paint)
        //画刻度
        paint.pathEffect = pathDashPathEffect
        canvas.drawPath(path, paint)
        paint.pathEffect = null

        //画指针,指针要指向刻度，所以要根据每个刻度对应的角度计算指针末端的坐标

        //刻度对应的角度为第一个刻度角度+每个刻度之间占用的角度*刻度的下标
        val index = 17
        val angle =
            90 + (360 - DASHBOARD_ANGLE) / 2 + index * (DASHBOARD_ANGLE / (POINTER_COUNT - 1))

        //使用cos sin的时候要用弧度表示角度，
        val radians = Math.toRadians(angle.toDouble())

        canvas.drawLine(
            width / 2f, height / 2f,
            width / 2f + cos(radians).toFloat() * POINTER_HEIGHT,
            height / 2f + sin(radians).toFloat() * POINTER_HEIGHT, paint
        )
    }

}