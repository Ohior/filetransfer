package com.example.filetransfer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.filetransfer.R
import com.example.filetransfer.tools.Constant
import kotlin.math.pow


class ApkAdapter(
    private val context: Context,
    private val recycler_view: RecyclerView,
) :
    RecyclerView.Adapter<ApkAdapter.ViewHolder>() {
    private val arrayList = ArrayList<ApkDataClass>()
    private lateinit var onClickListener: OnClickListener

    init {
        this.recycler_view.layoutManager = CustomGridLayoutManager(context, Constant.COLUMN_COUNT)
        this.recycler_view.adapter = this
    }

    interface OnClickListener {
        fun onItemClick(position: Int, view: View)
        fun onItemLongClick(position: Int, view: View)
    }

    fun clickListener(on_click_listener: OnClickListener){
        onClickListener = on_click_listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_apk_adapter, parent, false)
        return ViewHolder(view, onClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val apkItems = arrayList[position]
        holder.name.text = apkItems.name
        holder.apkPath.text = apkItems.apk_path
        holder.size.text = getReadableSize(apkItems.apk_size)
        holder.icon.setImageDrawable(apkItems.icon)
    }

    private fun getReadableSize(apkSize: Long): String {
        val readableSize = if (apkSize < 1024){
            String.format(context.getString(R.string.app_b_size), apkSize.toDouble())
        } else if (apkSize < 1024.0.pow(2)){
            String.format(context.getString(R.string.app_kb_size), (apkSize/1024).toDouble())
        }else if (apkSize < 1024.0.pow(3)){
            String.format(context.getString(R.string.app_kb_size), (apkSize/1024.0.pow(2).toDouble()))
        } else{
            String.format(context.getString(R.string.app_b_size), apkSize/(1024.0.pow(3).toDouble()))
        }
        return readableSize
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    fun addToAdapter(element: ApkDataClass) {
        // add item to your recyclerview
        arrayList.add(element)
    }

    fun getArrayList()=arrayList

    inner class ViewHolder(
        private val item_view: View,
        private val on_click_listener: OnClickListener
    ) : RecyclerView.ViewHolder(item_view) {
        val name = item_view.findViewById<TextView>(R.id.id_tv_apk_name)
        val icon = item_view.findViewById<ImageView>(R.id.id_iv_apk_icon)
        val apkPath = item_view.findViewById<TextView>(R.id.id_tv_apk_path)
        val size = item_view.findViewById<TextView>(R.id.id_tv_apk_size)
        init {
            item_view.setOnClickListener{
                on_click_listener.onItemClick(adapterPosition, it)
            }
            item_view.setOnLongClickListener {
                on_click_listener.onItemLongClick(adapterPosition, it)
                true
            }
        }
    }
}
