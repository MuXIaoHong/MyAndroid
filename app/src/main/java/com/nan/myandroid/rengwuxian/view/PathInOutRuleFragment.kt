package com.nan.myandroid.rengwuxian.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nan.myandroid.R
import com.nan.myandroid.Utils.dp

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Path添加多个图形
 * 叠加
 * 内部外部判断规则
 */
class PathInOutRuleFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_path_in_out_rule, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PathInOutRuleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PathInOutRuleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

class PathInOutRule(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()

    private val CIRCLE_RADIUS = 50.dp
    private val RECT_WIDTH = 50.dp


    override fun onDraw(canvas: Canvas) {

        paint.color = Color.parseColor("#424f22")


        //四种路径的填充方式，判断path的一部分属于内部还是外部，包含一个path添加多个形状叠加的规则
        //EVEN_ODD,从该位置发出一条射线，如果射线穿过的边的条数为奇数，就是内部
//        path.fillType = Path.FillType.EVEN_ODD

        //WINDING,这个模式与path添加形状的时候的Direction有关，从该位置发出一条射线，如果射线穿过的边的方向左右个数相等就是外部
        path.fillType = Path.FillType.WINDING

        //INVERSE_EVEN_ODD,与EVEN_ODD相反
//        path.fillType = Path.FillType.INVERSE_EVEN_ODD
//        path.fillType = Path.FillType.INVERSE_WINDING

//        path.addCircle(width / 2f, height / 2f - CIRCLE_RADIUS, CIRCLE_RADIUS, Path.Direction.CCW)



        //CW：顺时针
        path.addCircle(width / 2f, height / 2f - CIRCLE_RADIUS, CIRCLE_RADIUS, Path.Direction.CW)

        path.addRect(
            width / 2f - RECT_WIDTH,
            height / 2f - RECT_WIDTH,
            width / 2f + RECT_WIDTH,
            height / 2f + RECT_WIDTH,
            Path.Direction.CCW
        )

//        path.addRect(
//            width / 2f - RECT_WIDTH,
//            height / 2f - RECT_WIDTH,
//            width / 2f + RECT_WIDTH,
//            height / 2f + RECT_WIDTH,
//            Path.Direction.CW
//        )
        canvas.drawPath(path, paint)

    }
}