package com.example.pokedex.features.pokemons.useCases

import androidx.annotation.Keep
import net.livinapp.lealtad.core.interactor.UseCaseFlow
import net.livinapp.lealtad.features.awards.data.Award
import net.livinapp.lealtad.features.awards.data.AwardRepository
import net.livinapp.lealtad.features.home.data.ApiResponseBase
import javax.inject.Inject

class DoGetPokemonList
@Inject constructor(private val awardRepository: )
    : UseCaseFlow<ApiResponseBase<Award>, DoGetAward.Params>() {

    override suspend fun run(params: Params) = awardRepository.getAward(params.id)

    @Keep
    data class Params(val id: Int)
}