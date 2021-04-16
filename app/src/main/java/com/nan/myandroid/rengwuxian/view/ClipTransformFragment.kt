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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 *范围裁切和几何变换
 */
class ClipTransformFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_crop_transform, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CropTransformFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ClipTransformFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

class ClipTransformView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val BITMAP_WIDTH = 200.dp
    private val BITMAP_PADDING = 100.dp

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private val clipPath = Path().apply {
        addOval(
            BITMAP_PADDING,
            BITMAP_PADDING,
            BITMAP_PADDING + BITMAP_WIDTH,
            BITMAP_PADDING + BITMAP_WIDTH,
            Path.Direction.CW
        )
    }


    //三维变换 camera
    private val camera = Camera()

    var topFlip = 0f
        set(value) {
            field = value
            invalidate()
        }
    var bottomFlip = 0f
        set(value) {
            field = value
            invalidate()
        }
    var flipRotation = 0f
        set(value) {
            field = value
            invalidate()
        }

    init {
        camera.setLocation(0f, 0f, -6 * resources.displayMetrics.density)
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        //clipRect
        //canvas.clipRect(BITMAP_PADDING,BITMAP_PADDING,BITMAP_PADDING+BITMAP_WIDTH/2f,BITMAP_PADDING+BITMAP_WIDTH/2f)
        //clipPath  使用这个实现圆形头像会有毛边 xfermode的方式没有毛边
        //clipOutRect clipOutPath 是将相应的裁切留切剩下的部分
        //canvas.clipPath(clipPath)


        //几何变换 两种：canvas.xxx  matrix  真实View没变，只是展示出来的效果变了
        //canvas.translate rotate scale skew
        //多个几何变要是按照图形为视角需要倒着写，因为变化的是坐标系
        //matrix preXXX postXXX(如果思考以图形为变换而不是画布，可以使用postXXX正着写)

        //上半部分
        canvas.save()
        canvas.translate(BITMAP_PADDING + BITMAP_WIDTH / 2, BITMAP_PADDING + BITMAP_WIDTH / 2)
        canvas.rotate(flipRotation)
        canvas.clipRect(-BITMAP_WIDTH, -BITMAP_WIDTH, BITMAP_WIDTH, 0f)
        camera.save()
        camera.rotateX(topFlip)
        camera.applyToCanvas(canvas)
        camera.restore()
        canvas.rotate(-flipRotation)
        canvas.translate(-(BITMAP_PADDING + BITMAP_WIDTH / 2), -(BITMAP_PADDING + BITMAP_WIDTH / 2))
        canvas.drawBitmap(getAvatar(BITMAP_WIDTH.toInt()), BITMAP_PADDING, BITMAP_PADDING, paint)
        canvas.restore()

        //下半部分
        canvas.save()
        canvas.translate(BITMAP_PADDING + BITMAP_WIDTH / 2, BITMAP_PADDING + BITMAP_WIDTH / 2)
        canvas.rotate(flipRotation)
        canvas.clipRect(-BITMAP_WIDTH, 0f, BITMAP_WIDTH, BITMAP_WIDTH)
        camera.save()
        camera.rotateX(bottomFlip)
        camera.applyToCanvas(canvas)
        camera.restore()
        canvas.rotate(-flipRotation)
        canvas.translate(-(BITMAP_PADDING + BITMAP_WIDTH / 2), -(BITMAP_PADDING + BITMAP_WIDTH / 2))
        canvas.drawBitmap(getAvatar(BITMAP_WIDTH.toInt()), BITMAP_PADDING, BITMAP_PADDING, paint)
        canvas.restore()


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
