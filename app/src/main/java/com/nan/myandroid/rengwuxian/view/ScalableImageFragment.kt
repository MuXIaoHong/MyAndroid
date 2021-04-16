package com.nan.myandroid.rengwuxian.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.OverScroller
import android.widget.Scroller
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.core.view.GestureDetectorCompat
import com.nan.myandroid.R
import com.nan.myandroid.Utils.dp
import com.nan.myandroid.getAvatar
import kotlin.math.max
import kotlin.math.min

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ScalableImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScalableImageFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_scalable_image, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScalableImageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScalableImageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

/**
 *author：93289
 *date:2020/8/21
 *dsc:
 */
class ScalableImageView(context: Context?, attrs: AttributeSet?) : View(context, attrs), Runnable {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val IMAGE_SIZE = 300.dp
    private val imageBitmap = getAvatar(IMAGE_SIZE.toInt())

    private var originalOffsetX = 0f
    private var originalOffsetY = 0f

    private var offsetX = 0f
    private var offsetY = 0f

    private var curScaleFactor = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var smallScaleFactor = 0f
    private var bigScaleFactor = 0f
    private var big = false

    //OverScroller适合做有初速的滑动 Scroller适合做没有初始速度的
    private val scroller = OverScroller(context)

    private val scaleAnimator =
        ObjectAnimator.ofFloat(this, "curScaleFactor", smallScaleFactor, bigScaleFactor)
    private val gestureDetector = GestureDetectorCompat(context, MyGestureListener())
    private val scaleGestureDetector = ScaleGestureDetector(context, ScaleGestureListener())


    init {
        gestureDetector.setOnDoubleTapListener(object : GestureDetector.OnDoubleTapListener {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                // ⽤户双击时被调⽤
                // 注意：第⼆次触摸到屏幕时就调⽤，⽽不是抬起时
                Log.d("ScalableImageView", "onDoubleTap")
                big = !big
                if (big) {
                    offsetX = (e.x - width / 2)*(1-bigScaleFactor/smallScaleFactor)
                    offsetY = (e.y - height / 2)*(1-bigScaleFactor/smallScaleFactor)
                    fixOffset()
                    scaleAnimator.start()
                } else {
                    scaleAnimator.reverse()
                }
                return false
            }

            override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
                // ⽤户双击第⼆次按下时、第⼆次按下后移动时、第⼆次按下后抬起时都 会被调⽤
                //用于上级之后的后续操作
                // 常⽤于「双击拖拽」的场景
                Log.d("ScalableImageView", "onDoubleTapEvent")
                return false
            }

            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                // ⽤户单击时被调⽤,支持双击的时候用这个判断单击，不支持双击的时候用onSingltTapUp
                // 和 onSingltTapUp() 的区别在于，⽤户的⼀次点击不会⽴即调⽤ 这个⽅法，⽽是在⼀定时间后（300ms），确认⽤户没有进⾏双击，这个⽅法 才会被调⽤
                Log.d("ScalableImageView", "onSingleTapConfirmed")
                return false
            }
        })
    }

    inner class MyGestureListener : GestureDetector.OnGestureListener {

        override fun onShowPress(e: MotionEvent?) {
            // ⽤户按下 100ms 不松⼿后会被调⽤，⽤于标记「可以显示按下状态了
            Log.d("ScalableImageView", "onShowPress")
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            // ⽤户单击时被调⽤(⽀持⻓按时⻓按后松⼿不会调⽤、双击的第⼆下时 不会被调⽤)
            Log.d("ScalableImageView", "onSingleTapUp")
            return false
//            return true
        }

        override fun onDown(e: MotionEvent?): Boolean {
            Log.d("ScalableImageView", "onDown")
            return true
//            return false  onDown必须返回true，否则各种事件无法正常触发，理解为由这个监听消费
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            Log.d("ScalableImageView", "velocityX $velocityX  velocityY $velocityY")
            Log.d("ScalableImageView", "offsetX $offsetX  offsetY $offsetY")
            if (big) {
                scroller.fling(
                    offsetX.toInt(), offsetY.toInt(), velocityX.toInt(),velocityY.toInt(),
                    (-(bigScaleFactor*imageBitmap.width-width)/2).toInt(),
                    ((bigScaleFactor*imageBitmap.width-width)/2).toInt(),
                    (-(bigScaleFactor*imageBitmap.height-height)/2).toInt(),
                    ((bigScaleFactor*imageBitmap.height-height)/2).toInt()
                )

                postOnAnimation(this@ScalableImageView)
            }

            return false
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            // ⽤户滑动时被调⽤ distanceX\Y 是移动的距离
            // 第⼀个事件是⽤户按下时的 ACTION_DOWN 事件，第⼆个事件是当前 事件
            // 偏移是按下时的位置 - 当前事件的位置
            Log.d("ScalableImageView", "distanceX=$distanceX ,distanceY=$distanceY ")
            if (big) {
                offsetX -= distanceX
                offsetY -= distanceY
                fixOffset()
                invalidate()
            }

            return false
        }

        override fun onLongPress(e: MotionEvent?) {
            // ⽤户⻓按（按下 500ms 不松⼿）后会被调⽤
            // 这个 500ms 在 GestureDetectorCompat 中变成了 600ms
            Log.d("ScalableImageView", "onLongPress")
        }

    }

    private fun fixOffset() {
        offsetX = min(offsetX, (IMAGE_SIZE * bigScaleFactor - width) / 2)
        offsetX = max(offsetX, -(IMAGE_SIZE * bigScaleFactor - width) / 2)
        offsetY = min(offsetY, (IMAGE_SIZE * bigScaleFactor - height) / 2)
        offsetY = max(offsetY, -(IMAGE_SIZE * bigScaleFactor - height) / 2)
    }

    inner class ScaleGestureListener : ScaleGestureDetector.OnScaleGestureListener {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            Log.d("ScalableImageView", "onScaleBegin")
            //要返回true才能开启缩放
            offsetX = (detector.focusX - width / 2)*(1-curScaleFactor/smallScaleFactor)
            offsetY = (detector.focusY - height / 2)*(1-curScaleFactor/smallScaleFactor)
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            Log.d("ScalableImageView", "onScaleEnd")
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            Log.d("ScalableImageView", "onScale.detector.scaleFactor=${detector.scaleFactor}")
            val tempScaleFactor=curScaleFactor*detector.scaleFactor
            if (tempScaleFactor<smallScaleFactor||tempScaleFactor>bigScaleFactor){
                return false
            }
            curScaleFactor*=detector.scaleFactor
            return true
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        originalOffsetX = (width - IMAGE_SIZE) / 2f
        originalOffsetY = (height - IMAGE_SIZE) / 2f
        if (imageBitmap.width / imageBitmap.height > width / height) {
            smallScaleFactor = width / IMAGE_SIZE
            bigScaleFactor = height / IMAGE_SIZE * 1.5f
        } else {
            bigScaleFactor = width / IMAGE_SIZE
            smallScaleFactor = height / IMAGE_SIZE * 1.5f
        }
        if (curScaleFactor == 0f) {
            curScaleFactor = smallScaleFactor
        }
        scaleAnimator.setFloatValues(smallScaleFactor, bigScaleFactor)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //用于缩小回来的时候重置offset为0，但是要慢慢变化
        val scaleFactor = (curScaleFactor - smallScaleFactor) / (bigScaleFactor - smallScaleFactor)

        canvas.translate(offsetX * scaleFactor, offsetY * scaleFactor)
        canvas.scale(curScaleFactor, curScaleFactor, width / 2f, height / 2f)
        canvas.drawBitmap(imageBitmap, originalOffsetX, originalOffsetY, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        if (!scaleGestureDetector.isInProgress) {
            gestureDetector.onTouchEvent(event)
        }
        return true
    }

    override fun run() {
        if (scroller.computeScrollOffset()){
            offsetX= scroller.currX.toFloat()
            offsetY= scroller.currY.toFloat()
            invalidate()
            postOnAnimation(this)
        }
    }


}