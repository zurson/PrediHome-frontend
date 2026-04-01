package com.betoniarze.predihome.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.TextUnit
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt
import kotlin.reflect.KClass


val polishToNormal by lazy {
    mapOf(
        'ą' to 'a',
        'ć' to 'c',
        'ę' to 'e',
        'ł' to 'l',
        'ń' to 'n',
        'ó' to 'o',
        'ś' to 's',
        'ź' to 'z',
        'ż' to 'z',
        'Ą' to 'A',
        'Ć' to 'C',
        'Ę' to 'E',
        'Ł' to 'L',
        'Ń' to 'N',
        'Ó' to 'O',
        'Ś' to 'S',
        'Ź' to 'Z',
        'Ż' to 'Z'
    )
}

fun replacePolishCharacters(input: String): String {
    return input.map { polishToNormal[it] ?: it }.joinToString("")
}


@Composable
fun dpToSp(@DimenRes id: Int): TextUnit {
    return with(LocalDensity.current) { dimensionResource(id).toSp() }
}


fun changeActivity(context: Context, activity: KClass<*>, finish: Boolean = false) {
    val intent = Intent(context, activity.java)
    context.startActivity(intent)

    if (finish)
        (context as? Activity)?.finish()
}


fun showToast(context: Context, text: String?, toastLength: Int = Toast.LENGTH_SHORT) {
    text?.let {
        (context as? Activity)?.runOnUiThread {
            Toast.makeText(context, it, toastLength).show()
        }
    }
}


fun roundToOnePlace(number: Float): Float = ((number * 10).roundToInt() / 10.0).toFloat()


fun roundToOnePlace(number: Double): Float = ((number * 10).roundToInt() / 10.0).toFloat()


fun roundToFourPlace(number: Double): Double = ((number * 10_000).roundToInt() / 10_000.0)


fun formatTimestamp(timestamp: Timestamp?): String {
    if (timestamp == null) return ""
    val date = timestamp.toDate()
    val dateFormat = SimpleDateFormat("dd, MMM\nyyyy", Locale.getDefault())
    return dateFormat.format(date)
}
