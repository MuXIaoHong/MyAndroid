package com.nan.myandroid.rengwuxian.view

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nan.myandroid.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 *GPU:图形处理器
 *硬件加速！
 *软件绘制，CPU绘制，整个界面的绘制都是基于一个Bitmap,在上面叠加融合。
 *硬件绘制，GPU绘制，CPU在绘制阶段（canvas.drawxxx）转换成GPU操作，并没有转换成像素，而是GPU完成操作之后，再将像素显示在屏幕上。
 *硬件加速：就是使用GPU绘制，硬件绘制和硬件加速是一个东西
 *为什么可以加速：1 CPU一部分工作分到更快的GPU  2 GPU具有绘制优势，天生快  3 绘制流程得到优化（优化重绘流程）
 *硬件绘制缺点：缺少兼容性（Android系统开发这不知道有些图形使用GPU怎么绘制） 软件绘制则不会
 *
 *
 */
class YJJSFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_y_j_j_s, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment YJJSFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            YJJSFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

/**
 *硬件加速和离屏缓冲的一些容易误解的点
 *开启硬件加速:在Application 中添加标签：android:hardwareAccelerated="true"
 *在Application中开启了之后没有特定API在其他View中单独关闭(setLayerType(LAYER_TYPE_SOFTWARE,null)简介可以，但其实这个方法是来设置View级别（区别于saveLayer）离屏缓冲的方式的)
 *setLayerType的使用场景目前知道的是在使用属性动画的时候view.animate().translationY(200).withLayer()，这里withLayer()的使用会使用GPU的离屏缓冲优化重绘，前提示这个动画只针对于
 *translateX等非自定义属性的动画。
 *
 */
class YJJSvIEW(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    init {
        setLayerType(LAYER_TYPE_HARDWARE,null)
        setLayerType(LAYER_TYPE_SOFTWARE,null)
        setLayerType(LAYER_TYPE_NONE,null)
    }
}