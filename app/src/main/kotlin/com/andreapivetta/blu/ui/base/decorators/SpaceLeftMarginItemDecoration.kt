package com.andreapivetta.blu.ui.base.decorators

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class SpaceLeftMarginItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        outRect.bottom = space

        if (parent.getChildAdapterPosition(view) == 0)
            outRect.left = space
    }
}
