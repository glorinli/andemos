package xyz.glorin.layoutinflaterdemo

import android.content.Context
import android.util.AttributeSet
import android.view.View

class HeavyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    init {
        Thread.sleep(1000)
    }
}