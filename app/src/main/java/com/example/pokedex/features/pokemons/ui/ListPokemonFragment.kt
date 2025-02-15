package com.example.pokedex.features.pokemons.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pokedex.databinding.FragmentPokemonListBinding import dagger.hilt.android.AndroidEntryPoint
import net.livinapp.lealtad.core.common.BaseFragment

@AndroidEntryPoint
class ListPokemonFragment : BaseFragment() {
    private lateinit var _binding: FragmentPokemonListBinding
    private val awardViewModel by viewModels<AwardViewModel>()
    private var points = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAwardDetailBinding.inflate(inflater)
        requireActivity().window.statusBarColor =
            requireActivity().getColor(R.color.primary_accent_dark)
        initView()
        responseData()
        return _binding.root
    }

    private fun responseData() {
        with(awardViewModel) {
            awardState.observe(viewLifecycleOwner) { resp ->
                when (resp) {
                    is Response.Loading -> {
                        showLoadingDialog()
                    }

                    is Response.Success -> {
                        hideLoadingDialog()
                        val resultAward = resp.data?.result
                        if (resultAward != null) {
                            award = resultAward
                            debug("Award  $award")
                            initData(award)
                            val currentPoints: Int = points
                            if (currentPoints >= resultAward.points!!) {
                                val qr = resultAward.qr ?: ""
                                val qrCode: QRCode =
                                    QRCode.from(qr)
                                qrCode.withSize(250, 250) // Ajustar el tamanio del qr
                                val bitmap: Bitmap = qrCode.bitmap()
                                _binding.imgQr.setImageBitmap(bitmap)
                                _binding.contentQr.show()
                                _binding.withoutQR.hide()

                            } else {
                                _binding.withoutQR.show()
                                _binding.contentQr.hide()
                            }
                        } else {
                            val errorMessage = getString(
                                R.string.error_login_default_error
                            )
                            showAlert(
                                requireContext(),
                                getString(R.string.error_title_alert),
                                errorMessage
                            )
                        }
                    }

                    is Response.Error -> {
                        hideLoadingDialog()
                        when (resp.exception) {
                            is AwardNotFoundException -> {
                                val error = (resp.message)
                                showAlert(
                                    requireContext(),
                                    getString(R.string.error_title_alert),
                                    error
                                )
                            }

                            is ErrorServerApiException -> {
                                val error = (resp.message)
                                showAlert(
                                    requireContext(),
                                    getString(R.string.error_title_alert),
                                    error
                                )
                            }

                            else -> {
                                val error = (resp.message)
                                showAlert(
                                    requireContext(),
                                    getString(R.string.error_title_alert),
                                    error
                                )
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    private fun initView() {
        _binding.apply {
            imageBack.setOnClickListener {
                findNavController().popBackStack()
            }
            home.setOnClickListener {
                findNavController().navigate(R.id.homeMultiFragment)
            }
        }
        award = Gson().fromJson(args.award, Award::class.java)
        points = args.points

        awardViewModel.getAward(award.id ?: 0)
        //initData(award)
    }

    private fun initData(award: Award) {
        _binding.apply {
            imgProduct.load(award.image ?: "")
            txvPoints.text = getString(R.string.current_points, award.points)
            txvProductName.text = award.name ?: ""
            txvProductBrand.text = award.brand ?: ""
            txvCodeQr.text = getString(R.string.code_qr, award.sku)
            txvProductExpirationDate.text = award.expirationDate?.let { formatDate(it) }
            txvProductDescription.text = award.description ?: ""
            val mAdapter = InventoryAdapter(award.inventories as MutableList<Inventory>)
            listBranchOffice.adapter = mAdapter
        }
    }
}