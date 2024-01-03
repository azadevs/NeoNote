package android.dev.kalmurzaeff.neonote.data.local.prefs

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

object SharedPrefsManager {
    private var sharedPreferences: SharedPreferences? = null
    private const val KEY_ONBOARD = "isHave"
    private const val NAME_FILE = "Temp"

    fun init(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(NAME_FILE, Activity.MODE_PRIVATE)
        }
    }

    fun readData(defValue: Boolean): Boolean {
        return sharedPreferences?.getBoolean(KEY_ONBOARD, defValue) == true
    }

    fun writeData(value: Boolean) {
        val editor = sharedPreferences?.edit()
        editor?.putBoolean(KEY_ONBOARD, value)
        editor?.apply()
    }
}
