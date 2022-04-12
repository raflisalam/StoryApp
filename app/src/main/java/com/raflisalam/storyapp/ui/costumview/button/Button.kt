package com.raflisalam.storyapp.ui.costumview.button

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.raflisalam.storyapp.R

class Button @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : AppCompatButton(context, attrs) {

    private var txtColor: Int = 0
    private var buttonPressed: Drawable
    private var buttonFocused: Drawable

    init {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        buttonFocused = ContextCompat.getDrawable(context, R.drawable.button_focused) as Drawable
        buttonPressed = ContextCompat.getDrawable(context, R.drawable.button_pressed) as Drawable
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setTextColor(txtColor)
        textSize = 14F
        gravity = Gravity.CENTER
        background = if (isPressed) buttonPressed else buttonFocused
    }
}