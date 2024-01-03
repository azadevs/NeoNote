package android.dev.kalmurzaeff.neonote.utils

/**
 * Created by : KalmurzaeffDev_A
 * Date : 1/3/2024
 */

sealed class Resource<out T> {

    data class Success<T>(val notes: T) : Resource<T>()

    data class Error(val messages: String) : Resource<Nothing>()

    data object Loading : Resource<Nothing>()
}
