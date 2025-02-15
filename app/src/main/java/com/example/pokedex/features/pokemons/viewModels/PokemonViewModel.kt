package com.example.pokedex.features.pokemons.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AwardViewModel
@Inject constructor(
    private val doGetAward: DoGetAward,
    private val doGetFilter: DoGetFilter
): ViewModel(){
    private val award = MutableLiveData<Response<ApiResponseBase<Award>>>(Response.Loading)
    val awardState: LiveData<Response<ApiResponseBase<Award>>> = award

    private val filter = MutableLiveData<Response<ApiResponseBase<FilterPlanResponse>>>()
    val filterState: LiveData<Response<ApiResponseBase<FilterPlanResponse>>> = filter

    fun getAward(id: Int) =
        doGetAward(DoGetAward.Params(id)) { useCase ->
            viewModelScope.launch {
                useCase.collect{ response ->
                    award.value = response
                }
            }
        }

    fun getFilter(plan: Int) =
        doGetFilter(DoGetFilter.Params(plan)) { useCase ->
            viewModelScope.launch {
                useCase.collect{ response ->
                    filter.value = response
                }
            }
        }

}