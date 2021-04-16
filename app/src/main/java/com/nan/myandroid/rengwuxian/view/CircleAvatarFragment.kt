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
 * 圆形头像
 * Xfermode
 * 图像重合时，原像素与目标像素的融合规则
 */
class CircleAvatarFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_xfer_mode_activity, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment XferModeActivity.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CircleAvatarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

class AvatarView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
     var imageWidth = 200.dp
        set(value) {
            field=value
            invalidate()
        }
    private val IMAGE_PADDING = 20.dp
    private val IMAGE_RADIUS = 100.dp
    private val XFERMODE = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    private val bounds by lazy {
        RectF(
            width / 2f - imageWidth / 2,
            height / 2f - imageWidth / 2,
            width / 2f + imageWidth / 2,
            height / 2f + imageWidth / 2
        )
    }





    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {

        val saveLayer = canvas.saveLayer(bounds, null)
        canvas.drawCircle(width/2f,height/2f,IMAGE_RADIUS,paint)
        paint.xfermode=XFERMODE
        canvas.drawBitmap(getAvatar(imageWidth.toInt()),width/2f-imageWidth/2,height/2f-imageWidth/2,paint)
        paint.xfermode=null
        canvas.restoreToCount(saveLayer)
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