package com.nan.myandroid.rengwuxian.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.nan.myandroid.R
import com.nan.myandroid.Utils.dp
import com.nan.myandroid.getAvatar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * 多点触控
 * 大致场景分为三种：
 * 1接力型
 * 2配合型
 * 3各自为战型
 *
 */
class MultiTouchFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_multi_touch, container, false)
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MultiTouchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

/**
 * 单点触控
 */
class MultiTouch1(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val bitmap = getAvatar(200.dp.toInt())
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var offsetX = 0f
    private var offsetY = 0f

    private var originalOffsetX = 0f
    private var originalOffsetY = 0f

    private var downX = 0f
    private var downY = 0f


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y

                originalOffsetX = offsetX
                originalOffsetY = offsetY
            }

            MotionEvent.ACTION_MOVE -> {
                offsetX = event.x - downX + originalOffsetX
                offsetY = event.y - downY + originalOffsetY
                invalidate()
            }
        }
        return true
    }
}

/**
 * 多点触控接力型
 */
class MultiTouch2(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val bitmap = getAvatar(200.dp.toInt())
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var offsetX = 0f
    private var offsetY = 0f

    private var originalOffsetX = 0f
    private var originalOffsetY = 0f

    private var downX = 0f
    private var downY = 0f

    private var trackingPointerId = 0


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                trackingPointerId = event.getPointerId(0)
                downX = event.x
                downY = event.y

                originalOffsetX = offsetX
                originalOffsetY = offsetY

            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                val index = event.actionIndex
                trackingPointerId = event.getPointerId(index)
                downX = event.getX(index)
                downY = event.getY(index)

                originalOffsetX = offsetX
                originalOffsetY = offsetY


            }
            MotionEvent.ACTION_MOVE -> {
                val index = event.findPointerIndex(trackingPointerId)
                offsetX = event.getX(index) - downX + originalOffsetX
                offsetY = event.getY(index) - downY + originalOffsetY
                invalidate()
            }
            MotionEvent.ACTION_POINTER_UP -> {

                val index = if (event.actionIndex == event.pointerCount - 1) {
                    event.pointerCount - 2
                } else {
                    event.pointerCount - 1
                }
                trackingPointerId = event.findPointerIndex(index)
                //downX Y是用来计算偏移量的，控制点换了，计算偏移量的时候downX Y不能再已原来的了，所以需要更新
                downX=event.getX(index)
                downY=event.getY(index)


            }
        }
        return true
    }
}