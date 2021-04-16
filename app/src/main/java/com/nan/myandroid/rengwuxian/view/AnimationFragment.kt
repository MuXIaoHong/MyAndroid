package com.nan.myandroid.rengwuxian.view

import android.animation.*
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import android.os.Bundle
import android.text.TextPaint
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.view.isInvisible
import com.nan.myandroid.R
import com.nan.myandroid.Utils.dp
import kotlinx.android.synthetic.main.fragment_animation.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * 属性动画
 */
class AnimationFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_animation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //ViewPropertyAnimator
        //button.animate().setStartDelay(1000).translationX(200f).translationY(200f).translationX(200f).translationY(200f).translationX(200f).translationY(200f)

        //ObjectAnimator
        /* ObjectAnimator.ofFloat(avatar,"imageWidth",400.dp).run {
            startDelay=1000
             start()
         }*/

        //AnimatorSet
        /*val bottomFlip = ObjectAnimator.ofFloat(clipView, "bottomFlip", 60f).apply {
            startDelay = 1000
            duration = 1000
        }
        val flipRotation = ObjectAnimator.ofFloat(clipView, "flipRotation", 360f).apply {
            startDelay = 200
            duration = 1000
        }
        val topFlip = ObjectAnimator.ofFloat(clipView, "topFlip", -60f).apply {
            startDelay = 200
            duration = 1000
        }
        AnimatorSet().run {
            //依次执行
            playSequentially(bottomFlip,flipRotation,topFlip)
            start()
        }*/

        //PropertyValuesHolder 结合 KeyFrame(关键帧) 指定某个时间点（0-1的小数)所修改属性的值。
        /*val length=600f
        // fraction
        val keyFrame1=Keyframe.ofFloat(0.3f,0.3f*length)
        val keyFrame2=Keyframe.ofFloat(0.4f,0.5f*length)
        val keyFrame3=Keyframe.ofFloat(0.5f,0.9f*length)
        val keyFrame4=Keyframe.ofFloat(0.8f,0.1f*length)
        val keyFrame5=Keyframe.ofFloat(0.9f,0.4f*length)
        PropertyValuesHolder.ofKeyframe("translationX",keyFrame1,keyFrame2,keyFrame3,keyFrame4,keyFrame5).also {
            ObjectAnimator.ofPropertyValuesHolder(imageview,it).run {
                startDelay=1000
                duration=3000
                start()
            }
        }*/

        //Interpolator 插值器
        /* ObjectAnimator.ofFloat().apply {
             interpolator=AccelerateInterpolator()
             interpolator= DecelerateInterpolator()
         }*/

        //TypeEvaluator 估值器
        /* ObjectAnimator.ofObject(pointF,"point",PointFEvaluator(),PointF(500f,500f)).run {
             startDelay=1000
             duration=3000
             start()
         }*/

        /*ObjectAnimator.ofObject(proviceView,"text",ProvinceEvaluator(), provinceList.last()).run {
            startDelay=1000
            duration=5000
            start()
        }*/
        //Listener
        //ValueAnimator 最基本的动画，就是数值的变化，一般用不到

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AnimationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AnimationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}

//-----------------------
class PointFView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var point = PointF(0f, 0f)
        set(value) {
            field = value
            invalidate()
        }

    init {
        paint.strokeWidth = 20.dp
        paint.strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPoint(point.x, point.y, paint)
    }
}

class PointFEvaluator : TypeEvaluator<PointF> {
    override fun evaluate(fraction: Float, startValue: PointF, endValue: PointF): PointF {
        val startX = startValue.x
        val endX = endValue.x
        val currentX = startX + (endX - startX) * fraction

        val startY = startValue.y
        val endY = endValue.y
        val currentY = startY + (endY - startY) * fraction
        return PointF(currentX, currentY)
    }
}

//-----------------------
val provinceList = listOf("滴答答滴","滴滴打的", "滴答滴滴", "滴滴答答", "嗒嘀嗒嗒")

class ProvinceView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 60.dp
        textAlign = Paint.Align.CENTER
    }
    var text = provinceList.first()
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText(text,width/2f,height/2f,paint)
    }

}
class  ProvinceEvaluator : TypeEvaluator<String> {
    override fun evaluate(fraction: Float, startValue: String, endValue: String): String {
            val startIndex= provinceList.indexOf(startValue)
            val endIndex= provinceList.indexOf(endValue)
            val currentIndex=startIndex+((endIndex-startIndex)*fraction).toInt()
        return provinceList[currentIndex]
    }
}
//-----------------------