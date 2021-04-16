package com.nan.myandroid.databinding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nan.myandroid.R

/**
 *author：93289
 *date:2020/8/25
 *dsc:
 */
class TwoWayRwAdapter: RecyclerView.Adapter<TwoWayRwVH>{
    var users :MutableList<ObservableUser>? =null
    var context: Context?=null

    fun setData(users :MutableList<ObservableUser>){
        this.users=users
        notifyDataSetChanged()
    }


    constructor(users :MutableList<ObservableUser>,context: Context){
        this.users=users
        this.context=context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TwoWayRwVH {
        val binding = DataBindingUtil.inflate<ItemRwTwoWayDatabindingBinding>(
            LayoutInflater.from(context!!),
            R.layout.item_rw_two_way_databinding,
            null,
            false
        )
    return TwoWayRwVH(binding.root)
    }

    override fun getItemCount(): Int {
      return users!!.size
    }

    override fun onBindViewHolder(holder: TwoWayRwVH, position: Int) {
        DataBindingUtil.getBinding<ItemRwTwoWayDatabindingBinding>(holder.itemView)?.let {
            it.defaultName="默认名字"
            it.user=users!![position]
            it.root.setOnClickListener {rootview->
                it.tvUsername.text="反向数据"
            }
            it.executePendingBindings()

        }
    }
}

class TwoWayRwVH(itemView: View) : RecyclerView.ViewHolder(itemView)