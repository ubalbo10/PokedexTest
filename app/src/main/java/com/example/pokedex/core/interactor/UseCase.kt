package net.livinapp.lealtad.core.interactor

import com.example.pokedex.features.pokemons.data.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This abstraction represents an execution unit for different use cases (this means than any use
 * case in the application should implement this contract).
 *
 * By convention each [UseCase] implementation will execute its job in a background thread
 * (kotlin coroutine) and will post the result in the UI thread.
 */
abstract class UseCase<out Type, in Params> where Type : Any {

    abstract suspend fun run(params: Params): Response<out Any>

    operator fun invoke(params: Params, onResult: (Response<out Any>) -> Unit = {}) {
        val job = GlobalScope.async(Dispatchers.IO) {run(params)}
        GlobalScope.launch(Dispatchers.Main) {onResult(job.await())}
    }
}