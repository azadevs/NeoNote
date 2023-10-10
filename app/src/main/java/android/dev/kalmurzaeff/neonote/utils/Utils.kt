package android.dev.kalmurzaeff.neonote.utils

import android.dev.kalmurzaeff.neonote.data.models.OnBoardData
import android.dev.kalmurzaeff.notesapp.R
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.N)
fun convertToDate(milliSeconds: Long): String {
    // Create a DateFormatter object for displaying date in specified format.
    val formatter = SimpleDateFormat("dd-MMMM", Locale.UK)
    val date = Date(milliSeconds)
    return formatter.format(date)
}

fun getOnBoardDataList(): List<OnBoardData> {
    return mutableListOf(
        OnBoardData(
            "Welcome onBoard :)",
            R.drawable.notes,
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry"
        ), OnBoardData(
            "Get thins done with todo :)",
            R.drawable.taking_note,
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry"
        ),
        OnBoardData(
            "Let's create your first note :)",
            R.drawable.add_note,
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry"
        )
    )
}

fun colors(): List<String> {
    return mutableListOf(
        "#0000FF",
        "#FFFF00",
        "#FF6347",
        "#98FB98",
        "#32CD32",
        "#FF7F50",
        "#87CEFA",
        "#FFDEAD",
    )
}


