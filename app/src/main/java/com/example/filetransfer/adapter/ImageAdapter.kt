package com.example.filetransfer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.filetransfer.R
import com.squareup.picasso.Picasso
import java.io.File



class ImageAdapter(
    private val context: Context,
    private val recycler_view: RecyclerView,
    column_count: Int = 1
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    private val array_list = ArrayList<ImageDataClass>()
    private lateinit var clickListener: OnClickListener

    init {
        this.recycler_view.layoutManager = CustomGridLayoutManager(context, column_count)
        this.recycler_view.adapter = this
    }

    interface OnClickListener {
        fun onItemClick(position: Int, view: View)
        fun onLongItemClick(position: Int, view: View)

    }

    fun setOnItemClickListener(listener: OnClickListener) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_adapter_view, parent, false)
        return ViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //bind data to viewholder
        val imageitem = array_list[position]

        val file = File(imageitem.image_url)
        if (file.exists()) {
            Picasso.get().load(File(imageitem.file_location))
                .placeholder(R.drawable.ic_launcher_background).into(holder.imageUri)
            holder.title.text = imageitem.title
            holder.fileLocation.text = imageitem.file_location
        }
    }

    override fun getItemCount(): Int {
        return array_list.size
    }

    fun clearAdapter() {
        //remove all item from your recyclerview
        array_list.clear()
    }

    fun removeItem(index: Int) {
        array_list.removeAt(index)
    }

    fun addToAdapter(element: ImageDataClass) {
        // add item to your recyclerview
        array_list.add(element)
    }

    fun getItem(index: Int): ImageDataClass {
        return array_list[index]
    }

    fun addToAdapter(index: Int, element: ImageDataClass) {
        // add item to an index spot of your recyclerview
        array_list.add(index, element)
    }

    inner class ViewHolder(item_view: View, listener: OnClickListener) :
        RecyclerView.ViewHolder(item_view) {
        val imageUri: ImageView = item_view.findViewById(R.id.id_iv_item_image)
        val title: TextView = item_view.findViewById(R.id.id_tv_item_title)
        val fileLocation: TextView = item_view.findViewById(R.id.id_tv_item_location)


        init {
            item_view.setOnLongClickListener {
                listener.onLongItemClick(adapterPosition, item_view)
                true
            }
            item_view.setOnClickListener {
                listener.onItemClick(adapterPosition, item_view)
            }
        }
    }
}