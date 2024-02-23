package com.university.termomaps.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class BottomMarginItemDecoration(private val bottomMargin: Int) : RecyclerView.ItemDecoration() {

  override fun getItemOffsets(
    outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State,
  ) {
    super.getItemOffsets(outRect, view, parent, state)

    val position = parent.getChildAdapterPosition(view)
    // Check if it's the last item
    if (position == state.itemCount - 1) {
      outRect.bottom = bottomMargin
    } else {
      outRect.bottom = 0
    }
  }
}