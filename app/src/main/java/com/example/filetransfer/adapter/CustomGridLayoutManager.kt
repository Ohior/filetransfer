package com.example.filetransfer.adapter

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomGridLayoutManager(val context: Context, column_count: Int = 1)
    : GridLayoutManager(context, column_count, VERTICAL, false) {

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            Log.e("CustomGridLayout","Inconsistency detected")
        }
    }
}