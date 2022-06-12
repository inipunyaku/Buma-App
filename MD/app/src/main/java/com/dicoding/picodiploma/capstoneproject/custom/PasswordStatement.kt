package com.dicoding.picodiploma.submissionaplikasistoryapp.view.edit

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.dicoding.picodiploma.capstoneproject.R
import com.google.android.material.textfield.TextInputEditText

class PasswordStatement: TextInputEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, atribut: AttributeSet) : super(context, atribut) {
        init()
    }

    constructor(context: Context, atribut: AttributeSet, defStyleAttr: Int) : super(
        context,
        atribut,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas){
        super.onDraw(canvas)
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().length < 6) {
                    setError(context.getString(R.string.password_statement), null)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }
}