package xyz.glorin.coveodemo

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object KeyboardUtil {
    fun hideKeyBoard(editText: EditText) {
        val inputMethodManager =
            editText.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            editText.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
        editText.clearFocus()
    }
}