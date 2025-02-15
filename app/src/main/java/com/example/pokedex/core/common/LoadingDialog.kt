package com.example.pokedex.core.common

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.example.pokedex.R
import net.livinapp.lealtad.R
import net.livinapp.lealtad.databinding.ViewLoadingBinding

class LoadingDialog private constructor(context: Context) {

    private val mBinding: ViewLoadingBinding
    private val mDialog: Dialog

    companion object {
        fun create(context: Context): LoadingDialog {
            return LoadingDialog(context)
        }
    }
    init {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.view_loading, null)
        mBinding = ViewLoadingBinding.bind(view)
        mDialog = Dialog(context, R.style.ProgressDialog)
        val window = mDialog.window
        if (window != null) window.statusBarColor = context.getColor(R.color.white)

        mDialog.setContentView(mBinding.root)

    }

    fun cancelable(cancel: Boolean): LoadingDialog {
        mDialog.setCancelable(cancel)
        return this
    }

    fun show() {
        if (!mDialog.isShowing) mDialog.show()
    }

    fun cancel() {
        if (mDialog.isShowing) mDialog.cancel()
    }
}