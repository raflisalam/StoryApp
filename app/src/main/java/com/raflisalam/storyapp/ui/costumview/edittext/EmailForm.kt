package com.raflisalam.storyapp.ui.costumview.edittext

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.raflisalam.storyapp.R

class EmailForm @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private var txtColor: Int = 0
    private var backgroundForm: Drawable
    private var clearTextButton: Drawable

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = backgroundForm
        textSize = 14f
        setTextColor(txtColor)
    }

    init {
        txtColor = ContextCompat.getColor(context, R.color.font)
        backgroundForm = ContextCompat.getDrawable(context, R.drawable.bg_form) as Drawable
        clearTextButton = ContextCompat.getDrawable(context, R.drawable.ic_clear) as Drawable
        setOnTouchListener(this)
        setupTextChangedListener()
    }

    private fun setupTextChangedListener() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()
                if (email.isEmpty()) {
                    clearFocus()
                } else if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(context,"Valid email address",Toast.LENGTH_SHORT).show()
                } else {
                    error = "Format email salah"
                }
            }
        })
    }

    fun showClearButton() {
        setButtonDrawable(endOfTheText = clearTextButton)
    }

    fun hideClearButton() {
        setButtonDrawable()
    }

    private fun setButtonDrawable(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (clearTextButton.intrinsicWidth + paddingStart).toFloat()
                when {
                    event?.x!! < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - clearTextButton.intrinsicWidth).toFloat()
                when {
                    event?.x!! > clearButtonStart -> isClearButtonClicked = true
                }
            }
            if (isClearButtonClicked) {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        clearTextButton = ContextCompat.getDrawable(context, R.drawable.ic_clear) as Drawable
                        showClearButton()
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        clearTextButton = ContextCompat.getDrawable(context, R.drawable.ic_clear) as Drawable
                        when {
                            text != null -> text?.clear()
                        }
                        hideClearButton()
                        return true
                    }
                    else -> return false
                }
            } else return false
        }
        return false
    }
}