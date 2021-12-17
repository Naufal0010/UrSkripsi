package com.capstone.urskripsi.ui.content.task.list

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.capstone.urskripsi.R

class CustomTaskTitleView : AppCompatTextView {
    private var mState = 0

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context) : super(context)

    var state: Int
        get() = mState
        set(state) {
            when (state) {
                DONE -> {
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    setTextColor(ContextCompat.getColor(context, R.color.grey_text))
                }
                NORMAL -> {
                    paintFlags = 0
                    setTextColor(ContextCompat.getColor(context, R.color.black))
                }
                else -> return
            }
            mState = state
        }

    companion object {
        const val NORMAL = 0
        const val DONE = 1
    }
}
