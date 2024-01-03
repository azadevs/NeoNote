package android.dev.kalmurzaeff.neonote.data.model

import java.io.Serializable

data class OnBoardData(
    val title: String,
    val image: Int,
    val description: String
) : Serializable