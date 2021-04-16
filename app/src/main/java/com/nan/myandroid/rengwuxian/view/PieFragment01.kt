package com.nan.myandroid.rengwuxian.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
import kotlin.math.cos
import kotlin.math.sin

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 *饼图
 */
class SectorFragment01 : Fragment() {
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
        return inflater.inflate(R.layout.fragment_sector01, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SectorFragment01.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SectorFragment01().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

class PieView01(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val angles = floatArrayOf(100f, 50f, 150f, 60f)
    private val colors = listOf("#FF6A9E", "#776DFF", "#41FFAA", "#2DF2FF")
    private val paint = Paint()

    //View的上下左右距离中心的偏移量
    private val POINT_OFFSET = 100.dp

    private val SECTOR_OFFSET = 10.dp

    init {
        paint.style = Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {


    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        var startAngle = 0f

        for ((index, value) in angles.withIndex()) {
            paint.color = Color.parseColor(colors[index])

            if (index == 2) {
                canvas.save()
                //算偏移量的时候注意使用的角度是从0°+当前扇形的角度的一半，而只是当前扇形角度的一半
                canvas.translate(
                    (SECTOR_OFFSET * cos(Math.toRadians((startAngle + value / 2f).toDouble()))).toFloat(),
                    (SECTOR_OFFSET * sin(Math.toRadians((startAngle + value / 2f).toDouble()))).toFloat()
                )
                drawSector(canvas, startAngle, value)
                canvas.restore()
            } else {
                drawSector(canvas, startAngle, value)
            }

            startAngle += value

        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun drawSector(
        canvas: Canvas,
        startAngle: Float,
        value: Float
    ) {
        canvas.drawArc(
            width / 2 - POINT_OFFSET,
            height / 2 - POINT_OFFSET,
            width / 2 + POINT_OFFSET,
            height / 2 + POINT_OFFSET,
            startAngle,
            value,
            true,
            paint
        )
    }
}