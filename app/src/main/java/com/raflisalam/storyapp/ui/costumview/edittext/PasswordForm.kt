package com.raflisalam.storyapp.ui.costumview.edittext

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.raflisalam.storyapp.R

class PasswordForm @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : AppCompatEditText(context, attrs) {

    private var txtColor: Int = 0
    private var backgroundForm: Drawable


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = backgroundForm
        textSize = 14f

        setTextColor(txtColor)
    }

    init {
        txtColor = ContextCompat.getColor(context, R.color.font)
        backgroundForm = ContextCompat.getDrawable(context, R.drawable.bg_form) as Drawable

        setupTextChangedListener()
    }

    private fun setupTextChangedListener() {

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                setErrorPassword(text)
            }

        })
    }

    private fun setErrorPassword(password: String) {
        if (password.length <= 6) {
            error = "Password Anda Kurang dari 6 Karakter"
        }
    }

}