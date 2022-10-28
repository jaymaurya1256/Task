package com.example.task.models

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

enum class ClickType {
    SHORT,
    LONG_DELETE,
    LONG_EDIT
}

enum class Priority {
    LOW,
    MEDIUM,
    HIGH;
}

fun Fragment.hideKeyboard() {
    val view = requireActivity().currentFocus
    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.windowToken, 0)
}

fun Fragment.showKeyboard() {
    val view = requireActivity().currentFocus
    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, 0)
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}