package com.nan.myandroid.rengwuxian.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nan.myandroid.R
import com.nan.myandroid.Utils.dp

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Bitmap 存储每个图片的每个点位的像素信息
 * Drawable 绘制工具（可以用来调用canvas绘制），和View类似。View负责测量布局绘制，Drawable只负责绘制。
 * 他们不是一个东西，互转的本质是互相生成。
 * 怎么互转：
 * B2D:BitmapDrawable(resources, Bitmap.createBitmap(...))
 * D2B:如果是BitmapDrawable，直接拿到其中的Bitmap，如果不是则通过Drawable中的绘制规则使用drawable.draw(canvas)画出Bitmap
 */
class BitmapDrawableFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_bitmap_drawable, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //自定义Drawable

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BitmapDrawableFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BitmapDrawableFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

class DrawableView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val colorDrawable =ColorDrawable(Color.RED).apply {
        setBounds(0,0, 200.dp.toInt(), 200.dp.toInt())
    }
    override fun onDraw(canvas: Canvas) {
        colorDrawable.draw(canvas)
        //canvas.drawBitmap() 为什么 drawable可以调用draw(canvas)画canvas， 而Bitmap却 canvas.drawBitmap()被canvas画
        //因为Drawable类似于一个View，里面装的是绘制规则，而Bitmap装的是具体的像素点位信息


    }
}

/**
 *自定义Drawable,可以用于提出View中一些公共的图形
 */
class MeshDrawable:Drawable(){

    override fun draw(canvas: Canvas) {

    }

    override fun setAlpha(alpha: Int) {
        TODO("Not yet implemented")
    }

    override fun getOpacity(): Int {
        TODO("Not yet implemented")
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        TODO("Not yet implemented")
    }
}