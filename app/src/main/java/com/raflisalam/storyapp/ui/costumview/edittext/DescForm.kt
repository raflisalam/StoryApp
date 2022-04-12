package com.raflisalam.storyapp.ui.costumview.edittext

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.raflisalam.storyapp.R

class DescForm @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : AppCompatEditText(context, attrs) {

    private var txtColor: Int = 0
    private var backgroundForm: Drawable

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = backgroundForm
        hint = "Tulis keterangan..."
        textSize = 14f
        setTextColor(txtColor)
    }

    init {
        txtColor = ContextCompat.getColor(context, R.color.font)
        backgroundForm = ContextCompat.getDrawable(context, R.drawable.bg_form_transparent) as Drawable

    }
}