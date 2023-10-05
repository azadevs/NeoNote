package android.dev.kalmurzaeff.notesapp.data.models

import java.io.Serializable

data class OnBoardData(
    val title: String,
    val image: Int,
    val description: String
) : Serializable