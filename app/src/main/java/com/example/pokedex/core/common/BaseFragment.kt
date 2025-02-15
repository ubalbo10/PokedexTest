package net.livinapp.lealtad.core.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.pokedex.core.common.LoadingDialog

abstract class BaseFragment: Fragment() {

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLoadingDialog()
    }


    private fun initLoadingDialog() {
        loadingDialog = LoadingDialog.create(requireContext())
        loadingDialog.cancelable(false)
    }

    fun showLoadingDialog() {
        loadingDialog.show()
    }

    fun hideLoadingDialog() {
        loadingDialog.cancel()
    }


}