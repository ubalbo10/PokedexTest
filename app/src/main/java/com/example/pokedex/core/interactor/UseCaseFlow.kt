package net.livinapp.lealtad.core.interactor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import net.livinapp.lealtad.features.auth.data.Response

abstract class UseCaseFlow<out Type, in Params> where Type : Any  {

    abstract suspend fun run(params: Params): Flow<Response<Type>>

    operator fun invoke(params: Params, onResult: (Flow<Response<Type>>) -> Unit = {}) {
        val job = GlobalScope.async(Dispatchers.IO) { run(params) }
        GlobalScope.launch(Dispatchers.Main) { onResult(job.await()) }
    }
}